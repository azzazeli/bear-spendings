package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.exports.BillExporter;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.StoreService;
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
    private final StoreService storeService;

    public ExcelBillExporter(BillService billService, StoreService storeService) {
        this.billService = billService;
        this.storeService = storeService;
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
                billCommand -> billCommand.getItems().forEach(billItemCommand -> createRow(sheet, billCommand, billItemCommand))
        );
    }

    private void createRow(Sheet sheet, BillCommand billCommand, BillItemCommand billItemCommand) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int column = 1;
        row.createCell(column++).setCellValue(DateTimeFormatter.ofPattern("yyyy/dd/MM").format(billCommand.getOrderDate()));
        row.createCell(column++).setCellValue(billCommand.getOrderDate().getDayOfMonth());
        row.createCell(column++).setCellValue(billCommand.getOrderDate().getMonthValue());
        row.createCell(column++).setCellValue(billCommand.getOrderDate().getYear());
        row.createCell(column++).setCellValue(billItemCommand.getProductName());
        column+=4; // skip product type, subcategory, category
        row.createCell(column++).setCellValue(billItemCommand.getQuantity());
        row.createCell(column++).setCellValue(billItemCommand.getPricePerUnit());
        row.createCell(column++).setCellValue(billItemCommand.getTotalPrice());
        column++; //skip Total, EUR column
        row.createCell(column).setCellValue(storeService.findStore(billCommand.getStoreId()).getName());
    }

}
