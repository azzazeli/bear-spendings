package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

/**
 * @author AlexM
 */
@SpringBootTest
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                new Product("Lapte")
        ));
    }

    @Test
    void products() {
        assertThat(productService.products(), containsInAnyOrder(new Product("Lapte")));
    }
}
