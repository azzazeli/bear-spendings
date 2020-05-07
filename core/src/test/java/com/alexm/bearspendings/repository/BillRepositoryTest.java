package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author AlexM
 * Date: 5/6/20
 **/
@DataJpaTest
public class BillRepositoryTest {
    private final long FIRST_BILL_ID = 1L;
    private final long FIRST_PRODUCT_ID = 1L;
    @Autowired
    BillRepository billRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void defaultTotal() {
        final Bill save = billRepository.save(sampleBill());
        assertThat(save.getTotal()).isNotNull();
        assertThat( billRepository.findById(FIRST_BILL_ID).orElseThrow().getTotal()).isNotNull();
    }

    Bill sampleBill() {
        final Bill template = billRepository.findById(FIRST_BILL_ID).orElseThrow();
        return Bill.builder()
                .orderDate(LocalDateTime.now())
                .store(sampleStore())
                .items(Lists.list(BillItem.builder()
                        .quantity(1.0)
                        .price(2.0)
                        .product(template.getItems().iterator().next().getProduct())
                        .build()))
                .build();
    }

    Store sampleStore() {
        return billRepository.findById(FIRST_BILL_ID).orElseThrow().getStore();
    }

    Product sampleProduct() {
        return billRepository.findById(FIRST_BILL_ID).orElseThrow()
                .getItems().iterator().next().getProduct();

    }


    @Test
    void calculatedTotalOnSave() {
        final Bill bill = Bill.builder()
                .orderDate(LocalDateTime.now())
                .store(sampleStore())
                .items(Lists.list(
                        BillItem.builder().quantity(1.0).price(2.0).product(sampleProduct()).build(),
                        BillItem.builder().quantity(3.0).price(2.0).product(sampleProduct()).build()
                ))
                .build();
        final Bill save = billRepository.save(bill);
        assertThat(save.getTotal()).isEqualTo(8.0);
    }

    @Test
    void saveBillFromUIExistingProduct() {
        Bill fromUI = Bill.builder().store(sampleStore()).orderDate(LocalDateTime.now()).build();
        fromUI.addItem(BillItem.builder().price(1.0).quantity(2.0).product(productRepository.getOne(FIRST_PRODUCT_ID)).build());
        assertNotNull(billRepository.save(fromUI));
    }
}
