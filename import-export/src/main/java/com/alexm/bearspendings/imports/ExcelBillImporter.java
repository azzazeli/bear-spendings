package com.alexm.bearspendings.imports;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@Slf4j
@Component
public class ExcelBillImporter implements BillImporter {
    private static final String SPACE = " ";

    @Override
    public void imports(Path source) throws ImportsException {
        if(!Files.exists(source)) {
            throw new ImportsException(String.format("Invalid source for bills import. Path %1$s does not exists", source.toString()));
        }
        try(Workbook workbook = new XSSFWorkbook(source.toFile())) {
            Sheet sheet = workbook.getSheetAt(0);
            log.debug("Sheet contains {} rows.", sheet.getLastRowNum());
            for (Row row : sheet) {
                processRow(row);
            }
        } catch (IOException |  InvalidFormatException e) {
            throw new ImportsException("Failed to load XSSFWorkbook." , e);
        }
    }

    private void processRow(Row row) {
        if (0 == row.getRowNum()) {
            // skip header
            return;
        }
        logRowInDebug(row);
    }

    private void logRowInDebug(Row row) {
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
}
