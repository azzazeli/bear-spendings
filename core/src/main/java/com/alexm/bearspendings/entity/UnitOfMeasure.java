package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author AlexM
 * Date: 5/13/20
 **/
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, of = {"name"})
@ToString(callSuper = true, of = {"name"})
public class UnitOfMeasure extends BaseEntity {
    public static final String DEFAULT_UNIT = "unit";

    @Column(unique = true)
    private String name;

    public UnitOfMeasure() {
        this(DEFAULT_UNIT);
    }

    public UnitOfMeasure(String name) {
        this.name = name;
    }

    public UnitOfMeasure(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
