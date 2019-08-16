package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 8/16/19
 **/
@Component
public class UiBill2BilFunction implements Function<UIBill, Bill> {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public UiBill2BilFunction(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Bill apply(UIBill uiBill) {
        return Bill.builder()
                .store(storeRepository.getOne(uiBill.getStoreId()))
                .orderDate(uiBill.getOrderDate())
                .items(
                        uiBill.getItems().stream().map(uiBillItem -> BillItem.builder()
                                .product(productRepository.getOne(uiBillItem.getId()))
                                .price(uiBillItem.getPrice())
                                .quantity(uiBillItem.getQuantity())
                                .build()).collect(Collectors.toSet())
                )
                .build();
    }
}
