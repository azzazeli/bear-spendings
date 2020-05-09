package com.alexm.bearspendings.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * @author AlexM
 * Date: 3/29/20
 **/
@Getter
@Setter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @CreatedDate
    LocalDateTime createdDT;
    @LastModifiedDate
    LocalDateTime modifiedDT;

    //todo: use BigDecimal
    Double twoDigits(Double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }
}
