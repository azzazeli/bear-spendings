package com.alexm.bearspendings.service.impl.map;

import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.BillItem;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BillItem2BillItemCmd implements Function<BillItem, BillItemCommand> {

    @Override
    public BillItemCommand apply(BillItem billItem) {
        return BillItemCommand.builder()
                .id(billItem.getId())
                .pricePerUnit(billItem.getPricePerUnit())
                .totalPrice(billItem.getTotalPrice())
                .quantity(billItem.getQuantity())
                .productId(billItem.getProduct().getId())
                .build();

    }
}
