package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alexm.bearspendings.bootstrap.DevBootstrap.ALLOWED_ORIGIN;

/**
 * @author AlexM
 */

@RestController
@CrossOrigin( origins = ALLOWED_ORIGIN)
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    public List<Product> products() {
        //TODO no usage at all
        return Stream.of(Product.builder().name("Lapte").build()).collect(Collectors.toList());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<UIProduct> product(@PathVariable Long id) {
        return ResponseEntity.of(productService.findProduct(id));
    }


}
