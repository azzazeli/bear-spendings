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
public class UIBillItem {
    private Long id;
    @NotNull(message = "Product id is mandatory")
    @Positive(message = "Product id must be positive")
    private Long productId;
    @NotNull
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
    @NotNull
    @Positive(message = "Price must be a positive number")
    private Double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UIBillItemBuilder {

    }
}
