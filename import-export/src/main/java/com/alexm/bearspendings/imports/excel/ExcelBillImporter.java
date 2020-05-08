package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsConfig;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@Slf4j
@Component
public class ExcelBillImporter implements BillImporter {
    private final BillService billService;
    private final ExcelRowProcessor rowProcessor;

    private final Map<ImportBill, Bill> billsToImport = new LinkedHashMap<>();

    public ExcelBillImporter(BillService billService, ExcelRowProcessor rowProcessor, ImportsConfig importsConfig) {
        this.billService = billService;
        this.rowProcessor = rowProcessor;
        this.importsConfig = importsConfig;
    }

    private final ImportsConfig importsConfig;

    @Override
    public void imports(Path source) throws ImportsException {
        log.info("Starting bill import process of bath:" + source.toAbsolutePath());
        boolean successProcessing;
        try(Workbook workbook = new XSSFWorkbook(source.toFile())) {
            successProcessing = processWorkBook(workbook);
        } catch (IOException |  InvalidFormatException | InvalidOperationException e) {
            throw new ImportsException("Failed to load XSSFWorkbook." , e);
        }
        logFinishProcessingMessage(successProcessing);
    }

    private boolean processWorkBook(Workbook workbook) {
        boolean successProcessingRows = true;
        Sheet sheet = workbook.getSheetAt(0);
        log.debug("Sheet contains {} rows.", sheet.getLastRowNum());
        for (Row row : sheet) {
            successProcessingRows = successProcessingRows && processRow(row);
        }
        saveLastBillsBatch();
        return successProcessingRows;
    }

    private void saveLastBillsBatch() {
        billService.saveAll(billsToImport.values());
    }

    private void logFinishProcessingMessage(boolean successProcessingRow) {
        if (successProcessingRow) {
            log.info("Bill import process finished with success");
        } else {
            log.info("Bill import process finished with errors. See logs for details");
        }
    }

    private boolean processRow(Row row) {
        boolean withSuccess = true;
        log.debug("Processing row:" + row.getRowNum());
        if (0 == row.getRowNum()) {
            log.debug("Skip processing sheet header row.");
            return true;
        }
        rowProcessor.logRowInDebug(row);
        try {
            final ImportBill importBill = rowProcessor.processBill(row);
            if (billsToImport.get(importBill) == null) {
                saveBillsInBatch();
                billsToImport.put(importBill, Bill
                        .builder()
                        .store(importBill.getStore())
                        .orderDate(importBill.getOrderDate().atStartOfDay())
                        .build());
            }
            billsToImport.get(importBill).addItem(rowProcessor.processBillItem(row));
        } catch (ExcelRowProcessingException e) {
            log.error("Exception occurred during processing o row:" + row.getRowNum()+ " from excel file.", e);
            withSuccess = false;
        }
        return withSuccess;
    }

    private void saveBillsInBatch() {
        if (this.billsToImport.size() == importsConfig.getBillsBatchSize()) {
            billService.saveAll(new ArrayList<>(billsToImport.values()));
            billsToImport.clear();
        }
    }
}
