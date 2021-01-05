package com.alexm.bearspendings;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.google.common.collect.ImmutableSet;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author AlexM
 * Date: 1/5/21
 **/
public class TestBills {

    public static List<BillCommand> sampleBills() {
        List<BillCommand> bills = new LinkedList<>();
        bills.add(bill1());
        bills.add(bill2());
        return bills;
    }

    private static BillCommand bill1() {
        return BillCommand.builder()
                .orderDate(LocalDateTime.of(2021, 1, 4, 14, 10))
                .items(ImmutableSet.of(billItem1(),billItem2())).build();
    }

    private static BillCommand bill2() {
        return BillCommand.builder()
                .orderDate(LocalDateTime.of(2021, 1, 5, 14, 10))
                .items(ImmutableSet.of(billItem1(),billItem2())).build();
    }

    private static BillItemCommand billItem1() {
        return BillItemCommand.builder()
                .id(1L)
                .build();
    }

    private static BillItemCommand billItem2() {
        return BillItemCommand.builder()
                .id(2L)
                .build();
    }
}
