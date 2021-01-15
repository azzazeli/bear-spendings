package com.alexm.bearspendings.imports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author AlexM
 * Date: 5/21/20
 **/
@Profile("import-export")
@Slf4j
@Service
public class ImportsServiceImpl implements ImportsService {
    private final BillImporter importer;
    private final Path importPath;

    public ImportsServiceImpl(Path importPath, BillImporter importer) {
        this.importPath = importPath;
        this.importer = importer;
    }

    @Override
    public void importsBills(String fileName) {
        try {
            importer.imports(resolveImportPath(fileName));
        } catch (ImportsException | IOException e) {
            log.error("Exception occurred during import of file with bills.", e);
        }
    }

    private Path resolveImportPath(String filename) throws IOException {
        return importPath.resolve(filename);
    }

}
