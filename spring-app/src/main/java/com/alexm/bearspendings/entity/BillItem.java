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
@ToString
@EqualsAndHashCode(of = {"id"})
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
    private Integer quantity;
    @NotNull
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id")
    private Bill bill;
}
