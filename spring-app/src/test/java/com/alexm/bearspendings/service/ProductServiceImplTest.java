package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author AlexM
 */
@SpringBootTest
class ProductServiceImplTest {
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
        UIProduct product = productService.findProduct(1L);
        assertEquals(PROODUCT_CHEFIR , product.getName());
        verify(productRepository, times(1)) .findById(1L);
    }

    @Test
    void noProductFound(){
        assertThrows(NoSuchElementException.class, () -> productService.findProduct(222355L));
    }
}
