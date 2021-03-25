package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor @Getter @Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, of = {"product", "quantity", "pricePerUnit"})
public class BillItem extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @NotNull
    private Double quantity;
    @NotNull
    private Double pricePerUnit;
    @NotNull
    @Builder.Default
    private Double totalPrice = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @PrePersist
    private void calculateTotalPrice() {
        this.totalPrice = twoDigits(quantity * pricePerUnit);
    }

    public Double getTotalPrice() {
        if (totalPrice == null || totalPrice == 0.0) {
            calculateTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", productId=" + ((product == null) ? null : product.getId()) +
                ", quantity=" + quantity +
                ", price=" + pricePerUnit +
                '}';
    }
}
