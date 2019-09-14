package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.alexm.bearspendings.bootstrap.DevBootstrap.ALLOWED_ORIGIN;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@Slf4j
@RestController
@CrossOrigin( origins = ALLOWED_ORIGIN)
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    public ResponseEntity<Iterable<Store>> stores() {
        log.debug("getting all stores ...");
        return ResponseEntity.ok().body(storeService.allStores());
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<Store> store(@PathVariable Long id) {
        log.debug("request for store with id: {}", id);
        return ResponseEntity.of(storeService.findStore(id));
    }

    @GetMapping("/top_store_products")
    public ResponseEntity<Iterable<Product>> topStoreProduct(@RequestParam(name = "storeId") Long storeId) {
        log.debug("Getting top products for store with id:{}", storeId);
        return ResponseEntity.ok(storeService.topProducts(storeId, 10));
    }


}
