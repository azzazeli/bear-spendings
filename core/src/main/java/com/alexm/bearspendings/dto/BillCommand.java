package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = BillCommand.BillCommandBuilder.class)
@Data
public class BillCommand {
    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull(message = "OrderDate is mandatory")
    private LocalDateTime orderDate;

    @NotNull(message = "Store id is mandatory")
    @Positive(message = "Store id must be positive")
    private Long storeId;

    @NotNull
    @PositiveOrZero(message = "Total must be greater than zero")
    private Double total;

    @Valid
    @NotEmpty(message = "Bill must contains at least one bill item")
    private Set<BillItemCommand> items;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BillCommandBuilder {

    }

    public static BillCommandBuilder builder() {
        return new CustomBillCommandBuilder();
    }

    private static class CustomBillCommandBuilder extends BillCommandBuilder {
        private CustomBillCommandBuilder() {
            super.total = 0.0;
        }

        @Override
        public BillCommand build() {
            Validate.isTrue(super.total >= 0.0, "Provided total:%s is invalid. Total must be positive", super.total );
            return super.build();
        }
    }

}
