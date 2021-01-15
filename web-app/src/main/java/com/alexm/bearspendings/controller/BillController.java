package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.exports.BillExporter;
import com.alexm.bearspendings.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
    private final BillExporter billExporter;

    public BillController(BillService billService, BillExporter billExporter) {
        this.billService = billService;
        this.billExporter = billExporter;
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

    @GetMapping(
            value = "/export_all",
            produces = "application/vnd.ms-excel.sheet.macroEnabled.12"
    )
    public @ResponseBody byte[] exportAll() throws IOException {
        return IOUtils.toByteArray(billExporter.exportAll());
    }

}
