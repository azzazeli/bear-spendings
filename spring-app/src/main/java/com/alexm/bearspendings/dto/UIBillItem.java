package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
@JsonDeserialize(builder = UIBillItem.UIBillItemBuilder.class)
@ValidUiBillItem
public class UIBillItem {
    private Long id;
    private String productName;
    private Long productId;
    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UIBillItemBuilder {

    }
}
