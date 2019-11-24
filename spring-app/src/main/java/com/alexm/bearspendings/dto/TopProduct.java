package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

/**
 * Class represents a product bought from a store with price and quantity from bill item.
 * Class is used in 'top store' feature - to accelerate data entry.
 * //TODO May be a better name for class is StoreProduct
 * @author AlexM
 * Date: 9/14/19
 **/

@Builder
@JsonDeserialize(builder = BillCommand.BillCommandBuilder.class)
@Data
public class TopProduct {
    /**
     * product id
     * //TODO may be rename field to productId
     */
    private Long productId;
    private Double quantity;
    private Double price;
}
