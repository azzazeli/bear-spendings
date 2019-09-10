package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.alexm.bearspendings.entity.Bill;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AlexM
 * Date: 9/10/19
 **/
@SpringBootTest
class UiBill2BilFunctionTest {

    @Autowired
    UiBill2BilFunction uiBill2BilFunction;

    @DisplayName("transform UiBill that contains existing product to Bill")
    @Test
    void existingProduct() {
        Set<UIBillItem> items = ImmutableSet.of(UIBillItem.builder().productId(122L).quantity(1).price(2.2).build());
        UIBill uiBill = UIBill.builder().storeId(1L).items(items).build();
        Bill bill = uiBill2BilFunction.apply(uiBill);
        assertAll(() -> {
            assertEquals(1L, bill.getStore().getId().longValue());
            assertEquals(122L, bill.getItems().iterator().next().getProduct().getId().longValue());
        });
    }

    @DisplayName("transform UiBill that contains new product to Bill")
    @Test
    void newProduct() {
        String lapte = "Lapte";
        Set<UIBillItem> items = ImmutableSet.of(UIBillItem.builder().productName(lapte).quantity(1).price(2.2).build());
        UIBill uiBill = UIBill.builder().storeId(1L).items(items).build();
        Bill bill = uiBill2BilFunction.apply(uiBill);
        assertEquals(lapte, bill.getItems().iterator().next().getProduct().getName());
    }

}