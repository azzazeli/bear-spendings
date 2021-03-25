package com.alexm.bearspendings.service.impl.map;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.entity.Bill;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 5/9/20
 **/
@Component
public class Bill2BillCmd implements Function<Bill, BillCommand> {
    private final BillItem2BillItemCmd billItem2BillItemCmd;

    public Bill2BillCmd(BillItem2BillItemCmd billItem2BillItemCmd) {
        this.billItem2BillItemCmd = billItem2BillItemCmd;
    }

    @Override
    public BillCommand apply(Bill bill) {
        return BillCommand.builder()
                .id(bill.getId())
                .orderDate(bill.getOrderDate())
                .storeId(bill.getStore().getId())
                .total(bill.getTotal())
                .items(bill.getItems().stream().map(billItem2BillItemCmd).collect(Collectors.toSet()))
                .build();
    }
}
