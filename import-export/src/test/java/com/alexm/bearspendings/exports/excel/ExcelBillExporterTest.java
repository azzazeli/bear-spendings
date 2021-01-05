package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.TestBills;
import com.alexm.bearspendings.service.BillService;
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
    @InjectMocks
    ExcelBillExporter excelBillExporter;

    @BeforeEach
    void setup() {
       when(billService.allBills(eq(0), eq(10))).thenReturn(TestBills.sampleBills());
    }

    @Test
    void exportAll() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_HHmmss");
        final String fileName = "build/export_" + LocalDateTime.now().format(formatter) + ".xlsm";
        File export = new File(fileName);
        FileCopyUtils.copy(excelBillExporter.exportAll(), new FileOutputStream(export));
        assertTrue(export.exists());

        //assertion on worksheet
    }
}
