package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    }

    @Test
    void stores() throws Exception {
        mvc.perform(get("/stores"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
        .andDo(print())
        ;
    }
}