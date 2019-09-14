package com.alexm.bearspendings.service;

import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    private final ProductRepository productRepository;

    public StoreServiceImpl(StoreRepository storeRepository, ProductRepository productRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
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


    /**
     * Return last 10 products from bills
     * @param storeId identifier  of {@link Store}
     * @param size number of returned products
     * @return
     */
    @Override
    public Set<Product> topProducts(Long storeId, int size) {
        log.debug("Obtaining top product for store with id:{}", storeId);
        List<Product> products = productRepository.topByStore(storeId, PageRequest.of(0, size));
        return new HashSet<>(products);
    }
}
