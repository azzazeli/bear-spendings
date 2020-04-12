package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author AlexM
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String productName);
    List<Product> findByNameStartsWithIgnoreCase(String prefix);
}
