package com.alexm.bearspendings.service;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.entity.Bill;

import java.util.List;

/**
 * @author AlexM created on 7/11/19
 */
public interface BillService {
    List<UIBill> allBills();

    Bill addBill(UIBill bill);
}
