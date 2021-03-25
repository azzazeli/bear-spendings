package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.entity.Bill;

import java.util.List;
import java.util.Optional;

/**
 * @author AlexM created on 7/11/19
 */
public interface BillService {
    List<BillCommand> allBills(int page, int size);

    List<Bill> bills(int page, int size);

    Bill addBill(BillCommand bill);

    Long allBillsCount();

    Iterable<Bill> saveAll(Iterable<Bill> bills);

    Optional<Bill> findById(Long id);
}
