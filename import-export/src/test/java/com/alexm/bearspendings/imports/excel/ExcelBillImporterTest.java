package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.imports.ImportsConfig;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.CategoryService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
import com.alexm.bearspendings.test.TestCategories;
import com.alexm.bearspendings.test.TestProducts;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.DATE_PATTERN;
import static com.alexm.bearspendings.test.TestCategories.MISC;
import static com.alexm.bearspendings.test.TestCategories.*;
import static com.alexm.bearspendings.test.TestProducts.*;
import static com.alexm.bearspendings.test.TestStores.ALIMARKET;
import static com.alexm.bearspendings.test.TestStores.FARMACIA_FAMILIEI;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@ExtendWith(MockitoExtension.class)
class ExcelBillImporterTest {
    @Mock
    StoreService mockStoreService;
    @Mock
    BillService mockBillService;
    @Mock
    ProductService mockProductService;
    @Mock
    private CategoryService mockCategoryService;

    @Captor
    ArgumentCaptor<Iterable<Bill>> iterableBillsCaptor;

    ExcelBillImporter importer;
    Map<String, Store> storesMap = new HashMap<>();

    @BeforeEach
    void setup() {
        ImportsConfig importsConfig = new ImportsConfig();
        importsConfig.setBillsBatchSize(2);
        ExcelRowProcessor rowProcessor = new ExcelRowProcessor(mockStoreService, mockProductService, mockCategoryService);
        importer = new ExcelBillImporter(mockBillService, rowProcessor, importsConfig);
        storesMap.put(FARMACIA_FAMILIEI.storeName, Store.builder().id(100L).name(FARMACIA_FAMILIEI.storeName).build());
        storesMap.put(ALIMARKET.storeName, Store.builder().id(200L).name(ALIMARKET.storeName).build());
    }

    @Test
    void wrongPath() {
        final String wrongPath = "sss";
        Path source = Paths.get(wrongPath);
        ImportsException exception = assertThrows(ImportsException.class, () -> importer.imports(source));
        final String expected = "Failed to load XSSFWorkbook.";
        assertThat(exception.getMessage()).startsWith(expected);
    }

    @Test
    void imports() throws FileNotFoundException, ImportsException {
        when(mockCategoryService.getOrInsert(any())).thenAnswer(invocation ->
                TestCategories.of(invocation.getArguments().length > 2 ? invocation.getArgument(2) : invocation.getArgument(1)));

        when(mockProductService.getOrInsert(anyString(), any(Category.class))).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            final Category category = invocation.getArgument(1);
            return Stream.of(TestProducts.values())
                    .filter(testProduct -> testProduct.productName.equals(name.toString()))
                    .map(testProduct -> Product.builder().id(testProduct.id).name(testProduct.productName).category(category).build())
                    .findFirst().orElseThrow();
        });
        when(mockStoreService.getOrInsert(anyString())).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            return storesMap.get(name.toString());
        });
        File file = ResourceUtils.getFile("classpath:import_sample.xlsm");

        importer.imports(file.toPath());

        verify(mockStoreService, times(10)).getOrInsert(FARMACIA_FAMILIEI.storeName);
        verify(mockStoreService, times(22)).getOrInsert(ALIMARKET.storeName);
        verifyCategories();
        verifyProducts();
        verifyBills();
    }

    private void verifyCategories() {
        verify(mockCategoryService, times(3)).getOrInsert(HEALTH.categoryName, BEBE.categoryName);
        verify(mockCategoryService, times(6)).getOrInsert(HEALTH.categoryName, MISC.categoryName);
        verify(mockCategoryService, times(1)).getOrInsert(HOUSEHOLD.categoryName, CONSUMABLE.categoryName);
        verify(mockCategoryService, times(4)).getOrInsert(FOOD_AND_DRINK.categoryName, FRUITS.categoryName);
        verify(mockCategoryService, times(5)).getOrInsert(FOOD_AND_DRINK.categoryName, LEGUME.categoryName);
        verify(mockCategoryService, times(2)).getOrInsert(FOOD_AND_DRINK.categoryName, LACTATE.categoryName, "Smintina");
        verify(mockCategoryService, times(2)).getOrInsert(FOOD_AND_DRINK.categoryName, LACTATE.categoryName, "Iaurt / Chefir");
        verify(mockCategoryService, times(1)).getOrInsert(FOOD_AND_DRINK.categoryName, LACTATE.categoryName, "Lapte");
        verify(mockCategoryService, times(1)).getOrInsert(FOOD_AND_DRINK.categoryName, SNACKS.categoryName);
        verify(mockCategoryService, times(1)).getOrInsert(FOOD_AND_DRINK.categoryName, DESERT.categoryName, MISC.categoryName);
        verify(mockCategoryService, times(2)).getOrInsert(FOOD_AND_DRINK.categoryName, ALCOHOL.categoryName);
        verify(mockCategoryService, times(1)).getOrInsert(FOOD_AND_DRINK.categoryName, MISC2.categoryName);
        verify(mockCategoryService, times(1)).getOrInsert(FOOD_AND_DRINK.categoryName, PIINE.categoryName);
    }

    private void verifyBills() {
        verify(mockBillService, times(2)).saveAll(anyIterable());
        verify(mockBillService, times(2)).saveAll(iterableBillsCaptor.capture());
        final List<Iterable<Bill>> savedList = iterableBillsCaptor.getAllValues();
        savedList.forEach(bills -> bills.forEach(bill ->  assertTrue(storesMap.containsValue(bill.getStore()))));
        assertEquals(2, savedList.size());
        Iterable<Bill> firstBatch = savedList.get(0);
        Iterable<Bill> secondBatch = savedList.get(1);
        assertEquals(2 , firstBatch.spliterator().estimateSize());

        final Iterator<Bill> iterator = firstBatch.iterator();
        Bill bill = iterator.next();
        assertThat(bill)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/10"))
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(FARMACIA_FAMILIEI.storeName).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple(MEDICAMENTE.id, 1.0, 284.99),
                            Tuple.tuple(CALMANTE.id, 2.0, 10.72),
                            Tuple.tuple(TestProducts.MISC.id, 1.0, 8.12),
                            Tuple.tuple(TestProducts.MISC.id, 9.0, 8.27),
                            Tuple.tuple(TestProducts.MISC.id, 3.0, 19.6203),
                            Tuple.tuple(PACHET.id, 1.0, 0.3),
                            Tuple.tuple(TestProducts.MISC.id, 1.0, 59.9),
                            Tuple.tuple(TestProducts.MISC.id, 1.0, 71.88),
                            Tuple.tuple(VITAMINE.id, 1.0, 36.62),
                            Tuple.tuple(TestProducts.MISC.id, 1.0, 172.8)
                    );

        bill = iterator.next();
        assertThat(bill)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/10"))
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(ALIMARKET.storeName).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(APPA_MORSHINSKA.id, 2.0, 41.75),
                        Tuple.tuple(RIDICHE.id, 0.47, 22.0),
                        Tuple.tuple(LAMII.id, 0.208, 58.0),
                        Tuple.tuple(BANANE.id, 0.97, 33.0),
                        Tuple.tuple(SMINTINA_20.id, 1.0, 23.0),
                        Tuple.tuple(ARAHIDE_FIESTA.id, 1.0, 25.5),
                        Tuple.tuple(CHEFIR.id, 1.0, 11.5),
                        Tuple.tuple(LAPTE_05.id, 1.0, 7.25),
                        Tuple.tuple(VARZA_NOUA.id, 0.614, 23.0),
                        Tuple.tuple(AVOCADO.id, 0.198, 140.0)
                );

        assertEquals(1 , secondBatch.spliterator().estimateSize());
        bill = secondBatch.iterator().next();

        assertThat(bill)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/05"))
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(ALIMARKET.storeName).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(BRINZICA.id, 4.0, 5.75),
                        Tuple.tuple(SMINTINA_20.id, 1.0, 23.0),
                        Tuple.tuple(BERE.id, 1.0, 32.0),
                        Tuple.tuple(BERE.id, 1.0, 24.9),
                        Tuple.tuple(DROJDIE.id, 3.0, 7.1),
                        Tuple.tuple(CHEFIR.id, 1.0, 15.0),
                        Tuple.tuple(APPA_MORSHINSKA.id, 2.0, 41.75),
                        Tuple.tuple(BANANE.id, 0.624, 33.00),
                        Tuple.tuple(AVOCADO.id, 0.202, 140.0),
                        Tuple.tuple(CEAPA.id, 1.384, 15.0),
                        Tuple.tuple(GRAPEFRUIT.id, 1.242, 26.0),
                        Tuple.tuple(PIINE_FRANZELA.id, 1.0, 5.0)
                );
    }

    private LocalDateTime orderDate(String dateValue) {
        return LocalDate.parse(dateValue, ofPattern(DATE_PATTERN)).atStartOfDay();
    }

    private void verifyProducts() {
        verify(mockProductService, times(1)).getOrInsert(MEDICAMENTE.productName, BEBE.category());
        verify(mockProductService, times(1)).getOrInsert(CALMANTE.productName, MISC.category());
        verify(mockProductService, times(5)).getOrInsert(TestProducts.MISC.productName, MISC.category());
        verify(mockProductService, times(1)).getOrInsert(TestProducts.MISC.productName, BEBE.category());
        verify(mockProductService, times(1)).getOrInsert(PACHET.productName, CONSUMABLE.category());
        verify(mockProductService, times(1)).getOrInsert(VITAMINE.productName, BEBE.category());
        verify(mockProductService, times(2)).getOrInsert(APPA_MORSHINSKA.productName, APPA.category());
        verify(mockProductService, times(1)).getOrInsert(RIDICHE.productName, LEGUME.category());
        verify(mockProductService, times(1)).getOrInsert(LAMII.productName, FRUITS.category());
        verify(mockProductService, times(2)).getOrInsert(BANANE.productName, FRUITS.category());
        verify(mockProductService, times(2)).getOrInsert(SMINTINA_20.productName, SMINTINA.category());
        verify(mockProductService, times(1)).getOrInsert(ARAHIDE_FIESTA.productName, SNACKS.category());
        verify(mockProductService, times(2)).getOrInsert(CHEFIR.productName, IAURT_CHEFIR.category());
        verify(mockProductService, times(1)).getOrInsert(LAPTE_05.productName, LAPTE.category());
        verify(mockProductService, times(1)).getOrInsert(VARZA_NOUA.productName, LEGUME.category());
        verify(mockProductService, times(2)).getOrInsert(AVOCADO.productName, LEGUME.category());
        verify(mockProductService, times(1)).getOrInsert(BRINZICA.productName, MISC.category());
        verify(mockProductService, times(2)).getOrInsert(BERE.productName, ALCOHOL.category());
        verify(mockProductService, times(1)).getOrInsert(DROJDIE.productName, MISC2.category());
        verify(mockProductService, times(1)).getOrInsert(GRAPEFRUIT.productName, FRUITS.category());
        verify(mockProductService, times(1)).getOrInsert(PIINE_FRANZELA.productName, PIINE.category());
    }

    @Test
    void importsInvalidOrderDate() throws FileNotFoundException, ImportsException { //NOSONAR
        File file = ResourceUtils.getFile("classpath:import_invalid_date_format.xlsm");
        importer.imports(file.toPath());
        //just log the error
    }


}
