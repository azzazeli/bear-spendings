package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIBill;
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
    public List<UIBill> bills(@RequestParam int page, @RequestParam int size) {
        return billService.allBills(page, size);
    }

    @PostMapping("/add_bill")
    public UIBill addBill(@Valid @RequestBody UIBill uiBill) {
      log.debug("Processing add bill request. uiBill:" + uiBill);
      billService.addBill(uiBill);
      log.debug("Finished to process request");
      return uiBill;
    }

    @GetMapping("/bills/count")
    public ResponseEntity<Long> allBillsCount(){
        return ResponseEntity.ok(billService.allBillsCount());
    }

}
