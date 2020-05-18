package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.entity.Product;
import com.alexm.bearspendings.repository.ProductRepository;
import com.alexm.bearspendings.repository.StoreRepository;
import com.alexm.bearspendings.service.UnitOfMeasureService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author AlexM
 * Date: 8/16/19
 **/
@Component
public class BillCmd2BilFunction implements Function<BillCommand, Bill> {

    //todo: must work with services
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UnitOfMeasureService unitOfMeasureService;

    public BillCmd2BilFunction(ProductRepository productRepository, StoreRepository storeRepository, UnitOfMeasureService unitOfMeasureService) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @Override
    public Bill apply(BillCommand billCommand) {
        return Bill.builder()
                .store(storeRepository.getOne(billCommand.getStoreId()))
                .orderDate(billCommand.getOrderDate())
                .items(
                        billCommand.getItems().stream().map(uiBillItem -> BillItem.builder()
                                .product(newOrExistingProduct(uiBillItem))
                                .pricePerUnit(uiBillItem.getPricePerUnit())
                                .quantity(uiBillItem.getQuantity())
                                .build()).collect(Collectors.toSet())
                )
                .build();
    }

    private Product newOrExistingProduct(BillItemCommand billItemCommand) {
        if (Objects.isNull(billItemCommand.getProductId())) {
            final Product newProduct = Product.builder()
                    .name(billItemCommand.getProductName())
                    .unit(unitOfMeasureService.defaultUnit())
                    .build();
            return productRepository.save(newProduct);
        }
        return productRepository.getOne(billItemCommand.getProductId());
    }
}
