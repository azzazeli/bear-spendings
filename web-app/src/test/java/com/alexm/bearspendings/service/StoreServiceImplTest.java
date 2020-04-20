package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.TopProductCommand;
import com.alexm.bearspendings.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@SpringBootTest()
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
        Store store = storeService.findStore(1L);
        assertAll("store properties",
                () -> assertEquals(1L, store.getId().longValue()),
                () -> assertEquals("Nr.1", store.getName())
        );
    }

    @Test
    void noSuchStore() {
        assertThrows(NoSuchElementException.class, () -> storeService.findStore(10002L));
    }


    @DisplayName("test getting of top store products")
    @Test
    void testTopProducts() {
        //given
        //when
        Set<TopProductCommand> products = storeService.topProducts(1L, 3);
        //then
        assertAll(
                () -> { assertEquals(3, products.size());}
        );

        TopProductCommand inghetata = products.stream().filter(topProduct -> topProduct.getProductId().equals(3L)).findFirst()
                .orElseThrow(RuntimeException::new);
        assertThat(inghetata,
                allOf(
                        hasProperty("productId", equalTo(3L)),
                        hasProperty("quantity", equalTo(1.0)),
                        hasProperty("price", equalTo(87.00))
                )
        );
        TopProductCommand chefir25 = products.stream().filter(topProduct -> topProduct.getProductId().equals(2L)).findFirst()
                .orElseThrow(RuntimeException::new);
        assertThat(chefir25,
                allOf(
                        hasProperty("productId", equalTo(2L)),
                        hasProperty("quantity", equalTo(1.0)),
                        hasProperty("price", equalTo(10.00))
                )
        );
    }
}
