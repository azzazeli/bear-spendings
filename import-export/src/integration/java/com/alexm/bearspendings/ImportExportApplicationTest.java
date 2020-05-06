package com.alexm.bearspendings;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.imports.TestImportProducts;
import com.alexm.bearspendings.imports.excel.ExcelBillImporterTest;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void importsBills() throws FileNotFoundException, ImportsException { //NOSONAR
        importer.imports(ResourceUtils.getFile("classpath:import_sample.xlsm").toPath());
        assertEquals(3L, billService.allBillsCount().longValue());
        assertEquals(21L, productService.products().size());
        assertEquals(2L, storeService.allStores().size());

        verifyStores();
        verifyProducts();
        verifyBills();
    }

    private void verifyBills() {
        final Optional<Bill> byId1 = billService.findById(1L);
        assertTrue(byId1.isPresent());
        final Bill bill1 = byId1.orElseThrow();
        Assertions.assertThat(bill1)
                .hasFieldOrPropertyWithValue("orderDate", LocalDate.of(2020, 4, 10).atStartOfDay())
                .hasFieldOrPropertyWithValue("store.id", 1L);
        //todo: total as calculated field
        assertEquals(10, bill1.getItems().size());
        Assertions.assertThat(bill1.getItems()).extracting("product.id", "quantity", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestImportProducts.Medicamente.id, 1.0, 284.99),
                        Tuple.tuple(TestImportProducts.Calmante.id, 2.0, 10.72),
                        Tuple.tuple(TestImportProducts.Misc.id, 1.0, 8.12),
                        Tuple.tuple(TestImportProducts.Misc.id, 9.0, 8.27),
                        Tuple.tuple(TestImportProducts.Misc.id, 3.0, 19.6203),
                        Tuple.tuple(TestImportProducts.Pachet.id, 1.0, 0.3),
                        Tuple.tuple(TestImportProducts.Misc.id, 1.0, 59.9),
                        Tuple.tuple(TestImportProducts.Misc.id, 1.0, 71.88),
                        Tuple.tuple(TestImportProducts.Vitamine.id, 1.0, 36.62),
                        Tuple.tuple(TestImportProducts.Misc.id, 1.0, 172.8));
        final Optional<Bill> byId2 = billService.findById(2L);
        assertTrue(byId2.isPresent());
        final Bill bill2 = byId2.orElseThrow();
        Assertions.assertThat(bill2)
                .hasFieldOrPropertyWithValue("orderDate", LocalDate.of(2020, 4, 10).atStartOfDay())
                .hasFieldOrPropertyWithValue("store.id", 2L);
        assertEquals(10, bill2.getItems().size());
        Assertions.assertThat(bill2.getItems()).extracting("product.id", "quantity", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestImportProducts.AppaMorshinska.id, 2.0, 41.75),
                        Tuple.tuple(TestImportProducts.Ridiche.id, 0.47, 22.0),
                        Tuple.tuple(TestImportProducts.Lamii.id, 0.208, 58.0),
                        Tuple.tuple(TestImportProducts.Banane.id, 0.97, 33.0),
                        Tuple.tuple(TestImportProducts.Smintina20.id, 1.0, 23.0),
                        Tuple.tuple(TestImportProducts.ArahideFiesta.id, 1.0, 25.5),
                        Tuple.tuple(TestImportProducts.Chefir.id, 1.0, 11.5),
                        Tuple.tuple(TestImportProducts.Lapte05.id, 1.0, 7.25),
                        Tuple.tuple(TestImportProducts.VarzaNoua.id, 0.614, 23.0),
                        Tuple.tuple(TestImportProducts.Avocado.id, 0.198, 140.0)
                );

        final Optional<Bill> byId3 = billService.findById(3L);
        assertTrue(byId3.isPresent());
        final Bill bill3 = byId3.orElseThrow();
        Assertions.assertThat(bill3)
                .hasFieldOrPropertyWithValue("orderDate", LocalDate.of(2020, 4, 5).atStartOfDay())
                .hasFieldOrPropertyWithValue("store.id", 2L);
        Assertions.assertThat(bill3.getItems()).extracting("product.id", "quantity", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestImportProducts.Brinzica.id, 4.0, 5.75),
                        Tuple.tuple(TestImportProducts.Smintina20.id, 1.0, 23.0),
                        Tuple.tuple(TestImportProducts.Bere.id, 1.0, 32.0),
                        Tuple.tuple(TestImportProducts.Bere.id, 1.0, 24.9),
                        Tuple.tuple(TestImportProducts.Drojdie.id, 3.0, 7.1),
                        Tuple.tuple(TestImportProducts.Chefir.id, 1.0, 15.0),
                        Tuple.tuple(TestImportProducts.AppaMorshinska.id, 2.0, 41.75),
                        Tuple.tuple(TestImportProducts.Banane.id, 0.624, 33.00),
                        Tuple.tuple(TestImportProducts.Avocado.id, 0.202, 140.0),
                        Tuple.tuple(TestImportProducts.Ceapa.id, 1.384, 15.0),
                        Tuple.tuple(TestImportProducts.Grapefruit.id, 1.242, 26.0),
                        Tuple.tuple(TestImportProducts.PiineFranzela.id, 1.0, 5.0)
                );
    }

    private void verifyProducts() {
        Stream.of(TestImportProducts.values()).forEach(testProduct ->
                assertNotNull(productService.findStartWith(testProduct.productName))); //todo: use findByName
    }

    private void verifyStores() {
        Store store = storeService.findStore(1L);
        assertEquals(ExcelBillImporterTest.farmaciaFamiliei, store.getName());
        store = storeService.findStore(2L);
        assertEquals(ExcelBillImporterTest.alimarket, store.getName());
    }
}