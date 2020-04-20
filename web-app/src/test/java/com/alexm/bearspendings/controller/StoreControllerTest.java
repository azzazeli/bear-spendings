package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.TopProductCommand;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(secure = false, controllers = StoreController.class)
class StoreControllerTest {
    private static final String STORES_URL = "/api/v1/stores";

    @Autowired
    MockMvc mvc;

    @MockBean
    StoreService storeService;
    final long NO_STORE_ID = 10002L;

    @BeforeEach
    void setup() {

        when(storeService.allStores()).thenReturn(ImmutableSet.of(
                Store.builder().id(1L).name("Nr.2").build(),
                Store.builder().id(2L).name("Pegas").build()
        ));
        when(storeService.findStore(1L))
                .thenReturn(Store.builder().id(1L).name("Nr.1").build());
        when(storeService.topProducts(eq(1L), anyInt())).thenReturn(ImmutableSet.of(
                TopProductCommand.builder().productId(23L).quantity(1.0).price(23.00).build()
        ));
        when(storeService.findStore(NO_STORE_ID)).thenThrow(new NoSuchElementException());
    }

    @Test
    void stores() throws Exception {
        mvc.perform(get(STORES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
        .andDo(print())
        ;
    }

    @DisplayName("get store")
    @Test
    void store() throws Exception {
        mvc.perform(get(STORES_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nr.1"))
                .andDo(print());
    }

    @DisplayName("store not found")
    @Test
    void noStore() throws Exception {
        mvc.perform(get(STORES_URL + NO_STORE_ID))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("test top store products")
    @Test
    void topStoreProducts() throws Exception {
        mvc.perform(get(STORES_URL + "/1/top_products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].productId").value(23L))
                .andExpect(jsonPath("$.[0].quantity").value(1))
                .andDo(print());
    }

}
