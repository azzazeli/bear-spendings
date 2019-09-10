package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Store;

import java.util.Optional;
import java.util.Set;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
public interface StoreService {
    Set<Store> allStores();
    Optional<Store> findStore(Long id);
}
