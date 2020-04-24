package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;

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
    private static final int PRODUCT_CELL_INDEX = 5;

    private final StoreService storeService;
    private final ProductService productService;
    private final BillService billService;

    private final Map<ImportBill, Bill> billsToImport = new LinkedHashMap<>();
    static final String DATE_PATTERN = "yy/M/d";

    public ExcelBillImporter(StoreService storeService, BillService billService, ProductService productService) {
        this.billService = billService;
        this.storeService = storeService;
        this.productService = productService;
    }

    @Value("#{new Integer('${com.alexm.bearspendings.imports.billsbatchsize}')}")
    private int billsBatchSize;

    public void setBillsBatchSize(int billsBatchSize) {
        this.billsBatchSize = billsBatchSize;
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
            saveLastBillsBatch();
        } catch (IOException |  InvalidFormatException e) {
            throw new ImportsException("Failed to load XSSFWorkbook." , e);
        }
        logFinishProcessingMessage(successProcessingRow);
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
            // skip header
            log.debug("Skip processing sheet header row.");
            return true;
        }
        logRowInDebug(row);
        try {
            LocalDate orderDate = parseOrderDate(getRequiredCell(row, ORDER_DATE_CELL_INDEX));
            log.debug("--- order date:" + orderDate.format(DateTimeFormatter.ISO_DATE));
            Store store = parseStore(getRequiredCell(row, STORE_CELL_INDEX));
            log.debug("--- store: " + store);
            final ImportBill importBill = new ImportBill(orderDate, store);
            if (billsToImport.get(importBill) == null) {
                saveBillsInBatch();
                billsToImport.put(importBill, Bill.builder().store(store).orderDate(orderDate.atStartOfDay()).build());
            }
            addBillItem(billsToImport.get(importBill), row);
        } catch (RowProcessingException e) {
            log.error("Exception occurred during processing o row:" + row.getRowNum()+ " from excel file.", e);
            withSuccess = false;
        }
        return withSuccess;
    }

    private void addBillItem(Bill bill, Row row) throws RowProcessingException {
        Product product = parseProduct(getRequiredCell(row, PRODUCT_CELL_INDEX));
        log.debug("--- product:{}", product);
    }

    private Cell getRequiredCell(Row row, int index) throws RowProcessingException {
        final Cell cell = row.getCell(index);
        if(isNull(cell)) {
            throw new RowProcessingException(String.format("Now cell with index %d found in row: %s", index, row.toString()));
        }
        return cell;
    }

    private void saveBillsInBatch() {
        if(this.billsToImport.size() == billsBatchSize) {
            billService.saveAll(new ArrayList<>(billsToImport.values()));
            billsToImport.clear();
        }
    }

    private Product parseProduct(Cell cell) throws RowProcessingException {
        return productService.getOrInsert(nonEmptyCellValue(cell));
    }

    private Store parseStore(Cell cell) throws RowProcessingException {
        return storeService.getOrInsert(nonEmptyCellValue(cell));
    }

    private String nonEmptyCellValue(Cell cell) throws RowProcessingException {
        String value = cell.getStringCellValue();
        if(StringUtils.isEmpty(value)) {
            throw new RowProcessingException("No value name found in provided cell. column index:" + cell.getColumnIndex());
        }
        return value;
    }

    private LocalDate parseOrderDate(Cell cell) throws RowProcessingException {
        try {
            return LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern(DATE_PATTERN));
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
