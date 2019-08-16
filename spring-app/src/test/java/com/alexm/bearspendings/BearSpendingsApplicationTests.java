package com.alexm.bearspendings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BearSpendingsApplicationTests {

	/**
	 * just check if context is ok
	 */
	@Test
	 void contextLoads() {
	}

	@Autowired
	WebApplicationContext webAppContext;

	private MockMvc mvc;

	@BeforeEach
	void setupMvc() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}

	@Test
	 void getAllBills() throws Exception {
		this.mvc.perform(get("/bills"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value("2"))
		;
	}

	@Test
	void addNewBill() {
		//todo: here add one new bill
	}

}
