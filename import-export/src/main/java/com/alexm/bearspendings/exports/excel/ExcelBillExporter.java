package com.alexm.bearspendings.exports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
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
        //todo: bug it export only 10 rows but need to export all of them
        billService.bills(0, 10).forEach(
                bill -> bill.getItems().forEach(billItem -> createRow(sheet, bill, billItem))
        );
    }

    private void createRow(Sheet sheet, Bill bill, BillItem billItem) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int column = 1;
        row.createCell(column++).setCellValue(DateTimeFormatter.ofPattern("yyyy/dd/MM").format(bill.getOrderDate()));
        row.createCell(column++).setCellValue(bill.getOrderDate().getDayOfMonth());
        row.createCell(column++).setCellValue(bill.getOrderDate().getMonthValue());
        row.createCell(column++).setCellValue(bill.getOrderDate().getYear());
        row.createCell(column++).setCellValue(billItem.getProduct().getName());
        column = fillCategories(billItem, row, column);
        column+=1; // skip price local currency
        row.createCell(column++).setCellValue(billItem.getQuantity());
        row.createCell(column++).setCellValue(billItem.getPricePerUnit());
        row.createCell(column++).setCellValue(billItem.getTotalPrice());
        column++; //skip Total, EUR column
        row.createCell(column).setCellValue(bill.getStore().getName());
    }

    private int fillCategories(BillItem billItem, Row row, int column) {
        if (hasProductType(billItem.getProduct())) {
            row.createCell(column++).setCellValue(billItem.getProduct().getCategory().getName());
            row.createCell(column++).setCellValue(billItem.getProduct().getCategory().getParent().getName());
            row.createCell(column++).setCellValue(billItem.getProduct().getCategory().getParent().getParent().getName());
        }
        else {
            column++;
            row.createCell(column++).setCellValue(billItem.getProduct().getCategory().getName());
            row.createCell(column++).setCellValue(billItem.getProduct().getCategory().getParent().getName());
        }
        return column;
    }

    private boolean hasProductType(Product product) {
        return product.getCategory().getParent().getParent() != null;
    }

}
