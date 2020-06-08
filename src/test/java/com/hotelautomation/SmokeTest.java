package com.hotelautomation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelautomation.controller.MotionController;
import com.hotelautomation.model.Status;

@SpringBootTest
public class SmokeTest {
	
	protected MockMvc mvc;
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	private MotionController motionController;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void contexLoads() throws Exception {
		assertThat(motionController).isNotNull();
	}
	
	@Test
	public void updateMotion() throws Exception {
		String uri = "/1";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Status updatedStatus = objectMapper.readValue(content, Status.class);
		assertTrue(updatedStatus.equals(Status.ON));
	}

}
