package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.exports.BillExporter;
import com.alexm.bearspendings.service.BillService;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.format.DateTimeFormatter;

/**
 * @author AlexM
 * Date: 12/10/20
 **/
@Component
public class ExcelBillExporter implements BillExporter {

    private final BillService billService;

    public ExcelBillExporter(BillService billService) {
        this.billService = billService;
    }

    @SneakyThrows
    @Override
    public InputStream exportAll() {
        final File template = ResourceUtils.getFile("classpath:export_template.xlsm");
        try (Workbook workbook = new XSSFWorkbook(template)) {
            Sheet sheet = workbook.getSheetAt(0);
            generateRows(sheet);
            return createResultStream(workbook);
        }
    }

    private ByteArrayInputStream createResultStream(Workbook workbook) throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        workbook.write(outStream);
        return new ByteArrayInputStream(outStream.toByteArray());
    }

    private void generateRows(Sheet sheet) {
        billService.allBills(0, 10).forEach(
                billCommand -> {
                    billCommand.getItems().forEach(billItemCommand -> {
                        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                        row.createCell(1).setCellValue(DateTimeFormatter.ofPattern("yyyy/dd/MM").format(billCommand.getOrderDate()));
                    });
                }
        );
    }
}
