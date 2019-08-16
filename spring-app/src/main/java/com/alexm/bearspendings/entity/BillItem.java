package com.alexm.bearspendings.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor @Getter @Setter
@Accessors(fluent = true)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
}
