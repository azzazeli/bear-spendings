package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alexm.bearspendings.BearSpendingsApplication.API_URL;
import static com.alexm.bearspendings.bootstrap.DevBootstrap.ALLOWED_ORIGIN;

/**
 * @author AlexM
 */

@RestController
@CrossOrigin( origins = ALLOWED_ORIGIN)
@RequestMapping(API_URL + "products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping()
    public List<Product> products() {
        return Stream.of(Product.builder().name("Lapte").build()).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public UIProduct product(@PathVariable Long id) {
        return productService.findProduct(id);
    }


}
