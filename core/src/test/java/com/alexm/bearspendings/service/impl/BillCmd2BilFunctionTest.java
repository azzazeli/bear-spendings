package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 9/10/19
 **/
@SpringBootTest
class BillCmd2BilFunctionTest {

    @Autowired
    BillCmd2BilFunction billCmd2BilFunction;

    @Transactional
    @DisplayName("transform UiBill that contains existing product to Bill")
    @Test
    void existingProduct() {
        Set<BillItemCommand> items = ImmutableSet.of(BillItemCommand.builder().productId(1L).quantity(1.0).pricePerUnit(2.2).build());
        BillCommand billCommand = BillCommand.builder().storeId(1L).items(items).build();
        Bill bill = billCmd2BilFunction.apply(billCommand);
        assertAll(() -> {
            assertEquals(1L, bill.getStore().getId().longValue());
            assertEquals(1L, bill.getItems().iterator().next().getProduct().getId().longValue());
        });
    }

    @DisplayName("transform UiBill that contains new product to Bill")
    @Test
    void newProduct() {
        String lapte = "Lapte";
        String piine = "Piine";
        Double total = 20.0;
        Set<BillItemCommand> items = ImmutableSet.of(
                BillItemCommand.builder().productName(lapte).quantity(1.1).pricePerUnit(2.2).build(),
                BillItemCommand.builder().productName(piine).quantity(2.3).pricePerUnit(12.1).build()
        );
        BillCommand billCommand = BillCommand.builder().storeId(1L).total(total).items(items).build();
        Bill bill = billCmd2BilFunction.apply(billCommand);
        for (BillItem billItem : bill.getItems()) {
            Assertions.assertTrue(ImmutableList.of(lapte, piine).contains(billItem.getProduct().getName()));
            assertThat(bill.getItems()).extracting("product.id").doesNotContainNull();
        }
        assertEquals(total, bill.getTotal());
    }

}
