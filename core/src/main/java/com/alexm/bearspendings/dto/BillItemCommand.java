package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
@JsonDeserialize(builder = BillItemCommand.BillItemCommandBuilder.class)
@ValidBillItemCommand
public class BillItemCommand {
    private Long id;
    private String productName;
    private Long productId;
    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be a positive number")
    private Double quantity;
    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BillItemCommandBuilder {

    }

    public static BillItemCommandBuilder builder() {
        return new CustomBillItemCommandBuilder();
    }

    private static class CustomBillItemCommandBuilder extends BillItemCommandBuilder {
        private CustomBillItemCommandBuilder() {
            super.price = 0.0;
            super.quantity = 0.0;
        }
        @Override
        public BillItemCommand build() {
            Validate.isTrue(super.price >= 0, "Provided price:%s is invalid. Price must be positive", super.price );
            Validate.isTrue(super.quantity >= 0, "Provided quantity:%s is invalid. Quantity must be positive", super.quantity );
            return super.build();
        }
    }
}
