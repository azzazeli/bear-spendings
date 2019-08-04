package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author AlexM created on 7/11/19
 */
@CrossOrigin( origins = "http://localhost:4200")
@RestController
@Slf4j
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public List<UIBill> bills() {
        return billService.allBills();
    }

    @PostMapping("add_bill")
    public UIBill addBill(@RequestBody UIBill uiBill) {
      log.debug("Processing add bill request. uiBill:" + uiBill);

      log.debug("Finished to process request");
      return uiBill;
    }

}
