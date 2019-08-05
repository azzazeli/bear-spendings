package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;

import java.util.List;

/**
 * @author AlexM created on 7/11/19
 */
public interface BillService {
    List<UIBill> allBills();

    void addBill(UIBill bill);
}
