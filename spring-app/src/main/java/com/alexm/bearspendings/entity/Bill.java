package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date orderDate;
    @ManyToOne
    private Store store;

    @OneToMany
    @JoinColumn(name = "bill_item_id")
    Set<BillItem> items = new HashSet<>();
}
