package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.entity.BillItem;
import com.alexm.bearspendings.repository.BillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final Function<BillCommand, Bill> uiBill2BilFunction;

    private final Function<BillItem, BillItemCommand> billItemToUiBillItemMap = billItem ->
            BillItemCommand.builder()
                    .id(billItem.getId())
                    .price(billItem.getPrice())
                    .quantity(billItem.getQuantity())
                    .productId(billItem.getProduct().getId())
                    .build();
    private final Function<Bill, BillCommand> billToUiBillMap = bill ->
            BillCommand.builder()
                    .id(bill.getId())
                    .orderDate(bill.getOrderDate())
                    .storeId(bill.getStore().getId())
                    .total(bill.getTotal())
                    .items(bill.getItems().stream().map(billItemToUiBillItemMap).collect(Collectors.toSet()))
                    .build();


    public BillServiceImpl(BillRepository billRepository, Function<BillCommand, Bill> uiBill2BilFunction) {
        this.billRepository = billRepository;
        this.uiBill2BilFunction = uiBill2BilFunction;
    }

    @Transactional
    @Override
    public List<BillCommand> allBills(int page, int size) {
        Sort byOrderDateDesc = Sort.by(Sort.Direction.DESC, "id", "orderDate");
        Pageable pageable = PageRequest.of(page, size, byOrderDateDesc);
        return StreamSupport.stream(billRepository.findAll(pageable).spliterator(), false)
                .map(billToUiBillMap)
                .collect(Collectors.toList());
    }

    @Override
    public Bill addBill(BillCommand billCommand) {
        Bill bill = uiBill2BilFunction.apply(billCommand);
        return billRepository.save(bill);
    }

    @Override
    public Long allBillsCount() {
        return billRepository.count();
    }

    @Override
    public void saveAll(Iterable<Bill> bills) {
        //todo: implement me
    }
}


