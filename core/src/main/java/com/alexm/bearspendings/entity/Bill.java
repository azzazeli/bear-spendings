package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author AlexM
 */

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = true, of = {})
public class Bill extends BaseEntity {
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
    private Double total;

    @Builder
    public Bill(Long id, @NotNull LocalDateTime orderDate, @NotNull Store store,
         @NotEmpty Set<BillItem> items, @NotNull Double total) {
        this.id = id;
        this.orderDate = orderDate;
        this.store = store;
        this.setItems(items);
        this.total = total;
    }

    public void setItems(Set<BillItem> items) {
        this.items = items;
        this.items.forEach(billItem -> billItem.setBill(this));
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", storeId=" + store.getId() +
                ", createdDT=" + createdDT +
                ", createdDT=" + modifiedDT +
                '}';
    }

    public static class BillBuilder {
        private Set<BillItem> items = new HashSet<>();

        public BillBuilder items(Collection<BillItem> items) {
            this.items.addAll(items);
            return this;
        }
    }
}
