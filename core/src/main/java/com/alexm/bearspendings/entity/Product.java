package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, of = {"name", "comment"})
@EqualsAndHashCode(callSuper=true, of = {"name"})
@SuppressWarnings("JpaDataSourceORMInspection")
public class Product extends BaseEntity {
    private String name;
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Builder
    public Product(Long id, LocalDateTime createdDT, LocalDateTime modifiedDT,
                   @NonNull String name, UnitOfMeasure unit, String comment) {
        super(id, createdDT, modifiedDT);
        this.name = name;
        this.comment = comment;
        this.unit = unit;
    }
}
