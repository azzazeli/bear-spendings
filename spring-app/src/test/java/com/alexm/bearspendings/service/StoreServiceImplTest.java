package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@SpringBootTest
class StoreServiceImplTest {

    @Autowired
    StoreServiceImpl storeService;

    @BeforeEach
    void setup() {
//        storeService = new StoreServiceImpl();
    }


    @DisplayName("all stores test")
    @Test
    void allStores() {
        assertThat(storeService.allStores(), containsInAnyOrder(Store.builder().id(1L).name("Nr.1").build()));
    }
}