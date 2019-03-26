package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 */
@SpringBootTest
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;

//    @InjectMocks
    ProductService productService;


    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                new Product()
        ));
    }


    @Test
    void products() {
//        assertEquals(productService.products()).con
    }
}
