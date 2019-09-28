package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.repository.BillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                    .id(billItem.getId())
                    .price(billItem.getPrice())
                    .quantity(billItem.getQuantity())
                    .productId(billItem.getProduct().getId())
                    .build();
    private final Function<Bill, UIBill> billToUiBillMap = bill ->
            UIBill.builder()
                    .id(bill.getId())
                    .orderDate(bill.getOrderDate())
                    .storeId(bill.getStore().getId())
                    .items(bill.getItems().stream().map(billItemToUiBillItemMap).collect(Collectors.toSet()))
                    .build();


    public BillServiceImpl(BillRepository billRepository, Function<UIBill, Bill> uiBill2BilFunction) {
        this.billRepository = billRepository;
        this.uiBill2BilFunction = uiBill2BilFunction;
    }

    @Transactional
    @Override
    public List<UIBill> allBills() {
        return StreamSupport.stream(billRepository.findAll().spliterator(), false)
                .map(billToUiBillMap)
                .collect(Collectors.toList());
    }

    @Override
    public Bill addBill(UIBill uiBill) {
        Bill bill = uiBill2BilFunction.apply(uiBill);
        return billRepository.save(bill);
    }

    @Override
    public Long allBillsCount() {
        return billRepository.count();
    }
}


