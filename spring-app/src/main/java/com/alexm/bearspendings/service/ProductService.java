package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * @author AlexM
 */
public interface ProductService {
    List<Product> products();

    Optional<UIProduct> findProduct(Long id);
}
