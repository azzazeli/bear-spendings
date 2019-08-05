package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@JsonDeserialize(builder = UIBill.UIBillBuilder.class)
@Data
public class UIBill {
    private Long id;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull(message = "OrderDate is mandatory")
    private LocalDateTime orderDate;
    @NotNull(message = "Store id is mandatory")
    @Positive(message = "Store id must be positive")
    private Long storeId;
    @Valid
    @NotEmpty(message = "Bill must contains at least one bill item")
    private Set<UIBillItem> items;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UIBillBuilder {

    }
}
