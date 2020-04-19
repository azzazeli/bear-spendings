package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.TopProduct;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.alexm.bearspendings.BearSpendingsApplication.API_URL;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@Slf4j
@RestController()
@RequestMapping(API_URL + "stores")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping()
    public Set<Store> stores() {
        log.debug("getting all stores ...");
        return storeService.allStores();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Store store(@PathVariable Long id) {
        log.debug("request for store with id: {}", id);
        return storeService.findStore(id);
    }

    @GetMapping("/{id}/top_products")
    public ResponseEntity<Iterable<TopProduct>> topStoreProduct(@PathVariable Long id) {
        log.debug("Getting top products for store with id:{}", id);
        return ResponseEntity.ok(storeService.topProducts(id, 10));
    }


}
