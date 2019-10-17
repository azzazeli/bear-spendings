package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.ALL;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor @Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode()
@Builder
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    @JoinColumn(name = "product_id")
    private Product product;
    @NotNull
    private Double quantity;
    @NotNull
    private Double price;

    //TODO: please implement m2: issue #35
    // spring jpa generate n+1 query; @FetchMode does not work properly
    // try to use @NamedEntityGraph and @EntityGraph
    // details here: https://stackoverflow.com/questions/29602386/how-does-the-fetchmode-work-in-spring-data-jpa
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", productId=" + product.getId() +
                ", quantity=" + quantity +
                ", price=" + price +
                ", billId=" + bill.getId() +
                '}';
    }
}
