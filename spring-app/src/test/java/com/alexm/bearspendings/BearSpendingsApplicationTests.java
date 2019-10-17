package com.alexm.bearspendings;

import com.alexm.bearspendings.dto.UIBill;
import com.alexm.bearspendings.dto.UIBillItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BearSpendingsApplicationTests {
	private final ObjectMapper mapper = new ObjectMapper();
	private final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

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
		this.mvc.perform(get("/bills?page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value("2"))
		;
	}

	@Transactional
	@Test
	void addNewBill() throws Exception {
		LocalDateTime dateTime = LocalDateTime.now();
		UIBill bill = UIBill.builder()
				.orderDate(dateTime)
				.storeId(1L)
				.items(
						ImmutableSet.of(
								UIBillItem.builder().price(22.9).quantity(1.0).productId(1L).build()
						)
				)
				.build();

		mvc.perform(post("/add_bill")
				.content(writer.writeValueAsString(bill))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		this.mvc.perform(get("/bills?page=0&size=10"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("[0].orderDate").value(dateTime.toString()));
	}

}
