package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author AlexM
 */

@Entity
@Builder
@NoArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime orderDate;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Store store;

    @Setter(AccessLevel.NONE)
    @NotEmpty
//    @Singular
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private Set<BillItem> items;

    Bill(Long id, @NotNull LocalDateTime orderDate, @NotNull Store store, @NotEmpty Set<BillItem> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.store = store;
        this.setItems(items);
    }


    public void setItems(Set<BillItem> items) {
        this.items = items;
        this.items.forEach(billItem -> billItem.setBill(this));
    }

    public static class BillBuilder {
        private Set<BillItem> items;

        public BillBuilder items(Set<BillItem> items) {
            this.items = items;
            return this;
        }
    }
}
