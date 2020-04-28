package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AlexM
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
}
