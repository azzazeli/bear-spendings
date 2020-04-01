package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;

import java.util.List;

/**
 * @author AlexM
 */
public interface ProductService {
    List<Product> products();

    UIProduct findProduct(Long id);
}
