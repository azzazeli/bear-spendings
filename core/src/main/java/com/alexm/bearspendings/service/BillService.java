package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.entity.Bill;

import java.util.List;

/**
 * @author AlexM created on 7/11/19
 */
public interface BillService {
    List<BillCommand> allBills(int page, int size);

    Bill addBill(BillCommand bill);

    Long allBillsCount();

    void saveAll(Iterable<Bill> bills);
}
