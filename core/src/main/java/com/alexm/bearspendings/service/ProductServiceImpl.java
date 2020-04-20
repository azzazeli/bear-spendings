package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.ProductCommand;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public ProductCommand findProduct(Long id) {
        log.debug("Finding product by id:{}", id);
        final Product product = repository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));
        return ProductCommand.builder().id(product.getId()).name(product.getName()).build();
    }

    @Override
    public List<ProductCommand> findStartWith(String prefix) {
        return repository.findByNameStartsWithIgnoreCase(prefix).stream()
                //todo: extract this map
                .map(product -> ProductCommand.builder().id(product.getId()).name(product.getName()).build())
                .collect(Collectors.toList());
    }
}
