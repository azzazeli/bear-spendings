package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Set<Store> allStores() {
        log.info("Getting all stores ....");
        Set<Store> stores = new HashSet<>();
        storeRepository.findAll().iterator().forEachRemaining(stores::add);
        return stores;
    }

    @Override
    public Optional<Store> findStore(Long id) {
        log.debug("Getting store for id: {}", id);
        return storeRepository.findById(id);
    }
}
