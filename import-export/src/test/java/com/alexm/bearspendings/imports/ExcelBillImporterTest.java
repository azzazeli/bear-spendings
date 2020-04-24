package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.BillService;
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

    @Captor
    ArgumentCaptor<Iterable<Bill>> iterableBillsCaptor;

    ExcelBillImporter importer;
    Map<String, Store> storesMap = new HashMap<>();

    @BeforeEach
    void setup() {
        importer = new ExcelBillImporter(mockStoreService, mockBillService);
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
        when(mockStoreService.getOrInsert(anyString())).thenAnswer(invocation -> {
            final Object name = invocation.getArgument(0);
            return storesMap.get(name.toString());
        });

        File file = ResourceUtils.getFile("classpath:import_sample.xlsm");

        importer.imports(file.toPath());
        verify(mockStoreService, times(10)).getOrInsert(farmaciaFamiliei);
        verify(mockStoreService, times(22)).getOrInsert(alimarket);
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

    @Test
    void importsInvalidOrderDate() throws FileNotFoundException, ImportsException {
        File file = ResourceUtils.getFile("classpath:import_invalid_date_format.xlsm");
        importer.imports(file.toPath());
        //just log the error
    }


}
