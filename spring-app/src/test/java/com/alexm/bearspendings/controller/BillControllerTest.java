package com.alexm.bearspendings.controller;

import com.alexm.bearspendings.service.BillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author AlexM created on 7/11/19
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(secure = false, controllers = BillController.class)
class BillControllerTest {

    @MockBean
    BillService billService;

    @Autowired
    MockMvc mvc;

    @Test
    public void bills() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/bills"))
                .andExpect(status().isOk())
        ;
    }
}