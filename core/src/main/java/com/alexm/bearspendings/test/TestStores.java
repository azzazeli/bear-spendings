package com.alexm.bearspendings.test;

import com.alexm.bearspendings.entity.Store;

/**
 * @author AlexM
 * Date: 2/25/21
 **/
public enum TestStores {
    FARMACIA_FAMILIEI(2L, "Farmacia Familiei"),
    ALIMARKET(3L, "Alimarket");

    TestStores(Long id, String name) {
        this.storeId = id;
        this.storeName = name;
        this.store = Store.builder().name(storeName).id(storeId).build();
    }

    public final String storeName;
    public final Store store;
    public final Long storeId;
}
