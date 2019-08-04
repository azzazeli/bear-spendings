package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@JsonDeserialize(builder = UIBill.UIBillBuilder.class)
@Data
public class UIBill {
    private Long id;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime orderDate;
    private Long storeId;
    private Set<UIBillItem> items;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UIBillBuilder {

    }
}
