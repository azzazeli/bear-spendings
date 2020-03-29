package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString()
@EqualsAndHashCode(callSuper=true, of = {"name"})
public class Product extends BaseEntity {
    private String name;
    private String comment;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<BillItem> billItems;
    //todo: list of optional product properties

    @Builder
    public Product(Long id, LocalDateTime createdDT, LocalDateTime modifiedDT,
                   String name, String comment) {
        super(id, createdDT, modifiedDT);
        this.name = name;
        this.comment = comment;
    }
}
