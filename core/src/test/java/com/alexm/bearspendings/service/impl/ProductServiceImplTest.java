package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.ProductCommand;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.service.UnitOfMeasureService;
import com.alexm.bearspendings.test.SAMPLE_PRODUCTS;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void products() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(
                Product.builder().name("Lapte").build()
        ));
        assertThat(productService.products(), containsInAnyOrder(Product.builder().name("Lapte").build()));
    }

    @Test
    void product() {
        when(productRepository.findById(SAMPLE_PRODUCTS.CHEFIR.id)).thenReturn(Optional.of(Product.builder().id(1L).name(SAMPLE_PRODUCTS.CHEFIR.productName).build()));
        ProductCommand product = productService.findProduct(SAMPLE_PRODUCTS.CHEFIR.id);
        org.junit.jupiter.api.Assertions.assertEquals(SAMPLE_PRODUCTS.CHEFIR.productName, product.getName());
        verify(productRepository, times(1)) .findById(SAMPLE_PRODUCTS.CHEFIR.id);
    }

    @Test
    void noProductFound(){
        assertThrows(NoSuchElementException.class, () -> productService.findProduct(222355L));
    }

    @Test
    void findStartWith() {
        //given
        when(productRepository.findByNameStartsWithIgnoreCase("cA"))
                .thenReturn(Lists.list(SAMPLE_PRODUCTS.CARTOFI.product, SAMPLE_PRODUCTS.CARNE.product));
        //when
        final List<ProductCommand> productCommands = productService.findStartWith("cA");
        //then
        Assertions.assertThat(productCommands).extracting(ProductCommand::getId).containsExactly(1L, 2L);
    }

    @Test
    void getOrInsert() {
        String avocadoName = "Avocado";
        when(productRepository.findByName(avocadoName)).thenReturn(Optional.empty());
        final Product avocado = Product.builder().id(1244L).name(avocadoName).build();
        when(productRepository.save(any(Product.class))).thenReturn(avocado);
        final Product saved = productService.getOrInsert(avocadoName);
        when(productRepository.findByName(avocadoName)).thenReturn(Optional.of(avocado));
        final Product second = productService.getOrInsert(avocadoName);
        assertEquals(saved.getId(), second.getId());
    }
}
