package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.ProductCommand;
import com.alexm.bearspendings.entity.Product;

import java.util.List;

/**
 * @author AlexM
 */
public interface ProductService {
    List<Product> products();

    Product getOrInsert(String productName);

    ProductCommand findProduct(Long id);

    List<ProductCommand> findStartWith(String prefix);
}
