package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.service.ProductService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static com.alexm.bearspendings.test.SAMPLE_PRODUCTS.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlexM
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(secure = false, controllers = ProductController.class)
class ProductControllerTest {
    private static final String PRODUCTS_URL = "/api/v1/products";

    @Autowired
    MockMvc mockMvc;
    private final Long UNKNOWN_PRODUCT = 122345L;

    @MockBean
    ProductService productService;

    @BeforeEach
    void setup() {
        when(productService.findProduct(CHEFIR.id)).thenReturn(CHEFIR.uiProduct);
        when(productService.findProduct(UNKNOWN_PRODUCT)).thenThrow(NoSuchElementException.class);
        when(productService.findStartWith("ca")).thenReturn(Lists.list(
           CARNE.uiProduct, CARTOFI.uiProduct
        ));
    }

    @Test
    void product() throws Exception {
        mockMvc.perform(get(PRODUCTS_URL + "/" + CHEFIR.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHEFIR.id))
                .andExpect(jsonPath("$.name").value(CHEFIR.productName))
                .andDo(print());
        mockMvc.perform(get(PRODUCTS_URL + UNKNOWN_PRODUCT.toString()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void startWith() throws Exception {
        mockMvc.perform(get(PRODUCTS_URL + "?startWith=ca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(CARNE.id))
                .andExpect(jsonPath("$.[0].name").value(CARNE.productName))
                .andExpect(jsonPath("$.[1].id").value(CARTOFI.id))
                .andExpect(jsonPath("$.[1].name").value(CARTOFI.productName))
                .andDo(print());
    }
}
