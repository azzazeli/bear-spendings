package com.alexm.bearspendings.imports.excel;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.Store;
import lombok.Value;

import java.time.LocalDate;

/**
 * @author AlexM
 * Date: 4/23/20
 **/
@Value
public class ImportBill {
    private LocalDate orderDate;
    private Store store;

    public Bill toBill() {
        return Bill.builder()
                .store(store)
                .orderDate(orderDate.atStartOfDay())
                .build();
    }
}
