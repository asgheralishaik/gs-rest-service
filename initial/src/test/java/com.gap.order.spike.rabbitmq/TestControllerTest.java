

package com.gap.order.spike.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestControllerTest {


	@Autowired
	private WebApplicationContext context;

	private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	List<PlanReceipt> planReceiptList = new ArrayList<>();


	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		objectMapper= new ObjectMapper();
		planReceiptList.clear();
	}


	@RabbitListener(queues = "rrQueue")
    public void handleTNRMessages(PlanReceipt planReceipt) {
		System.out.println(planReceipt);
		planReceiptList.add(planReceipt);
    }

	@Test
	public void shouldSendMessageAndListen() throws Exception {
		PlanReceipt planReceipt = new PlanReceipt();
		planReceipt.setBrandId("1");
		planReceipt.setMarketId("1");
		planReceipt.setChannleId("1");
		planReceipt.setEventType("RR");
		MockHttpServletResponse response = mockMvc.perform(post("/planReceipt")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(planReceipt))).andReturn().getResponse();

		assertThat(response.getStatus(), is(HttpStatus.OK.value()));
		Thread.sleep(5000);
		assertNotNull(planReceiptList.get(0));

	}




}