package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 8/16/19
 **/
@Component
public class UiBill2BilFunction implements Function<BillCommand, Bill> {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public UiBill2BilFunction(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Bill apply(BillCommand billCommand) {
        return Bill.builder()
                .store(storeRepository.getOne(billCommand.getStoreId()))
                .orderDate(billCommand.getOrderDate())
                .items(
                        billCommand.getItems().stream().map(uiBillItem -> BillItem.builder()
                                .product(newOrExistingProduct(uiBillItem))
                                .price(uiBillItem.getPrice())
                                .quantity(uiBillItem.getQuantity())
                                .build()).collect(Collectors.toSet())
                )
                .total(billCommand.getTotal()) //todo: maybe calculate total here ???
                .build();
    }

    private Product newOrExistingProduct(BillItemCommand billItemCommand) {
        if (Objects.isNull(billItemCommand.getProductId())) {
            return Product.builder().name(billItemCommand.getProductName()).build();
        }
        return productRepository.getOne(billItemCommand.getProductId());
    }
}
