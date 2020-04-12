package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author AlexM
 * Date: 4/12/20
 **/
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void findByNameStartsWithIgnoreCase() {
        final String prefix = "cHe";
        final List<Product> result = productRepository.findByNameStartsWithIgnoreCase(prefix);
        assertThat(result)
                .extracting(Product::getName)
                .satisfies(names -> names.forEach(
                        name -> assertTrue(name.toUpperCase().startsWith(prefix.toUpperCase())))
                );
    }

    @Test
    void findByName() {
        final Optional<Product> byName = productRepository.findByName("chefir jlc 1%");
        assertTrue(byName.isPresent());
    }
}