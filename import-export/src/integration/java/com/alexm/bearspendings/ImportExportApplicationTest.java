package com.alexm.bearspendings;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.imports.BillImporter;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import com.alexm.bearspendings.test.TestProducts;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.alexm.bearspendings.test.TestStores.ALIMARKET;
import static com.alexm.bearspendings.test.TestStores.FARMACIA_FAMILIEI;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexM
 * Date: 4/20/20
 **/
@SuppressWarnings("squid:S5960")
@ActiveProfiles("import-export")
@SpringBootTest(classes = ImportExportApplication.class)
class ImportExportApplicationTest {

    private static final String ORDER_DATE_FIELD = "orderDate";
    private static final String STORE_ID_FIELD = "store.id";
    private static final String TOTAL_FIELD = "total";
    private static final String PRODUCT_ID_FIELD = "product.id";
    private static final String QUANTITY_FIELD = "quantity";
    private static final String PRICE_PER_UNIT_FIELD = "pricePerUnit";

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
                .hasFieldOrPropertyWithValue(ORDER_DATE_FIELD, LocalDate.of(2020, 4, 10).atStartOfDay())
                .hasFieldOrPropertyWithValue(STORE_ID_FIELD, 1L)
                .hasFieldOrPropertyWithValue(TOTAL_FIELD, 789.34);

        assertEquals(10, bill1.getItems().size());
        Assertions.assertThat(bill1.getItems()).extracting(PRODUCT_ID_FIELD, QUANTITY_FIELD, PRICE_PER_UNIT_FIELD)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestProducts.MEDICAMENTE.id, 1.0, 284.99),
                        Tuple.tuple(TestProducts.CALMANTE.id, 2.0, 10.72),
                        Tuple.tuple(TestProducts.MISC.id, 1.0, 8.12),
                        Tuple.tuple(TestProducts.MISC.id, 9.0, 8.27),
                        Tuple.tuple(TestProducts.MISC.id, 3.0, 19.6203),
                        Tuple.tuple(TestProducts.PACHET.id, 1.0, 0.3),
                        Tuple.tuple(TestProducts.MISC.id, 1.0, 59.9),
                        Tuple.tuple(TestProducts.MISC.id, 1.0, 71.88),
                        Tuple.tuple(TestProducts.VITAMINE.id, 1.0, 36.62),
                        Tuple.tuple(TestProducts.MISC.id, 1.0, 172.8));
        final Optional<Bill> byId2 = billService.findById(2L);
        assertTrue(byId2.isPresent());
        final Bill bill2 = byId2.orElseThrow();
        Assertions.assertThat(bill2)
                .hasFieldOrPropertyWithValue(ORDER_DATE_FIELD, LocalDate.of(2020, 4, 10).atStartOfDay())
                .hasFieldOrPropertyWithValue(STORE_ID_FIELD, 2L)
                .hasFieldOrPropertyWithValue(TOTAL_FIELD, 247.00);
        assertEquals(10, bill2.getItems().size());
        Assertions.assertThat(bill2.getItems()).extracting(PRODUCT_ID_FIELD, QUANTITY_FIELD, PRICE_PER_UNIT_FIELD)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestProducts.APPA_MORSHINSKA.id, 2.0, 41.75),
                        Tuple.tuple(TestProducts.RIDICHE.id, 0.47, 22.0),
                        Tuple.tuple(TestProducts.LAMII.id, 0.208, 58.0),
                        Tuple.tuple(TestProducts.BANANE.id, 0.97, 33.0),
                        Tuple.tuple(TestProducts.SMINTINA_20.id, 1.0, 23.0),
                        Tuple.tuple(TestProducts.ARAHIDE_FIESTA.id, 1.0, 25.5),
                        Tuple.tuple(TestProducts.CHEFIR.id, 1.0, 11.5),
                        Tuple.tuple(TestProducts.LAPTE_05.id, 1.0, 7.25),
                        Tuple.tuple(TestProducts.VARZA_NOUA.id, 0.614, 23.0),
                        Tuple.tuple(TestProducts.AVOCADO.id, 0.198, 140.0)
                );

        final Optional<Bill> byId3 = billService.findById(3L);
        assertTrue(byId3.isPresent());
        final Bill bill3 = byId3.orElseThrow();
        Assertions.assertThat(bill3)
                .hasFieldOrPropertyWithValue(ORDER_DATE_FIELD, LocalDate.of(2020, 4, 5).atStartOfDay())
                .hasFieldOrPropertyWithValue(STORE_ID_FIELD, 2L)
                .hasFieldOrPropertyWithValue(TOTAL_FIELD, 329.62);
        Assertions.assertThat(bill3.getItems()).extracting(PRODUCT_ID_FIELD, QUANTITY_FIELD, PRICE_PER_UNIT_FIELD)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(TestProducts.BRINZICA.id, 4.0, 5.75),
                        Tuple.tuple(TestProducts.SMINTINA_20.id, 1.0, 23.0),
                        Tuple.tuple(TestProducts.BERE.id, 1.0, 32.0),
                        Tuple.tuple(TestProducts.BERE.id, 1.0, 24.9),
                        Tuple.tuple(TestProducts.DROJDIE.id, 3.0, 7.1),
                        Tuple.tuple(TestProducts.CHEFIR.id, 1.0, 15.0),
                        Tuple.tuple(TestProducts.APPA_MORSHINSKA.id, 2.0, 41.75),
                        Tuple.tuple(TestProducts.BANANE.id, 0.624, 33.00),
                        Tuple.tuple(TestProducts.AVOCADO.id, 0.202, 140.0),
                        Tuple.tuple(TestProducts.CEAPA.id, 1.384, 15.0),
                        Tuple.tuple(TestProducts.GRAPEFRUIT.id, 1.242, 26.0),
                        Tuple.tuple(TestProducts.PIINE_FRANZELA.id, 1.0, 5.0)
                );
    }

    private void verifyProducts() {
        Stream.of(TestProducts.values()).forEach(testProduct ->
                assertNotNull(productService.findStartWith(testProduct.productName))); //todo: use findByName
    }

    private void verifyStores() {
        Store store = storeService.findStore(1L);
        assertEquals(FARMACIA_FAMILIEI.storeName, store.getName());
        store = storeService.findStore(2L);
        assertEquals(ALIMARKET.storeName, store.getName());
    }
}
