package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.TopProduct;
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

import java.util.Optional;

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

    @Autowired
    MockMvc mvc;

    @MockBean
    StoreService storeService;

    @BeforeEach
    void setup() {
        when(storeService.allStores()).thenReturn(ImmutableSet.of(
                Store.builder().id(1L).name("Nr.2").build(),
                Store.builder().id(2L).name("Pegas").build()
        ));
        when(storeService.findStore(1L))
                .thenReturn(Optional.of(Store.builder().id(1L).name("Nr.1").build()));
        when(storeService.topProducts(eq(1L), anyInt())).thenReturn(ImmutableSet.of(
                TopProduct.builder().productId(23L).quantity(1).price(23.00).build()
        ));
    }

    @Test
    void stores() throws Exception {
        mvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
        .andDo(print())
        ;
    }


    @DisplayName("get store")
    @Test
    void store() throws Exception {
        mvc.perform(get("/store/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nr.1"))
                .andDo(print());
    }

    @DisplayName("store not found")
    @Test
    void noStore() throws Exception {
        mvc.perform(get("/store/10002"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("test top store products")
    @Test
    void topStoreProducts() throws Exception {
        mvc.perform(get("/top_store_products?storeId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].productId").value(23L))
                .andExpect(jsonPath("$.[0].quantity").value(1))
                .andDo(print());
    }

}