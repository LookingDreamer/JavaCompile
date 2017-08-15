package com.zzb.mobile.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class AppRegisteredControllerTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testSendVerificationCode() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/mobile/registered/sendVerificationCode").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("phoneNo", "1234567871")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	@Test
	public void testSubmitRegInfo() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/mobile/registered/submitRegInfo").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("regInfoJSON", "{\"phone\":\"123456787\",\"passWord\":\"123456787\",\"readNum\":\"620041138\","
								+ "\"provinceCode\":\"110000\","
								+ "\"cityCode\":\"110100\",\"countyCode\":\"110101\"}")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	@Test
	public void testInsurancescheme() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/mobile/insured/quote/insurancescheme").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("plankey", "11111")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	@Test
	public void testUpdatePwd() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/mobile/registered/updateAgentPwd").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("param", "{'agentId':'00007b5053d111e5d12d53043cee09e7','newPwd':'888888','oldPwd':'888888888'}")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
}
