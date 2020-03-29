package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.repository.BillRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        final List<BillCommand> actualBills = billService.allBills(0, 10);
        assertNotNull(actualBills);
        assertEquals(2, actualBills.size());
        BillCommand billCommand = actualBills.get(0);
        assertEquals(2L, billCommand.getId().longValue());
        assertEquals(1L, billCommand.getStoreId().longValue());
        assertEquals(Double.valueOf(97.00), billCommand.getTotal());
        assertEquals(LocalDateTime.of(2019, 4, 22, 0, 0 ), billCommand.getOrderDate());
        assertThat(billCommand.getItems()).extracting("id", "price", "quantity", "productId" )
                .contains(
                        tuple(3L, 87.00, 1.0, 3L),
                        tuple(4L, 10.00, 1.0, 2L)
                );
    }

    @Transactional
    @Test
    void addBill() {
        BillCommand newBill = BillCommand.builder()
                .storeId(1L)
                .orderDate(orderDate)
                .items(
                        ImmutableSet.of(
                                BillItemCommand.builder().id(1L).quantity(2.0).productId(1L).price(22.9).build(),
                                BillItemCommand.builder().id(2L).quantity(1.0).productId(2L).price(44.0).build()
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

    @DisplayName("Bill toString to not generate stack overflow")
    @Test
    void billHashCode() {
        Bill bill = billRepository.findById(1L).orElseThrow(() -> new RuntimeException("Bill with id = 1 must be present in test dataset"));
        assertNotNull(bill.toString());
    }

}