package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Iterable<Store> stores() {
        log.debug("getting all stores ...");
        return storeService.allStores();
    }
}
