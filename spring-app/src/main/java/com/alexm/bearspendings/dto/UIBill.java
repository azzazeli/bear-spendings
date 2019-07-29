package com.alexm.bearspendings.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class UIBill {
    private Long id;
    private LocalDate orderDate;
    private Long storeId;
    private Set<UIBillItem> items;
}
