package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.repository.BillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author AlexM created on 7/11/19
 */
@Slf4j
@Service
public class BillServiceImpl implements BillService  {
    private final BillRepository billRepository;
    private final Function<UIBill, Bill> uiBill2BilFunction;

    private final Function<BillItem, UIBillItem> billItemToUiBillItemMap = billItem ->
            UIBillItem.builder()
                    .id(billItem.id())
                    .price(billItem.price())
                    .quantity(billItem.quantity())
                    .productId(billItem.product().getId())
                    .build();
    private final Function<Bill, UIBill> billToUiBillMap = bill ->
            UIBill.builder()
                    .id(bill.id())
                    .orderDate(bill.orderDate())
                    .storeId(bill.store().getId())
                    .items(bill.items().stream().map(billItemToUiBillItemMap).collect(Collectors.toSet()))
                    .build();


    public BillServiceImpl(BillRepository billRepository, Function<UIBill, Bill> uiBill2BilFunction) {
        this.billRepository = billRepository;
        this.uiBill2BilFunction = uiBill2BilFunction;
    }

    @Override
    public List<UIBill> allBills() {
        return StreamSupport.stream(billRepository.findAll().spliterator(), false)
                .map(billToUiBillMap)
                .collect(Collectors.toList());
    }

    @Override
    public void addBill(UIBill uiBill) {
        billRepository.save(uiBill2BilFunction.apply(uiBill));
    }

}


