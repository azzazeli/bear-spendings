package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.alexm.bearspendings.entity.Bill.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(MockitoExtension.class)
public class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    private BillServiceImpl billService;

    @BeforeEach
    public void setup() {
        billService = new BillServiceImpl(billRepository);
    }

    @Test
    public void allBills() {
        LinkedList<Bill> billsFromRepo = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now();
        billsFromRepo.add(builder()
                .id(1234L)
                .store(Store.builder().id(334L).name("Pegas").build())
                .orderDate(now)
                .items(Lists.list(BillItem.builder().id(222L).price(11.2).quantity(1).product(Product.builder().id(1000L).build()).build()))
                .build());
        when(billRepository.findAll()).thenReturn(billsFromRepo);
        final List<UIBill> actualBills = billService.allBills();
        assertNotNull(actualBills);
        assertEquals(1, actualBills.size());
        UIBill uiBill = actualBills.get(0);
        assertEquals(1234L, uiBill.getId().longValue());
        assertEquals(334L, uiBill.getStoreId().longValue());
        assertEquals(now, uiBill.getOrderDate());
        assertThat(uiBill.getItems()).extracting("id","productId","quantity", "price")
                .contains(
                        tuple(222L, 1000L, 1, 11.2)
                );
    }

}