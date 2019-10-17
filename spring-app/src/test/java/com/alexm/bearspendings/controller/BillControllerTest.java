package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
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

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(secure = false, controllers = BillController.class)
class BillControllerTest {

    @MockBean
    BillService billService;
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
        mvc.perform(MockMvcRequestBuilders.get("/bills?page=0&size=20"))
                .andExpect(status().isOk());
        verify(billService).allBills(0, 20);
    }

    @Test
    void addBill() throws Exception {
        UIBill bill = UIBill.builder()
                .orderDate(LocalDateTime.now())
                .storeId(2L)
                .items(ImmutableSet.of(
                        UIBillItem.builder().price(22.33).productId(22L).quantity(2.0).build(),
                        UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                ))
                .build();
        mvc.perform(MockMvcRequestBuilders
                .post("/add_bill")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(bill)))
                .andExpect(status().isOk());
        Mockito.verify(billService).addBill(bill);
    }

    @ParameterizedTest
    @MethodSource("uiBillProvider")
    void validateAddBillInput(UIBill bill) throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/add_bill")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(bill)))
                .andExpect(status().isBadRequest());
    }


    static Stream<UIBill> uiBillProvider() {
        return Stream.of(
                UIBill.builder().build(),
                UIBill.builder().storeId(2L)
                        .items(ImmutableSet.of(
                                UIBillItem.builder().price(22.33).productId(22L).quantity(2.0).build(),
                                UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                UIBill.builder().storeId(2L).build(),
                UIBill.builder().orderDate(LocalDateTime.now())
                        .items(ImmutableSet.of(
                                UIBillItem.builder().price(22.33).productId(22L).quantity(2.0).build(),
                                UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                UIBill.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                UIBillItem.builder().productId(22L).quantity(2.0).build(),
                                UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                UIBill.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                UIBillItem.builder().price(22.33).quantity(2.0).build(),
                                UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build(),
                UIBill.builder()
                        .orderDate(LocalDateTime.now())
                        .storeId(2L)
                        .items(ImmutableSet.of(
                                UIBillItem.builder().price(22.33).productId(22L).build(),
                                UIBillItem.builder().price(233.00).productId(12L).quantity(1.0).build()
                        ))
                        .build()
        );
    }

    @Test
    void allBillCounts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/bills/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(totalRecords.toString())
                );
    }

}