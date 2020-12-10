package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
    private final ImportsConfig importsConfig;
    private final int FIRST_SHEET = 0;

    public ExcelBillImporter(BillService billService, ExcelRowProcessor rowProcessor, ImportsConfig importsConfig) {
        this.billService = billService;
        this.rowProcessor = rowProcessor;
        this.importsConfig = importsConfig;
    }

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
        Sheet sheet = workbook.getSheetAt(FIRST_SHEET);
        log.debug("Sheet contains {} rows.", sheet.getLastRowNum());
        return  processSheet(sheet);
    }

    private boolean processSheet(Sheet sheet) {
        boolean successProcessingRows = true;
        final Map<ImportBill, Bill> billsToImport = new LinkedHashMap<>();
        for (Row row : sheet) {
          try {
              processRow(row, billsToImport);
          } catch (ExcelRowProcessingException e) {
              log.error("Exception occurred during processing o row:" + row.getRowNum()+ " from excel file.", e);
              successProcessingRows = false;
          }
        }
        saveLastBillsBatch(billsToImport);
        return successProcessingRows;
    }

    private void saveLastBillsBatch(Map<ImportBill, Bill> billsToImport) {
        billService.saveAll(new LinkedList<>(billsToImport.values()));
    }

    private void logFinishProcessingMessage(boolean successProcessingRow) {
        if (successProcessingRow) {
            log.info("Bill import process finished with success");
        } else {
            log.info("Bill import process finished with errors. See logs for details");
        }
    }

    private void processRow(Row row, Map<ImportBill, Bill> billsToImport) {
        log.debug("Processing row:" + row.getRowNum());
        if (0 == row.getRowNum()) {
            log.debug("Skip processing sheet header row.");
            return;
        }
        rowProcessor.logRowInDebug(row);
        final ImportBill importBill = rowProcessor.processBill(row);
        final BillItem billItem = rowProcessor.processBillItem(row);
        if (billsToImport.get(importBill) == null) {
            saveBillsInBatch(billsToImport);
            billsToImport.put(importBill, importBill.toBill());
        }
        billsToImport.get(importBill).addItem(billItem);
    }

    private void saveBillsInBatch(Map<ImportBill, Bill> billsToImport) {
        if (billsToImport.size() == importsConfig.getBillsBatchSize()) {
            billService.saveAll(new LinkedList<>(billsToImport.values()));
            billsToImport.clear();
        }
    }
}
