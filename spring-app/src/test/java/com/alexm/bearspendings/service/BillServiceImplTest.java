package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.repository.BillRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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

    private LocalDateTime orderDate = LocalDateTime.of(2019, 2, 12, 12, 33);

    @BeforeEach
    void setup() {}

    @Test
    void allBills() {
        final List<UIBill> actualBills = billService.allBills();
        assertNotNull(actualBills);
        assertEquals(2, actualBills.size());
        UIBill uiBill = actualBills.get(0);
        assertEquals(1L, uiBill.getId().longValue());
        assertEquals(1L, uiBill.getStoreId().longValue());
        assertEquals(LocalDateTime.of(2019, 04, 18, 0, 0 ), uiBill.getOrderDate());
        assertThat(uiBill.getItems()).extracting("id", "productId", "quantity", "price")
                .contains(
                        tuple(1L, 1L, 1, 7.85),
                        tuple(2L, 2L, 1, 9.85)
                );
    }

    @Transactional
    @Test
    void addBill() {
        UIBill newBill = UIBill.builder()
                .storeId(1L)
                .orderDate(orderDate)
                .items(
                        ImmutableSet.of(
                                UIBillItem.builder().quantity(2).productId(1L).price(22.9).build(),
                                UIBillItem.builder().quantity(1).productId(2L).price(44.0).build()
                        )
                )
                .build();
        Bill actualBill = billService.addBill(newBill);
        assertTrue(billRepository.existsById(actualBill.getId()));
    }

    @Test
    void allBillCounts() {
        assertEquals(2L, billService.allBillsCount().longValue());
    }

}