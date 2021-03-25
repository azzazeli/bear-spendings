package com.alexm.bearspendings.service.impl;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.repository.BillRepository;
import com.alexm.bearspendings.service.BillService;
import com.alexm.bearspendings.service.impl.map.Bill2BillCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * @author AlexM created on 7/11/19
 */
@Slf4j
@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final Function<BillCommand, Bill> uiBill2BilFunction;
    private final Bill2BillCmd bill2BillCmd;


    public BillServiceImpl(BillRepository billRepository,
                           Function<BillCommand, Bill> uiBill2BilFunction,
                           Bill2BillCmd bill2BillCmd) {
        this.billRepository = billRepository;
        this.uiBill2BilFunction = uiBill2BilFunction;
        this.bill2BillCmd = bill2BillCmd;
    }

    @Transactional
    @Override
    public List<BillCommand> allBills(int page, int size) {
        Page<Bill> all = billRepository.findAll(pageable(page, size));
        return StreamSupport.stream(all.spliterator(), false)
                .map(bill2BillCmd)
                .collect(toList());
    }

    private Pageable pageable(int page, int size) {
        Sort byOrderDateDesc = Sort.by(Sort.Direction.DESC, "id", "orderDate");
        return PageRequest.of(page, size, byOrderDateDesc);
    }

    @Override
    public List<Bill> bills(int page, int size) {
        return billRepository.allGraph(pageable(page, size)).stream().collect(toList());
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
    public Iterable<Bill> saveAll(Iterable<Bill> bills) {
        return billRepository.saveAll(bills);
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }
}


