package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.alexm.bearspendings.imports.ExcelRowProcessor.CELL_COLUMN.*;
import static com.alexm.bearspendings.imports.RowProcessingException.ERROR_CODE.*;
import static java.util.Objects.isNull;

/**
 * @author AlexM
 * Date: 4/26/20
 **/
@Slf4j
@Component
public class ExcelRowProcessor {
    static final String DATE_PATTERN = "yy/M/d";
    private static final String SPACE = " ";

    private final StoreService storeService;
    private final ProductService productService;

    //todo: rename to cell column
    enum CELL_COLUMN {
        ORDER_DATE_CELL(1),
        STORE_CELL(14),
        PRODUCT_CELL(5),
        QUANTITY_CELL(10),
        PRICE_CELL(11);

        int index;

        CELL_COLUMN(int index) {
            this.index = index;
        }
    }

    public ExcelRowProcessor(StoreService storeService, ProductService productService) {
        this.storeService = storeService;
        this.productService = productService;
    }

    public void logRowInDebug(Row row) {
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

    public ImportBill processBill(Row row) throws RowProcessingException {
        LocalDate orderDate = parseOrderDate(row);
        log.debug("--- order date:" + orderDate.format(DateTimeFormatter.ISO_DATE));
        Store store = parseStore(nonEmptyCellValue(row, STORE_CELL));
        log.debug("--- store: " + store);
        return new ImportBill(orderDate, store);
    }

    public BillItem processBillItem(Row row) throws RowProcessingException {
        Product product = parseProduct(nonEmptyCellValue(row, PRODUCT_CELL));
        log.debug("--- product:{}", product);
        BillItem billItem = BillItem.builder().product(product).build();
        final Double price = doubleCellValue(row, PRICE_CELL);
        log.debug("--- price:{}", product);
        billItem.setPrice(price);
        final Double quantity = doubleCellValue(row, QUANTITY_CELL);
        log.debug("--- quantity:{}", quantity);
        billItem.setQuantity(quantity);
        return billItem;
    }

    private Product parseProduct(String productName) {
        return productService.getOrInsert(productName);
    }

    private Store parseStore(String storeName) {
        return storeService.getOrInsert(storeName);
    }

    private LocalDate parseOrderDate(Row row) throws RowProcessingException {
        String dateValue = nonEmptyCellValue(row, ORDER_DATE_CELL);
        try {
            return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(DATE_PATTERN));
        } catch (DateTimeParseException e) {
            throw new RowProcessingException(INVALID_DATE_VALUE, row, ORDER_DATE_CELL, e);
        }
    }

    private String nonEmptyCellValue(Row row, CELL_COLUMN cellColumn) throws RowProcessingException {
        Cell cell = nonNullCell(row, cellColumn);
        String value = cell.getStringCellValue();
        if(StringUtils.isEmpty(value)) {
            throw new RowProcessingException(EMPTY_CELL, row, cellColumn);
        }
        return value;
    }

    private Double doubleCellValue(Row row, CELL_COLUMN cellColumn) throws RowProcessingException {
        Cell cell = nonNullCell(row, cellColumn);
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new RowProcessingException(INVALID_DOUBLE_VALUE, row, cellColumn, e);
        }
    }

    private Cell nonNullCell(Row row, CELL_COLUMN cellColumn) throws RowProcessingException {
        Cell cell = row.getCell(cellColumn.index);
        if (isNull(cell)) {
            throw new RowProcessingException(NULL_CELL, row, cellColumn);
        }
        return cell;
    }
}
