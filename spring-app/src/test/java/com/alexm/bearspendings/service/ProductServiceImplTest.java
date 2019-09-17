package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 */
@SpringBootTest
class ProductServiceImplTest {
    //todo: it is better to rely on integration test with h2
    @Mock
    ProductRepository productRepository;
    ProductService productService;
    private final String PROODUCT_CHEFIR = "chefir jlc 1%";

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                Product.builder().name("Lapte").build()
        ));
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder().id(1L).name(PROODUCT_CHEFIR).build()));
    }

    @Test
    void products() {
        assertThat(productService.products(), containsInAnyOrder(Product.builder().name("Lapte").build()));
    }

    @Test
    void product() {
        Optional<UIProduct> product = productService.findProduct(1L);
        assertTrue(product.isPresent());
        assertEquals(PROODUCT_CHEFIR , product.get().getName());
        verify(productRepository, times(1)) .findById(1L);
    }
}
