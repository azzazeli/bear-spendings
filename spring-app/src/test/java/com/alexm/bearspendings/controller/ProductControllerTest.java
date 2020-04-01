package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
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
    private final Long CHEFIR_PROD_ID = 1L;
    private final Long UNKNOWN_PRODUCT = 122345L;
    private final String CHEFIR_PROD_NAME  = "chefir jlc 1%";

    @MockBean
    ProductService productService;

    @BeforeEach
    void setup() {
        when(productService.findProduct(CHEFIR_PROD_ID)).thenReturn(
                UIProduct.builder().id(CHEFIR_PROD_ID).name(CHEFIR_PROD_NAME).build()
        );
        when(productService.findProduct(UNKNOWN_PRODUCT)).thenThrow(NoSuchElementException.class);
    }

    @Test
    void products() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lapte"));
    }

    @Test
    void product() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS_URL + "/" + CHEFIR_PROD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHEFIR_PROD_ID))
                .andExpect(jsonPath("$.name").value(CHEFIR_PROD_NAME))
                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS_URL +UNKNOWN_PRODUCT.toString()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
