package com.alexm.bearspendings.imports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author AlexM
 * Date: 5/21/20
 **/
@ExtendWith(MockitoExtension.class)
class ImportsServiceImplTest {

    ImportsConfig importsConfig;
    @Mock
    BillImporter importer;
    ImportsServiceImpl importsService;
    private final String importPath = "build" + File.pathSeparator + "test-import";

    @BeforeEach
    void setup() {
        importsConfig = new ImportsConfig();
        importsConfig.setImportPath(importPath);
        importsService = new ImportsServiceImpl(Path.of(importPath), importer);
    }

    @Test
    void importsBills() {
        ArgumentCaptor<Path> importPathCaptor = ArgumentCaptor.forClass(Path.class);
        final String fileName = "test.xlsm";
        importsService.importsBills(fileName);
        verify(importer, times(1)).imports(importPathCaptor.capture());
        assertEquals(importPath + File.separator + fileName, importPathCaptor.getValue().toString());
    }
}
