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
public class BearSpendingsApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	WebApplicationContext webAppContext;

	private MockMvc mvc;

	@BeforeEach
	public void setupMvc() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}

	@Test
	public void getAllBills() throws Exception {
		this.mvc.perform(get("/bills"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value("2"))
		;
	}

}
