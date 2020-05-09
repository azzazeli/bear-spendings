package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.imports.excel.ExcelRowProcessor.CELL_COLUMN;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.time.LocalDate;

import static com.alexm.bearspendings.imports.TestImportProducts.Medicamente;
import static com.alexm.bearspendings.imports.excel.ExcelBillImporterTest.farmaciaFamiliei;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.CELL_COLUMN.ORDER_DATE_CELL;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.CELL_COLUMN.STORE_CELL;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.DATE_PATTERN;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 * Date: 4/26/20
 **/
@Slf4j
@ExtendWith(MockitoExtension.class)
class ExcelRowProcessorTest {
    @Mock
    StoreService mockStoreService;
    @Mock
    ProductService mockProductService;

    ExcelRowProcessor processor;
    private static Workbook workbook;
    private Row row;

    @BeforeAll
    static void sampleRow() throws IOException, InvalidFormatException {
        workbook = new XSSFWorkbook(ResourceUtils.getFile("classpath:import_sample.xlsm"));
    }

    @BeforeEach
    void setup() {
        processor = new ExcelRowProcessor(mockStoreService, mockProductService);
        Sheet sheet = workbook.getSheetAt(0);
        row = sheet.getRow(1);
    }

    @Test
    void processBill() throws Exception {
        log.info("test");
        long storeId = 1200L;
        when(mockStoreService.getOrInsert(farmaciaFamiliei)).thenReturn(
                Store.builder().id(storeId).name(farmaciaFamiliei).build()
        );
        final ImportBill importBill = processor.processBill(row);
        assertThat(importBill).hasFieldOrPropertyWithValue("store.id", storeId)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/10"));
    }

    @Test
    void processBillItem() throws Exception {
        when(mockProductService.getOrInsert(Medicamente.productName)).thenReturn(Medicamente.product());
        final BillItem billItem = processor.processBillItem(row);
        assertThat(billItem)
                .hasFieldOrPropertyWithValue("product.id", Medicamente.id)
                .hasFieldOrPropertyWithValue("pricePerUnit", 284.99)
                .hasFieldOrPropertyWithValue("quantity", 1.0)
                .hasFieldOrPropertyWithValue("bill", null)
        ;
    }

    @Test
    void invalidOrderDate() {
        final String defaultValue = row.getCell(ORDER_DATE_CELL.index).getStringCellValue();
        row.getCell(ORDER_DATE_CELL.index).setCellValue("wwww");
        final ExcelRowProcessingException ex = assertThrows(ExcelRowProcessingException.class, () -> processor.processBill(row));
        assertThat(ex.getMessage()).startsWith("Exception occurred during parsing date in cell index:" + ORDER_DATE_CELL.index);
        row.getCell(ORDER_DATE_CELL.index).setCellValue(defaultValue);
    }

    @ParameterizedTest
    @EnumSource(value = CELL_COLUMN.class, names = {"PRICE_PER_UNIT_CELL", "QUANTITY_CELL"})
    void invalidPriceQuantity(CELL_COLUMN cellColumn) {
        final double defaultValue = row.getCell(cellColumn.index).getNumericCellValue();
        row.getCell(cellColumn.index).setCellValue("wwww");
        final ExcelRowProcessingException ex = assertThrows(ExcelRowProcessingException.class, () -> processor.processBillItem(row));
        assertThat(ex.getMessage()).startsWith("Exception occurred during extracting double value from cell index:" + cellColumn.index);
        row.getCell(cellColumn.index).setCellValue(defaultValue);
    }

    @Test
    void nonEmptyCell() {
        final String defaultValue = row.getCell(STORE_CELL.index).getStringCellValue();
        row.getCell(STORE_CELL.index).setCellValue("");
        final ExcelRowProcessingException ex = assertThrows(ExcelRowProcessingException.class, () -> processor.processBill(row));
        assertThat(ex.getMessage()).startsWith("No value name found in provided cell. column index:" + STORE_CELL.index);
        row.getCell(STORE_CELL.index).setCellValue(defaultValue);
    }

    private LocalDate orderDate(String dateValue) {
        return LocalDate.parse(dateValue, ofPattern(DATE_PATTERN));
    }
}
