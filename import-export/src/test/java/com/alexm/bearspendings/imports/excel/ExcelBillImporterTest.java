package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.imports.ImportsConfig;
import com.alexm.bearspendings.imports.ImportsException;
import com.alexm.bearspendings.imports.TestImportProducts;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.CategoryService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
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

import static com.alexm.bearspendings.imports.TestImportProducts.*;
import static com.alexm.bearspendings.imports.excel.ExcelRowProcessor.DATE_PATTERN;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@ExtendWith(MockitoExtension.class)
public class ExcelBillImporterTest {
    public static final String farmaciaFamiliei = "Farmacia Familiei";
    public static final String alimarket = "Alimarket";
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
        ExcelRowProcessor rowProcessor = new ExcelRowProcessor(mockStoreService, mockProductService);
        importer = new ExcelBillImporter(mockBillService, rowProcessor, importsConfig);
        storesMap.put(farmaciaFamiliei, Store.builder().id(100L).name(farmaciaFamiliei).build());
        storesMap.put(alimarket, Store.builder().id(200L).name(alimarket).build());
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
        when(mockProductService.getOrInsert(anyString())).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            return Stream.of(TestImportProducts.values())
                    .filter(testProduct -> testProduct.productName.equals(name.toString()))
                    .map(testProduct -> Product.builder().id(testProduct.id).name(testProduct.productName).build())
                    .findFirst().orElseThrow();
        });
        when(mockStoreService.getOrInsert(anyString())).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            return storesMap.get(name.toString());
        });
        File file = ResourceUtils.getFile("classpath:import_sample.xlsm");

        importer.imports(file.toPath());

        verify(mockStoreService, times(10)).getOrInsert(farmaciaFamiliei);
        verify(mockStoreService, times(22)).getOrInsert(alimarket);
        verifyCategories();
        verifyProducts();
        verifyBills();
    }

    private void verifyCategories() {
//        verify(mockCategoryService, times(3)).getOrInsert(HEALTH.categoryName, BEBE.categoryName);
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
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(farmaciaFamiliei).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple(Medicamente.id, 1.0, 284.99),
                            Tuple.tuple(Calmante.id, 2.0, 10.72),
                            Tuple.tuple(Misc.id, 1.0, 8.12),
                            Tuple.tuple(Misc.id, 9.0, 8.27),
                            Tuple.tuple(Misc.id, 3.0, 19.6203),
                            Tuple.tuple(Pachet.id, 1.0, 0.3),
                            Tuple.tuple(Misc.id, 1.0, 59.9),
                            Tuple.tuple(Misc.id, 1.0, 71.88),
                            Tuple.tuple(Vitamine.id, 1.0, 36.62),
                            Tuple.tuple(Misc.id, 1.0, 172.8)
                    );

        bill = iterator.next();
        assertThat(bill)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/10"))
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(alimarket).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(AppaMorshinska.id, 2.0, 41.75),
                        Tuple.tuple(Ridiche.id, 0.47, 22.0),
                        Tuple.tuple(Lamii.id, 0.208, 58.0),
                        Tuple.tuple(Banane.id, 0.97, 33.0),
                        Tuple.tuple(Smintina20.id, 1.0, 23.0),
                        Tuple.tuple(ArahideFiesta.id, 1.0, 25.5),
                        Tuple.tuple(Chefir.id, 1.0, 11.5),
                        Tuple.tuple(Lapte05.id, 1.0, 7.25),
                        Tuple.tuple(VarzaNoua.id, 0.614, 23.0),
                        Tuple.tuple(Avocado.id, 0.198, 140.0)
                );

        assertEquals(1 , secondBatch.spliterator().estimateSize());
        bill = secondBatch.iterator().next();

        assertThat(bill)
                .hasFieldOrPropertyWithValue("orderDate", orderDate("20/4/05"))
                .hasFieldOrPropertyWithValue("store.id", storesMap.get(alimarket).getId());
        assertThat(bill.getItems()).extracting("product.id", "quantity", "pricePerUnit")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(Brinzica.id, 4.0, 5.75),
                        Tuple.tuple(Smintina20.id, 1.0, 23.0),
                        Tuple.tuple(Bere.id, 1.0, 32.0),
                        Tuple.tuple(Bere.id, 1.0, 24.9),
                        Tuple.tuple(Drojdie.id, 3.0, 7.1),
                        Tuple.tuple(Chefir.id, 1.0, 15.0),
                        Tuple.tuple(AppaMorshinska.id, 2.0, 41.75),
                        Tuple.tuple(Banane.id, 0.624, 33.00),
                        Tuple.tuple(Avocado.id, 0.202, 140.0),
                        Tuple.tuple(Ceapa.id, 1.384, 15.0),
                        Tuple.tuple(Grapefruit.id, 1.242, 26.0),
                        Tuple.tuple(PiineFranzela.id, 1.0, 5.0)
                );
    }

    private LocalDateTime orderDate(String dateValue) {
        return LocalDate.parse(dateValue, ofPattern(DATE_PATTERN)).atStartOfDay();
    }

    private void verifyProducts() {
        verify(mockProductService, times(1)).getOrInsert(Medicamente.productName);
        verify(mockProductService, times(1)).getOrInsert(Calmante.productName);
        verify(mockProductService, times(6)).getOrInsert(Misc.productName);
        verify(mockProductService, times(1)).getOrInsert(Pachet.productName);
        verify(mockProductService, times(1)).getOrInsert(Vitamine.productName);
        verify(mockProductService, times(2)).getOrInsert(AppaMorshinska.productName);
        verify(mockProductService, times(1)).getOrInsert(Ridiche.productName);
        verify(mockProductService, times(1)).getOrInsert(Lamii.productName);
        verify(mockProductService, times(2)).getOrInsert(Banane.productName);
        verify(mockProductService, times(2)).getOrInsert(Smintina20.productName);
        verify(mockProductService, times(1)).getOrInsert(ArahideFiesta.productName);
        verify(mockProductService, times(2)).getOrInsert(Chefir.productName);
        verify(mockProductService, times(1)).getOrInsert(Lapte05.productName);
        verify(mockProductService, times(1)).getOrInsert(VarzaNoua.productName);
        verify(mockProductService, times(2)).getOrInsert(Avocado.productName);
        verify(mockProductService, times(1)).getOrInsert(Brinzica.productName);
        verify(mockProductService, times(2)).getOrInsert(Bere.productName);
        verify(mockProductService, times(1)).getOrInsert(Drojdie.productName);
        verify(mockProductService, times(1)).getOrInsert(Grapefruit.productName);
        verify(mockProductService, times(1)).getOrInsert(Calmante.productName);
        verify(mockProductService, times(1)).getOrInsert(PiineFranzela.productName);
    }

    @Test
    void importsInvalidOrderDate() throws FileNotFoundException, ImportsException { //NOSONAR
        File file = ResourceUtils.getFile("classpath:import_invalid_date_format.xlsm");
        importer.imports(file.toPath());
        //just log the error
    }


}
