package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author AlexM
 * Date: 5/6/20
 **/
@DataJpaTest
public class BillRepositoryTest {
    @Autowired
    BillRepository billRepository;

    @Test
    void defaultTotal() {
        final Bill save = billRepository.save(sampleBill());
        assertThat(save.getTotal()).isNotNull();
        assertThat( billRepository.findById(1L).orElseThrow().getTotal()).isNotNull();
    }

    public Bill sampleBill() {
        final Bill template = billRepository.findById(1L).orElseThrow();
        return Bill.builder()
                .orderDate(LocalDateTime.now())
                .store(template.getStore())
                .items(Lists.list(BillItem.builder()
                        .quantity(1.0)
                        .price(2.0)
                        .product(template.getItems().iterator().next().getProduct())
                        .build()))
                .build();
    }
}
