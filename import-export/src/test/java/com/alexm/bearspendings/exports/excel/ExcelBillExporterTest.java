package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.StoreService;
import com.alexm.bearspendings.test.TestBills;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.alexm.bearspendings.entity.Defaults.DEFAULT_STORE;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.CELL_COLUMN.*;
import static com.alexm.bearspendings.test.TestCategories.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 * Date: 12/10/20
 **/
@ExtendWith(MockitoExtension.class)
class ExcelBillExporterTest {

    @Mock
    BillService billService;
    @Mock
    StoreService storeService;
    @InjectMocks
    ExcelBillExporter excelBillExporter;

    @BeforeEach
    void setup() {
        when(billService.bills(0, 10)).thenReturn(TestBills.sampleBills());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void exportAll() throws IOException, InvalidFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_HHmmss");
        final String fileName = "build/export/export_" + LocalDateTime.now().format(formatter) + ".xlsm";
        File export = new File(fileName);
        export.getParentFile().mkdirs();
        FileCopyUtils.copy(excelBillExporter.exportAll(), new FileOutputStream(export));
        assertTrue(export.exists());
        try (Workbook workbook = new XSSFWorkbook(export)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(6, sheet.getLastRowNum());
            assertRow4(sheet.getRow(3));
            assertRow7(sheet.getRow(6));
        }
    }

    private void assertRow7(Row row) {
        assertThat(row.getCell(QUANTITY_CELL.index()).getNumericCellValue()).isEqualTo(1.0);
        assertThat(row.getCell(PRICE_PER_UNIT_CELL.index()).getNumericCellValue()).isEqualTo(39.0);
        assertThat(row.getCell(TOTAL_PRICE_CELL.index()).getNumericCellValue()).isEqualTo(39.0);
        assertThat(row.getCell(CATEGORY_CELL.index()).getStringCellValue()).isEqualTo(FOOD_AND_DRINK.categoryName);
        assertThat(row.getCell(SUB_CATEGORY_CELL.index()).getStringCellValue()).isEqualTo(LACTATE.categoryName);
        assertThat(row.getCell(SUB_SUB_CATEGORY_CELL.index()).getStringCellValue()).isEqualTo(SMINTINA.categoryName);
    }

    private void assertRow4(Row row) {
        assertThat(row.getCell(ORDER_DATE_CELL.index()).getStringCellValue()).isEqualTo("2021/04/01");
        assertThat(row.getCell(2).getNumericCellValue()).isEqualTo(4);
        assertThat(row.getCell(3).getNumericCellValue()).isEqualTo(1);
        assertThat(row.getCell(4).getNumericCellValue()).isEqualTo(2021);
        assertThat(row.getCell(PRODUCT_CELL.index()).getStringCellValue()).isEqualTo("Lamii");
        assertThat(row.getCell(QUANTITY_CELL.index()).getNumericCellValue()).isEqualTo(2.0);
        assertThat(row.getCell(PRICE_PER_UNIT_CELL.index()).getNumericCellValue()).isEqualTo(22.0);
        assertThat(row.getCell(TOTAL_PRICE_CELL.index()).getNumericCellValue()).isEqualTo(44.0);
        assertThat(row.getCell(STORE_CELL.index()).getStringCellValue()).isEqualTo(DEFAULT_STORE.getName());
        assertThat(row.getCell(SUB_CATEGORY_CELL.index()).getStringCellValue()).isEqualTo(FRUITS.categoryName);
        assertThat(row.getCell(CATEGORY_CELL.index()).getStringCellValue()).isEqualTo(FOOD_AND_DRINK.categoryName);
    }
}
