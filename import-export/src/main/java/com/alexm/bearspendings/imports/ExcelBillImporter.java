package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
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
    //todo: enum here
    private static final int ORDER_DATE_CELL_INDEX = 1;
    private static final int STORE_CELL_INDEX = 14;
    private static final int PRODUCT_CELL_INDEX = 5;
    private static final int QUANTITY_CELL_INDEX = 10;
    private static final int PRICE_CELL_INDEX = 11;

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
        //todo: extract to a class
        boolean withSuccess = true;
        log.debug("Processing row:" + row.getRowNum());
        if (0 == row.getRowNum()) {
            // skip header
            log.debug("Skip processing sheet header row.");
            return true;
        }
        logRowInDebug(row);
        try {
            LocalDate orderDate = parseOrderDate(nonEmptyCellValue(row, ORDER_DATE_CELL_INDEX));
            log.debug("--- order date:" + orderDate.format(DateTimeFormatter.ISO_DATE));
            Store store = parseStore(nonEmptyCellValue(row, STORE_CELL_INDEX));
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
        Product product = parseProduct(nonEmptyCellValue(row, PRODUCT_CELL_INDEX));
        log.debug("--- product:{}", product);
        BillItem billItem = BillItem.builder().product(product).build();
        final Double price = doubleCellValue(row, PRICE_CELL_INDEX);
        log.debug("--- price:{}", product);
        billItem.setPrice(price);
        final Double quantity = doubleCellValue(row, QUANTITY_CELL_INDEX);
        log.debug("--- quantity:{}", quantity);
        billItem.setQuantity(quantity);
        bill.addItem(billItem);
    }

    private void saveBillsInBatch() {
        if(this.billsToImport.size() == billsBatchSize) {
            billService.saveAll(new ArrayList<>(billsToImport.values()));
            billsToImport.clear();
        }
    }

    private Product parseProduct(String productName) {
        return productService.getOrInsert(productName);
    }

    private Store parseStore(String storeName) {
        return storeService.getOrInsert(storeName);
    }

    private String nonEmptyCellValue(Row row, int cellIndex) throws RowProcessingException {
        Cell cell = nonNullCell(row, cellIndex);
        String value = cell.getStringCellValue();
        if(StringUtils.isEmpty(value)) {
            throw new RowProcessingException("No value name found in provided cell. column index:" + cell.getColumnIndex());
        }
        return value;
    }

    private Cell nonNullCell(Row row, int cellIndex) throws RowProcessingException {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            throw new RowProcessingException(String.format("Now cell with index %d found in row: %s", cellIndex, row.toString()));
        }
        return cell;
    }

    private Double doubleCellValue(Row row, int cellIndex) throws RowProcessingException {
        Cell cell = nonNullCell(row, cellIndex);
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new RowProcessingException("Exception occurred during extracting double value from row:" + row +
                    " cell index:" + cellIndex, e);
        }
    }

    private LocalDate parseOrderDate(String dateValue) throws RowProcessingException {
        try {
            return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(DATE_PATTERN));
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
