package com.alexm.bearspendings;

import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 4/20/20
 **/
@SpringBootTest
class ImportExportApplicationTest {

    @Autowired
    BillImporter importer;

    @Autowired
    BillService billService;

    @Autowired
    ProductService productService;

    @Autowired
    StoreService storeService;

    @Disabled
    @Test
    void importsBills() throws FileNotFoundException, ImportsException { //NOSONAR
        importer.imports(ResourceUtils.getFile("classpath:import_sample.xlsm").toPath());
        assertEquals(3L, billService.allBillsCount().longValue());
        assertEquals(21L, productService.products().size());
        assertEquals(2L, storeService.allStores().size());
    }
}
