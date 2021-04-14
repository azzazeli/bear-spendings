package com.alexm.bearspendings.test;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.google.common.collect.ImmutableSet;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.alexm.bearspendings.entity.Defaults.DEFAULT_STORE;

/**
 * @author AlexM
 * Date: 1/5/21
 **/
public class TestBills {

    private TestBills() {
    }

    public static List<Bill> sampleBills() {
        List<Bill> bills = new LinkedList<>();
        bills.add(bill1());
        bills.add(bill2());
        return bills;
    }

    private static Bill bill1() {
        return Bill.builder()
                .orderDate(LocalDateTime.of(2021, 1, 4, 14, 10))
                .store(DEFAULT_STORE)
                .items(ImmutableSet.of(billItem1(),billItem2())).build();
    }

    private static Bill bill2() {
        return Bill.builder()
                .orderDate(LocalDateTime.of(2021, 1, 5, 14, 10))
                .store(DEFAULT_STORE)
                .items(ImmutableSet.of(billItem1(),billItem2(), billItem3())).build();
    }

    private static BillItem billItem1() {
        return BillItem.builder()
                .product(TestProducts.AVOCADO.product)
                .quantity(1.0)
                .pricePerUnit(89.0)
                .totalPrice(89.0)
                .build();
    }

    private static BillItem billItem2() {
        return BillItem.builder()
                .product(TestProducts.LAMII.product)
                .pricePerUnit(22.0)
                .quantity(2.0)
                .totalPrice(44.0)
                .build();
    }

    private static BillItem billItem3() {
        return BillItem.builder()
                .product(TestProducts.SMINTINA_20.product)
                .pricePerUnit(39.0)
                .quantity(1.0)
                .totalPrice(39.0)
                .build();
    }
}
