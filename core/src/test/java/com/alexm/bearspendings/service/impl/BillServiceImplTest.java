package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillRepository;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.alexm.bearspendings.entity.ProductTest.FIRST_PRODUCT_ID;
import static com.alexm.bearspendings.entity.StoreTest.FIRST_STORE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexM created on 7/11/19
 */
@SpringBootTest
class BillServiceImplTest {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillServiceImpl billService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreRepository storeRepository;

    private final LocalDateTime orderDate = LocalDateTime.of(2019, 2, 12, 12, 33);

    @Test
    void allBills() {
        final List<BillCommand> actualBills = billService.allBills(0, 10);
        assertNotNull(actualBills);
        assertEquals(3, actualBills.size());
        BillCommand billCommand = actualBills.get(1);
        assertEquals(2L, billCommand.getId().longValue());
        assertEquals(1L, billCommand.getStoreId().longValue());
        assertEquals(Double.valueOf(97.00), billCommand.getTotal());
        assertEquals(LocalDateTime.of(2019, 4, 22, 0, 0 ), billCommand.getOrderDate());
        assertThat(billCommand.getItems()).extracting("id", "pricePerUnit", "totalPrice", "quantity", "productId" )
                .contains(
                        tuple(3L, 87.00, 87.00, 1.0, 3L),
                        tuple(4L, 10.00, 10.00, 1.0, 2L)
                );
    }

    @Transactional
    @Test
    void addBill() {
        BillCommand newBill = BillCommand.builder()
                .storeId(1L)
                .orderDate(orderDate)
                .items(ImmutableSet.of(
                        BillItemCommand.builder().id(1L).quantity(2.0).productId(1L).pricePerUnit(22.9).totalPrice(22.9).build(),
                        BillItemCommand.builder().id(2L).quantity(1.0).productId(2L).pricePerUnit(44.0).totalPrice(44.0).build()
                        ))
                .build();
        Bill actualBill = billService.addBill(newBill);
        assertTrue(billRepository.existsById(actualBill.getId()));
    }

    @Test
    void allBillCounts() {
        assertEquals(3L, billService.allBillsCount().longValue());
    }

    @DisplayName("Bill toString to not generate stack overflow")
    @Test
    void billHashCode() {
        Bill bill = billRepository.findById(1L).orElseThrow(() -> new RuntimeException("Bill with id = 1 must be present in test dataset"));
        assertNotNull(bill.toString());
    }

    @Test
    void saveAll() {
        final long initialCount = billRepository.count();
        final List<Bill> bills = List.of(billToSave(), billToSave(), billToSave());
        final Iterable<Bill> saved = billService.saveAll(bills);
        assertEquals(initialCount + bills.size(), billRepository.count());
        assertThat(saved).extracting("id").doesNotContainNull();
    }

    private Bill billToSave() {
        final Bill previousBill = billRepository.findById(1L).orElseThrow();
        final Product sampleProduct = productRepository.findAll().get(0);
        return Bill.builder()
                .store(previousBill.getStore())
                .items(List.of(
                        BillItem.builder().pricePerUnit(12.0).quantity(2.0).product(sampleProduct).build())
                )
                .orderDate(LocalDateTime.now())
                .build();
    }

    @Test
    void findById() {
        final Optional<Bill> byId = billService.findById(1L);
        assertTrue(byId.isPresent());
        assertThat(byId.orElseThrow())
                .hasFieldOrPropertyWithValue("orderDate", LocalDate.of(2019, 4, 18).atStartOfDay())
                .hasFieldOrPropertyWithValue("store.id", 1L)
                .hasFieldOrPropertyWithValue("total", 21.0);
    }


    @Transactional
    @Test
    void saveBillExistingProduct() {
        final BillItem billItem = BillItem.builder()
                .pricePerUnit(1.0).quantity(2.0)
                .product(productRepository.findById(FIRST_PRODUCT_ID).orElseThrow())
                .build();
        Bill fromUI = Bill.builder()
                .store(sampleStore()).orderDate(LocalDateTime.now()).items(Lists.list(billItem))
                .build();
        assertNotNull(billRepository.save(fromUI));
    }

    private Store sampleStore() {
        return storeRepository.getOne(FIRST_STORE_ID);
    }
}
