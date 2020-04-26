package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.time.LocalDate;

import static com.alexm.bearspendings.imports.ExcelBillImporter.DATE_PATTERN;
import static com.alexm.bearspendings.imports.ExcelBillImporterTest.TEST_IMPORT_PRODUCTS.Medicamente;
import static com.alexm.bearspendings.imports.ExcelBillImporterTest.farmaciaFamiliei;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 * Date: 4/26/20
 **/
@ExtendWith(MockitoExtension.class)
class ExcelRowProcessorTest {
    @Mock
    StoreService mockStoreService;
    @Mock
    ProductService mockProductService;

    ExcelRowProcessor processor;
    private static Row row;

    @BeforeAll
    static void sampleRow() throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(ResourceUtils.getFile("classpath:import_sample.xlsm"));
        Sheet sheet = workbook.getSheetAt(0);
        row = sheet.getRow(1);
    }

    @BeforeEach
    void setup() {
        processor = new ExcelRowProcessor(mockStoreService, mockProductService);
    }

        @Test
    void processBill() throws Exception {
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
                .hasFieldOrPropertyWithValue("price", 284.99)
                .hasFieldOrPropertyWithValue("quantity", 1.0)
                .hasFieldOrPropertyWithValue("bill", null)
        ;
    }

    private LocalDate orderDate(String dateValue) {
        return LocalDate.parse(dateValue, ofPattern(DATE_PATTERN));
    }

    //todo: test wrong use cases
}
