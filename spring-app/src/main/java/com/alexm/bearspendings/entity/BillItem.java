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
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @OneToOne()
    private Product product;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
}
