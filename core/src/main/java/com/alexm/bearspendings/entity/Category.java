package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author AlexM
 * Date: 2/17/21
 **/

@Getter
@Setter
@Entity
@ToString(callSuper = true, of = {"name"})
@EqualsAndHashCode(callSuper=true, of = {"name"})
public class Category extends BaseEntity {

    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    @Builder
    public Category(Long id, LocalDateTime createdDT, LocalDateTime modifiedDT, String name) {
        super(id, createdDT, modifiedDT);
        this.name = name;
    }
}
