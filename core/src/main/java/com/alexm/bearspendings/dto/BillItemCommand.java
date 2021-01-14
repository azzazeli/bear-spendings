package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Data
@JsonDeserialize(builder = BillItemCommand.BillItemCommandBuilder.class)
@ValidBillItemCommand
public class BillItemCommand {
    //todo: store reference to parent BillCommand
    private Long id;
    private String productName;
    private Long productId;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be a positive number")
    private Double quantity;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price per unit must be a positive number")
    private Double pricePerUnit;

    @NotNull(message = "Total price is mandatory")
    @PositiveOrZero(message = "Total price per unit must be a positive number or zero")
    private Double totalPrice;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BillItemCommandBuilder {

    }

    public static BillItemCommandBuilder builder() {
        return new CustomBillItemCommandBuilder();
    }

    private static class CustomBillItemCommandBuilder extends BillItemCommandBuilder {
        private CustomBillItemCommandBuilder() {
            super.pricePerUnit = 0.0;
            super.quantity = 0.0;
            super.totalPrice = 0.0;
        }
        @Override
        public BillItemCommand build() {
            Validate.isTrue(super.pricePerUnit >= 0,
                    "Provided price per unit:%s is invalid. Price must be positive", super.pricePerUnit );
            Validate.isTrue(super.totalPrice >= 0,
                    "Provided total price:%s is invalid. Price must be positive", super.totalPrice );
            Validate.isTrue(super.quantity >= 0,
                    "Provided quantity:%s is invalid. Quantity must be positive", super.quantity );
            return super.build();
        }
    }
}
