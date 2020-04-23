package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@ExtendWith(MockitoExtension.class)
class ExcelBillImporterTest {

    @Mock
    StoreService mockStoreService;
    ExcelBillImporter importer;

    @BeforeEach
    void setup() {
        importer = new ExcelBillImporter(mockStoreService);
    }

    @Test
    void wrongPath() {
        final String wrongPath = "sss";
        Path source = Paths.get(wrongPath);
        ImportsException exception = assertThrows(ImportsException.class, () -> importer.imports(source));
        final String expected = "Invalid source for bills import. Path " + wrongPath + " does not exists";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void imports() throws FileNotFoundException, ImportsException {
        File file = ResourceUtils.getFile("classpath:import_sample.xlsm");
        importer.imports(file.toPath());
        verify(mockStoreService, times(10)).getOrInsert("Farmacia Familiei");
        verify(mockStoreService, times(22)).getOrInsert("Alimarket");
    }

    //todo: test excel file with wrong date

}
