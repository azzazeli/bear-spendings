package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.alexm.bearspendings.BearSpendingsApplication.API_URL;

/**
 * @author AlexM created on 7/11/19
 */
@RestController
@RequestMapping(API_URL + "bills")
@Slf4j
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping()
    public List<BillCommand> bills(@RequestParam int page, @RequestParam int size) {
        return billService.allBills(page, size);
    }

    @PostMapping()
    public BillCommand addBill(@Valid @RequestBody BillCommand billCommand) {
      log.debug("Processing add bill request. uiBill:" + billCommand);
      billService.addBill(billCommand);
      log.debug("Finished to process request");
      return billCommand;
    }

    @GetMapping("/count")
    public Long allBillsCount(){
        return billService.allBillsCount();
    }

}