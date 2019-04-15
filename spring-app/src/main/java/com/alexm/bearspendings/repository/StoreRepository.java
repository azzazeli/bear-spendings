package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Store;
import org.springframework.data.repository.CrudRepository;

/**
 * @author AlexM
 */
public interface StoreRepository extends CrudRepository<Store, Long> {
}
