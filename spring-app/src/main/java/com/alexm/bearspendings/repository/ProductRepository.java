package com.alexm.bearspendings.repository;

import com.alexm.bearspendings.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author AlexM
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
