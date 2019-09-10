package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
