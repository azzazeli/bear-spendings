package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author AlexM
 */
@Entity
@Setter @Getter @ToString @NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {})
public class Store extends BaseEntity {
    private String name;
    private String country;
    private String city;
    private String location;

    @Builder
    public Store(
            Long id, LocalDateTime createdDT, LocalDateTime modifiedDT,
            String name, String country, String city, String location) {
        super(id, createdDT, modifiedDT);
        this.name = name;
        this.country = country;
        this.city = city;
        this.location = location;
    }
}
