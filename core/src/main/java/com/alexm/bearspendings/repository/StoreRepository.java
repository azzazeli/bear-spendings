package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author AlexM
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
}
