package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.ProductService;
import com.alexm.bearspendings.service.StoreService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.alexm.bearspendings.imports.ExcelBillImporterTest.TEST_IMPORT_PRODUCTS.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 * Date: 4/22/20
 **/
@ExtendWith(MockitoExtension.class)
class ExcelBillImporterTest {

    private static final String farmaciaFamiliei = "Farmacia Familiei";
    private final String alimarket = "Alimarket";
    @Mock
    StoreService mockStoreService;
    @Mock
    BillService mockBillService;
    @Mock
    ProductService mockProductService;

    enum TEST_IMPORT_PRODUCTS {
        Medicamente(1000L, "Medicamente"),
        Calmante(1001L, "Calmante"),
        Misc(1002L, "Misc"),
        Pachet(1003L, "Pachet"),
        Vitamine(1004L, "Vitamine"),
        AppaMorshinska(1005L, "Appa Morshinska, 6L"),
        Ridiche(1006L, "Ridiche"),
        Lamii(1007L, "Lamii"),
        Banane(1008L, "Banane"),
        Smintina20(1009L, "Smintina 20%"),
        ArahideFiesta(1010L, "Arahide Fiesta, cu sare, 130g"),
        Chefir(1011L, "Chefir"),
        Lapte05(1012L, "Lapte, 0.5l"),
        VarzaNoua(1013L, "Varza noua"),
        Avocado(1014L, "Avocado"),
        Brinzica(1015L, "Brinzica"),
        Bere(1016L, "Bere"),
        Drojdie(1017L, "Drojdie uscata"),
        Grapefruit(1018L, "Grapefruit"),
        Ceapa(1019L, "Ceapa"),
        PiineFranzela(1020L, "Piine, franzela capitala");

        TEST_IMPORT_PRODUCTS(Long id, String name) {
            this.id = id;
            this.productName = name;
        }
        public final Long id;
        public final String productName;

    }

    @Captor
    ArgumentCaptor<Iterable<Bill>> iterableBillsCaptor;

    ExcelBillImporter importer;
    Map<String, Store> storesMap = new HashMap<>();

    @BeforeEach
    void setup() {
        importer = new ExcelBillImporter(mockStoreService, mockBillService, mockProductService);
        importer.setBillsBatchSize(2);
        storesMap.put(farmaciaFamiliei, Store.builder().id(100L).name(farmaciaFamiliei).build());
        storesMap.put(alimarket, Store.builder().id(200L).name(alimarket).build());
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
        when(mockProductService.getOrInsert(anyString())).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            return Stream.of(TEST_IMPORT_PRODUCTS.values())
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

        verifyProducts();

        verify(mockBillService, times(2)).saveAll(anyIterable());
        verify(mockBillService, times(2)).saveAll(iterableBillsCaptor.capture());
        final List<Iterable<Bill>> savedList = iterableBillsCaptor.getAllValues();
        savedList.forEach(bills -> bills.forEach(bill ->  assertTrue(storesMap.containsValue(bill.getStore()))));
        assertEquals(2, savedList.size());
        Iterable<Bill> firstBatch = savedList.get(0);
        Iterable<Bill> secondBatch = savedList.get(1);
        assertEquals(2 , firstBatch.spliterator().estimateSize());
        assertEquals(1 , secondBatch.spliterator().estimateSize());
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
