package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author AlexM
 */
@Entity
@Setter @Getter @ToString @NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String city;
    private String location;

}
