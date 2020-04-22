package com.alexm.bearspendings.imports;

import java.nio.file.Path;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
public interface BillImporter {
    void imports(Path source) throws ImportsException;
}
