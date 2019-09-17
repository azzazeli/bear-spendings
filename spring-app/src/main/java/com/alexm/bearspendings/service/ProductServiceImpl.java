package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author AlexM
 */
@Service
@Slf4j()
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> products() {
        log.debug("all products");
        return repository.findAll();
    }

    @Override
    public Optional<UIProduct> findProduct(Long id) {
        log.debug("Finding product by id:{}", id);
        return repository.findById(id).map(product ->
                UIProduct.builder().id(product.getId()).name(product.getName()).build()
        );
    }
}
