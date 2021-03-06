package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author AlexM
 * Date: 5/6/20
 **/
@DataJpaTest
class BillRepositoryTest {
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
                        .pricePerUnit(2.0)
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
                        BillItem.builder().quantity(1.1).pricePerUnit(2.0).product(sampleProduct()).build(),
                        BillItem.builder().quantity(3.2).pricePerUnit(2.0).product(sampleProduct()).build()
                ))
                .build();
        final Bill save = billRepository.save(bill);
        assertThat(save.getTotal()).isEqualTo(8.60);
    }

    @Test
    void saveBillFromUIExistingProduct() {
        Bill fromUI = Bill.builder().store(sampleStore()).orderDate(LocalDateTime.now()).build();
        fromUI.addItem(BillItem.builder().pricePerUnit(1.0).quantity(2.0).product(productRepository.getOne(FIRST_PRODUCT_ID)).build());
        assertNotNull(billRepository.save(fromUI));
    }

    @Test
    void avoidNPlusOneQueriesForItems() {
        assertThat(billRepository.findAll(PageRequest.of(0, 10))).isNotNull();
    }


    @Test
    void allGraph() {
        Page<Bill> page = billRepository.allGraph(PageRequest.of(0, 10));
        assertNotNull(page);
    }
}
