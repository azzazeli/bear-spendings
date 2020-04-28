package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
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
public class Product extends BaseEntity {
    private String name;
    private String comment;

    //todo: list of optional product properties

    @Builder
    public Product(Long id, LocalDateTime createdDT, LocalDateTime modifiedDT,
                   @NonNull String name, String comment) {
        super(id, createdDT, modifiedDT);
        this.name = name;
        this.comment = comment;
    }
}
