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
@SuppressWarnings("JpaDataSourceORMInspection")
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

    //TODO: please implement m2: issue #35
    // spring jpa generate n+1 query; @FetchMode does not work properly
    // try to use @NamedEntityGraph and @EntityGraph
    // details here: https://stackoverflow.com/questions/29602386/how-does-the-fetchmode-work-in-spring-data-jpa
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
                ", billId=" + bill.getId() +
                '}';
    }
}
