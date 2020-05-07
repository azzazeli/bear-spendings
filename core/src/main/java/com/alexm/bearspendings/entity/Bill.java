package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill", fetch = FetchType.EAGER)
    private Set<BillItem> items = new HashSet<>();
    private Double total = 0.0;

    @Builder
    public Bill(Long id, @NotNull LocalDateTime orderDate, @NotNull Store store,
         @NotEmpty Set<BillItem> items, Double total) {
        this.id = id;
        this.orderDate = orderDate;
        this.store = store;
        this.setItems(items);
        this.total = (total == null ) ? 0.0 : total;
    }

    public void setItems(Set<BillItem> items) {
        items.forEach(billItem -> billItem.setBill(this));
        this.items.addAll(items);
    }

    public Set<BillItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void addItem(BillItem item) {
        item.setBill(this);
        this.items.add(item);
    }

    @PrePersist
    void calculateTotal() {
        this.total = items.stream().mapToDouble(item ->  item.getPrice() * item.getQuantity())
                .reduce(0, Double::sum);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", storeId=" + (store != null ? store.getId() : null) +
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
