package com.alexm.bearspendings.entity;

import static com.alexm.bearspendings.service.impl.StoreServiceImpl.DEFAULT_STORE_NAME;

/**
 * @author AlexM
 * Date: 2/19/21
 **/
public class Defaults {
    private  Defaults() {
        // no instance
    }

    public static final Store         DEFAULT_STORE = Store.builder().id(1L).name(DEFAULT_STORE_NAME).build();
    public static final Category      DEFAULT_CATEGORY = Category.builder().name(Category.DEFAULT).build();
    public static final UnitOfMeasure DEFAULT_UNIT= new UnitOfMeasure();


}
