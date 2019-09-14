package com.alexm.bearspendings.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

/**
 * Class represents a product bought from a store with price and quantity from bill item.
 * Class is used in 'top store' feature - to accelerate data entry.
 * @author AlexM
 * Date: 9/14/19
 **/

@Builder
@JsonDeserialize(builder = UIBill.UIBillBuilder.class)
@Data
public class TopProduct {
    /**
     * product id
     * //TODO may be rename field to productId
     */
    private Long id;
    private Integer quantity;
    private Double price;
}
