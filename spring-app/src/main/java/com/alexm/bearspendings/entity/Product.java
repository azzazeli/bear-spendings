package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString()
@EqualsAndHashCode(of = {"id"})
@Builder()
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String comment;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<BillItem> billItems;
    //todo: list of optional product properties
}
