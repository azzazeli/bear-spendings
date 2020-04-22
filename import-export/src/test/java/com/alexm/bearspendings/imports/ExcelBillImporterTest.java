package com.alexm.bearspendings.imports;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
class ExcelBillImporterTest {

    ExcelBillImporter importer = new ExcelBillImporter();
    private ImportsException exception;

    @Test
    void wrongPath() {
        final String wrongPath = "sss";
        Path source = Paths.get(wrongPath);
        exception = assertThrows(ImportsException.class, () -> importer.imports(source));
        final String expected = "Invalid source for bills import. Path " + wrongPath + " does not exists";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void imports() throws FileNotFoundException, ImportsException {
        File file = ResourceUtils.getFile("classpath:import_sample.xlsm");
        importer.imports(file.toPath());
    }

}
