package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.TopProductCommand;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillItemRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import com.alexm.bearspendings.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {
    public static final String DEFAULT_STORE_NAME = "DEFAULT";

    private final StoreRepository storeRepository;
    private final BillItemRepository billItemRepository;

    public StoreServiceImpl(StoreRepository storeRepository, BillItemRepository billItemRepository) {
        this.storeRepository = storeRepository;
        this.billItemRepository = billItemRepository;
    }

    @Override
    public Store defaultStore() {
        return storeRepository.findByName(DEFAULT_STORE_NAME)
                .orElseGet(() -> storeRepository.save(Store.builder().name(DEFAULT_STORE_NAME).build()));
    }

    @Override
    public Set<Store> allStores() {
        log.info("Getting all stores ....");
        Set<Store> stores = new HashSet<>();
        storeRepository.findAll().iterator().forEachRemaining(stores::add);
        return stores;
    }

    @Override
    public Store findStore(Long id) {
        log.debug("Getting store for id: {}", id);
        return storeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));
    }


    /**
     * Return last 10 products from bills
     * @param storeId identifier  of {@link Store}
     * @param size number of returned products
     */
    @Override
    public Set<TopProductCommand> topProducts(Long storeId, int size) {
        log.debug("Obtaining top product for store with id:{}", storeId);
        return billItemRepository.lastItemsWithDistinctProducts(storeId, PageRequest.of(0, size))
                .stream()
                .map(billItem -> TopProductCommand.builder()
                        .productId(billItem.getProduct().getId())
                        .price(billItem.getPricePerUnit())
                        .quantity(billItem.getQuantity())
                        .build()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Store getOrInsert(String storeName) {
        return storeRepository.findByName(storeName)
                .orElseGet(() -> storeRepository.save(Store.builder().name(storeName).build()));
    }
}
