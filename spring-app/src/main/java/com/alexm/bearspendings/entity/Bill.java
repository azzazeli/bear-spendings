package com.alexm.bearspendings.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author AlexM
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private LocalDate orderDate;
    @NotNull
    @OneToOne
    private Store store;

    @NotEmpty
    @Singular
    @OneToMany
    @JoinColumn(name = "bill_id")
    Set<BillItem> items;
}
