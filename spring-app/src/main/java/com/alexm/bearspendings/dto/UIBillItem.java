package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonDeserialize(builder = UIBillItem.UIBillItemBuilder.class)
public class UIBillItem {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UIBillItemBuilder {

    }
}
