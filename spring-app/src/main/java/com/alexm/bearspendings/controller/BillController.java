package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.alexm.bearspendings.bootstrap.DevBootstrap.ALLOWED_ORIGIN;

/**
 * @author AlexM created on 7/11/19
 */
@CrossOrigin( origins = ALLOWED_ORIGIN)
@RestController
@Slf4j
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public List<BillCommand> bills(@RequestParam int page, @RequestParam int size) {
        return billService.allBills(page, size);
    }

    @PostMapping("/add_bill")
    public BillCommand addBill(@Valid @RequestBody BillCommand billCommand) {
      log.debug("Processing add bill request. uiBill:" + billCommand);
      billService.addBill(billCommand);
      log.debug("Finished to process request");
      return billCommand;
    }

    @GetMapping("/bills/count")
    public ResponseEntity<Long> allBillsCount(){
        return ResponseEntity.ok(billService.allBillsCount());
    }

}
