package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("get store by id")
    @Test
    void store() {
        Optional<Store> optionalStore = storeService.findStore(1L);
        assertTrue(optionalStore.isPresent());
        optionalStore.ifPresent(store ->
                assertAll("store properties",
                        () -> assertEquals(1L, store.getId().longValue()),
                        () -> assertEquals("Nr.1", store.getName())
                )
        );
        optionalStore = storeService.findStore(10002L);
        assertFalse(optionalStore.isPresent());
    }
}