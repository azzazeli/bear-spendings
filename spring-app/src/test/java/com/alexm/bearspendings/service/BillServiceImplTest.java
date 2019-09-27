package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.alexm.bearspendings.entity.Bill.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;
    private BillServiceImpl billService;
    private final Long allBillCount = 223L;

    private LocalDateTime orderDate = LocalDateTime.of(2019, 2, 12, 12, 33);

    @BeforeEach
    void setup() {
        billService = new BillServiceImpl(billRepository, uiBill -> {
            Bill bill = builder()
                    .store(Store.builder().id(1L).build())
                    .orderDate(orderDate)
                    .items(
                            ImmutableSet.of(
                                    BillItem.builder().quantity(2).product(Product.builder().id(1L).build()).price(22.9).build(),
                                    BillItem.builder().quantity(1).product(Product.builder().id(2L).build()).price(44.0).build()
                            )
                    )
                    .build();
            bill.getItems().forEach(billItem -> billItem.setBill(bill));
            return bill;
        });
        given(billRepository.count()).willReturn(allBillCount);
    }

    @Test
    void allBills() {
        LinkedList<Bill> billsFromRepo = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now();
        billsFromRepo.add(builder()
                .id(1234L)
                .store(Store.builder().id(334L).name("Pegas").build())
                .orderDate(now)
                .items(Sets.newSet((BillItem.builder().id(222L).price(11.2).quantity(1).product(Product.builder().id(1000L).build()).build())))
                .build());
        when(billRepository.findAll()).thenReturn(billsFromRepo);
        final List<UIBill> actualBills = billService.allBills();
        assertNotNull(actualBills);
        assertEquals(1, actualBills.size());
        UIBill uiBill = actualBills.get(0);
        assertEquals(1234L, uiBill.getId().longValue());
        assertEquals(334L, uiBill.getStoreId().longValue());
        assertEquals(now, uiBill.getOrderDate());
        assertThat(uiBill.getItems()).extracting("id", "productId", "quantity", "price")
                .contains(
                        tuple(222L, 1000L, 1, 11.2)
                );
    }

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
        Bill expectedBill = Bill.builder()
                .store(Store.builder().id(1L).build())
                .orderDate(orderDate)
                .items(
                        ImmutableSet.of(
                                BillItem.builder().quantity(2).product(Product.builder().id(1L).build()).price(22.9).build(),
                                BillItem.builder().quantity(1).product(Product.builder().id(2L).build()).price(44.0).build()
                        )
                )
                .build();
        verify(billRepository, times(1)).save(expectedBill);
    }

    @Test
    void allBillCounts() {
        assertEquals(allBillCount, billService.allBillsCount());
    }

}