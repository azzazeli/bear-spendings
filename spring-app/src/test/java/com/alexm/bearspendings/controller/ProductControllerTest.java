package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIProduct;
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

import static com.alexm.bearspendings.controller.ProductControllerTest.SAMPLE_PRODUCTS.*;
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

    enum SAMPLE_PRODUCTS {
        CARTOFI(1L, "Cartofi"),
        CARNE(2L, "Carne"),
        CHEFIR(1L, "Chefir");

        private final UIProduct uiProduct;
        private final Long id;
        private final String name;

        SAMPLE_PRODUCTS(long id, String name) {
            this.id = id;
            this.name = name;
            this.uiProduct = UIProduct.builder().id(id).name(name).build();
        }
    }

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
                .andExpect(jsonPath("$.name").value(CHEFIR.name))
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
                .andExpect(jsonPath("$.[0].name").value(CARNE.name))
                .andExpect(jsonPath("$.[1].id").value(CARTOFI.id))
                .andExpect(jsonPath("$.[1].name").value(CARTOFI.name))
                .andDo(print());
    }
}
