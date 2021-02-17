package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.TopProductCommand;
import com.alexm.bearspendings.entity.Store;

import java.util.Set;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
// todo: group StoreService, ProductService, BillService and CategoryService to SpengingService
public interface StoreService {
    Store defaultStore();

    Set<Store> allStores();

    Store findStore(Long id);

    /**
     * get a set of top products in the store
     * @param storeId identifier  of {@link Store}
     * @param size number of returned products
     */
    Set<TopProductCommand> topProducts(Long storeId, int size);

    Store getOrInsert(String storeName);


}
