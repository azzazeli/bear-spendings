package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author AlexM
 */

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> products() {
        return Stream.of(new Product("Lapte")).collect(Collectors.toList());
    }
}
