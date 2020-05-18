package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AlexM
 * Date: 5/14/20
 **/
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByName(String name);

}
