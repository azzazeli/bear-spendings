package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.TopProduct;
import com.alexm.bearspendings.entity.Store;
import com.alexm.bearspendings.repository.BillItemRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 9/8/19
 **/
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final BillItemRepository billItemRepository;

    public StoreServiceImpl(StoreRepository storeRepository, BillItemRepository billItemRepository) {
        this.storeRepository = storeRepository;
        this.billItemRepository = billItemRepository;
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
    public Set<TopProduct> topProducts(Long storeId, int size) {
        log.debug("Obtaining top product for store with id:{}", storeId);
        return billItemRepository.lastItemsWithDistinctProducts(storeId, PageRequest.of(0, size))
                .stream()
                .map(billItem -> TopProduct.builder()
                        .productId(billItem.getProduct().getId())
                        .price(billItem.getPrice())
                        .quantity(billItem.getQuantity())
                        .build()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
