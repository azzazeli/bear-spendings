package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@Slf4j
@Component
public class ExcelBillImporter implements BillImporter {
    private static final String SPACE = " ";
    private static final int ORDER_DATE_CELL_INDEX = 1;
    private static final int STORE_CELL_INDEX = 14;

    private final StoreService storeService;

    public ExcelBillImporter(StoreService storeService) {
        this.storeService = storeService;
    }


    @Override
    public void imports(Path source) throws ImportsException {
        if(!Files.exists(source)) {
            throw new ImportsException(String.format("Invalid source for bills import. Path %1$s does not exists", source.toString()));
        }
        log.info("Starting bill import process");
        boolean successProcessingRow = true;
        try(Workbook workbook = new XSSFWorkbook(source.toFile())) {
            Sheet sheet = workbook.getSheetAt(0);
            log.debug("Sheet contains {} rows.", sheet.getLastRowNum());
            for (Row row : sheet) {
                successProcessingRow = successProcessingRow && processRow(row);
            }
        } catch (IOException |  InvalidFormatException e) {
            throw new ImportsException("Failed to load XSSFWorkbook." , e);
        }
        logFinishProcessingMessage(successProcessingRow);
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
            // skip header
            log.debug("Skip processing sheet header row.");
            return withSuccess;
        }
        logRowInDebug(row);
        try {
            LocalDate orderDate = parseOrderDate(row.getCell(ORDER_DATE_CELL_INDEX));
            log.debug("--- order date:" + orderDate.format(DateTimeFormatter.ISO_DATE));
            Store store = parseStore(row.getCell(STORE_CELL_INDEX));
            log.debug("--- store: " + store);
        } catch (RowProcessingException e) {
            log.error("Exception occurred during processing o row:" + row.getRowNum()+ " from excel file.", e);
            withSuccess = false;
        }
        return withSuccess;
    }

    private Store parseStore(Cell cell) throws RowProcessingException {
        String storeName = cell.getStringCellValue();
        if(StringUtils.isEmpty(storeName)) {
            throw new RowProcessingException("No store name found in provided cell. column index:" + cell.getColumnIndex());
        }
        return storeService.getOrInsert(storeName);
    }

    private LocalDate parseOrderDate(Cell cell) throws RowProcessingException {
        try {
            return LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yy/M/d"));
        } catch (DateTimeParseException e) {
            throw new RowProcessingException(e);
        }
    }

    private void logRowInDebug(Row row) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder(row.getRowNum());
            sb.append(SPACE);
            sb.append(SPACE);
            for (Cell cell : row) {
                sb.append(SPACE);
                sb.append(cell);
            }
            log.debug(sb.toString());
        }
    }
}
