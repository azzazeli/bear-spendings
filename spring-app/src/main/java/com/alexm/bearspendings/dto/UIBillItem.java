package com.alexm.bearspendings.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UIBillItem {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}
