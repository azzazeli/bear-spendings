package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.entity.Bill;
import com.alexm.bearspendings.service.BillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author AlexM created on 7/11/19
 */
@RestController
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public List<Bill> bills() {
        return billService.allBills();
    }

}
