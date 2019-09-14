package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.TopProduct;
import com.alexm.bearspendings.entity.Product;
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

    /**
     * get a set of top products in the store
     * @param storeId identifier  of {@link Store}
     * @param size number of returned products
     * @return set of {@link Product}s
     */
    Set<TopProduct> topProducts(Long storeId, int size);
}
