package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.CategoryService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static com.alexm.bearspendings.imports.excel.ExcelRowProcessingException.ERROR_CODE.*;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.CELL_COLUMN.*;
import static java.util.Objects.isNull;
import static java.util.function.Predicate.not;

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
    private final CategoryService categoryService;

    public enum CELL_COLUMN {
        ORDER_DATE_CELL(1),
        STORE_CELL(14),
        PRODUCT_CELL(5),
        QUANTITY_CELL(10),
        CATEGORY_CELL(8),
        SUB_SUB_CATEGORY_CELL(6),
        SUB_CATEGORY_CELL(7),
        TOTAL_PRICE_CELL(12),
        PRICE_PER_UNIT_CELL(11);

        int index;

        CELL_COLUMN(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public ExcelRowProcessor(StoreService storeService, ProductService productService, CategoryService categoryService) {
        this.storeService = storeService;
        this.productService = productService;
        this.categoryService = categoryService;
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

    public ImportBill processBill(Row row) throws ExcelRowProcessingException {
        LocalDate orderDate = parseOrderDate(row);
        log.debug("--- order date:" + orderDate.format(DateTimeFormatter.ISO_DATE));
        Store store = parseStore(nonEmptyCellValue(row, STORE_CELL));
        log.debug("--- store: " + store);
        return new ImportBill(orderDate, store);
    }

    public BillItem processBillItem(Row row) throws ExcelRowProcessingException {
        Category category = parseCategory(nonEmptyCellValue(row, CATEGORY_CELL), nonEmptyCellValue(row, SUB_CATEGORY_CELL),
                row.getCell(SUB_SUB_CATEGORY_CELL.index).getStringCellValue());
        Product product = parseProduct(nonEmptyCellValue(row, PRODUCT_CELL),category);
        log.debug("--- product:{}", product);
        BillItem billItem = BillItem.builder().product(product).build();
        final Double pricePerUnit = doubleCellValue(row, PRICE_PER_UNIT_CELL);
        log.debug("--- pricePerUnit:{}", product);
        billItem.setPricePerUnit(pricePerUnit);
        final Double quantity = doubleCellValue(row, QUANTITY_CELL);
        log.debug("--- quantity:{}", quantity);
        billItem.setQuantity(quantity);
        return billItem;
    }

    private Category parseCategory(@NonNull String categoryName, @NonNull String subCategoryName,@Nullable String subSubCategoryName) {
        return categoryService.getOrInsert(Stream
                .of(categoryName, subCategoryName, subSubCategoryName)
                .filter(not(StringUtils::isEmpty))
                .toArray(String[]::new)
        );
    }

    private Product parseProduct(String productName, Category category) {
        return productService.getOrInsert(productName, category);
    }

    private Store parseStore(String storeName) {
        return storeService.getOrInsert(storeName);
    }

    private LocalDate parseOrderDate(Row row) throws ExcelRowProcessingException {
        String dateValue = nonEmptyCellValue(row, ORDER_DATE_CELL);
        try {
            return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(DATE_PATTERN));
        } catch (DateTimeParseException e) {
            throw new ExcelRowProcessingException(INVALID_DATE_VALUE, row, ORDER_DATE_CELL, e);
        }
    }

    private String nonEmptyCellValue(Row row, CELL_COLUMN cellColumn) throws ExcelRowProcessingException {
        Cell cell = nonNullCell(row, cellColumn);
        String value = cell.getStringCellValue();
        if(StringUtils.isEmpty(value)) {
            throw new ExcelRowProcessingException(EMPTY_CELL, row, cellColumn);
        }
        return value;
    }

    private Double doubleCellValue(Row row, CELL_COLUMN cellColumn) throws ExcelRowProcessingException {
        Cell cell = nonNullCell(row, cellColumn);
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new ExcelRowProcessingException(INVALID_DOUBLE_VALUE, row, cellColumn, e);
        }
    }

    private Cell nonNullCell(Row row, CELL_COLUMN cellColumn) throws ExcelRowProcessingException {
        Cell cell = row.getCell(cellColumn.index);
        if (isNull(cell)) {
            throw new ExcelRowProcessingException(NULL_CELL, row, cellColumn);
        }
        return cell;
    }
}
