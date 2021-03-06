package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.BillCommand;
import com.alexm.bearspendings.dto.BillItemCommand;
import com.alexm.bearspendings.exports.BillExporter;
import com.alexm.bearspendings.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(secure = false, controllers = BillController.class)
class BillControllerTest {
    private static final String BILLS_URL = "/api/v1/bills";

    @MockBean
    BillService billService;
    @MockBean
    BillExporter billExporter;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
    private final Long totalRecords = 120L;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setup() {
        given(billService.allBillsCount()).willReturn(totalRecords);
    }

    @Test
    void bills() throws Exception {
        mvc.perform(get(BILLS_URL+"?page=0&size=20"))
                .andExpect(status().isOk());
        verify(billService).allBills(0, 20);
    }

    @Test
    void addBill() throws Exception {
        BillCommand bill = BillCommand.builder()
                .orderDate(LocalDateTime.now())
                .storeId(2L)
                .total(20.0)
                .items(ImmutableSet.of(
                        BillItemCommand.builder().pricePerUnit(22.33).productId(22L).quantity(2.0).build(),
                        BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                ))
                .build();
        mvc.perform(MockMvcRequestBuilders
                .post(BILLS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(bill)))
                .andExpect(status().isOk());
        Mockito.verify(billService).addBill(bill);
    }

    @ParameterizedTest
    @MethodSource("uiBillProvider")
    void validateAddBillInput(BillCommand bill) throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(BILLS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(bill)))
                .andExpect(status().isBadRequest());
    }


    static Stream<BillCommand> uiBillProvider() {
        return Stream.of(
                BillCommand.builder().build(),
                BillCommand.builder().storeId(2L)
                        .items(ImmutableSet.of(
                                BillItemCommand.builder().pricePerUnit(22.33).productId(22L).quantity(2.0).build(),
                                BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                BillCommand.builder().storeId(2L).build(),
                BillCommand.builder().orderDate(LocalDateTime.now())
                        .items(ImmutableSet.of(
                                BillItemCommand.builder().pricePerUnit(22.33).productId(22L).quantity(2.0).build(),
                                BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                BillCommand.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                BillItemCommand.builder().productId(22L).quantity(2.0).build(),
                                BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                BillCommand.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                BillItemCommand.builder().pricePerUnit(22.33).quantity(2.0).build(),
                                BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                BillCommand.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                BillItemCommand.builder().pricePerUnit(22.33).productId(22L).build(),
                                BillItemCommand.builder().pricePerUnit(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build()
        );
    }

    @Test
    void allBillCounts() throws Exception {
        //todo: return count as metadata in bills
        mvc.perform(get(BILLS_URL + "/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(totalRecords.toString())
                );
    }

    @Test
    void exportAll() throws Exception {
        final byte[] bytes = {0, 1};
        given(billExporter.exportAll()).willReturn(new ByteArrayInputStream(bytes));

        mvc.perform(get(BILLS_URL + "/export_all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.ms-excel.sheet.macroEnabled.12"))
                .andExpect(content().bytes(bytes));
    }

}
