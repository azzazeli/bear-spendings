package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIProduct;
import com.alexm.bearspendings.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.alexm.bearspendings.BearSpendingsApplication.API_URL;

/**
 * @author AlexM
 */
@Slf4j
@RestController
@RequestMapping(API_URL + "products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public List<UIProduct> startWith(@RequestParam(name = "startWith") String prefix) {
        log.debug("Search products by name starts with:{}", prefix);
        return productService.findStartWith(prefix);
    }

    @GetMapping("{id}")
    public UIProduct product(@PathVariable Long id) {
        return productService.findProduct(id);
    }


}
