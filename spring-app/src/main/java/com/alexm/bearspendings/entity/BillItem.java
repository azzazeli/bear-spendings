package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor @Getter @Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne()
    private Product product;
    private Integer quantity;
    private Double price;
}
