package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.TestBills;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.StoreService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
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
    private final Store defaultStore = TestBills.defaultStore();

    @BeforeEach
    void setup() {
        when(billService.allBills(eq(0), eq(10))).thenReturn(TestBills.sampleBills());
        when(storeService.findStore(defaultStore.getId())).thenReturn(defaultStore);
    }

    @Test
    void exportAll() throws IOException, InvalidFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_HHmmss");
        final String fileName = "build/export_" + LocalDateTime.now().format(formatter) + ".xlsm";
        File export = new File(fileName);
        FileCopyUtils.copy(excelBillExporter.exportAll(), new FileOutputStream(export));
        assertTrue(export.exists());
        try (Workbook workbook = new XSSFWorkbook(export)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(5, sheet.getLastRowNum());
            final Row row = sheet.getRow(3);
            assertThat(row.getCell(1).getStringCellValue()).isEqualTo("2021/04/01");
            assertThat(row.getCell(2).getNumericCellValue()).isEqualTo(4);
            assertThat(row.getCell(3).getNumericCellValue()).isEqualTo(1);
            assertThat(row.getCell(4).getNumericCellValue()).isEqualTo(2021);
            assertThat(row.getCell(5).getStringCellValue()).isEqualTo("HEIDI");
            assertThat(row.getCell(10).getNumericCellValue()).isEqualTo(2.0);
            assertThat(row.getCell(11).getNumericCellValue()).isEqualTo(22.0);
            assertThat(row.getCell(12).getNumericCellValue()).isEqualTo(44.0);
            assertThat(row.getCell(14).getStringCellValue()).isEqualTo(defaultStore.getName());
        }
    }
}
