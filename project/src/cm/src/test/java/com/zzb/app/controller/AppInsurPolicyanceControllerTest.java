package com.zzb.app.controller;

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
public class AppInsurPolicyanceControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testQueryVehicleByType() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/cpmap/access").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("MSM_HEADER", "CmdType=FHBX;CmdModule=CAR;CmdId=NewSearchList;CmdVer=100;Token=test")			
						.param("MSM_PARAM", "key=奥拓;count=3;off=2")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	
	@Test
	public void testAppATM() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/appMsg/msg/online/aa/0001d6b053d811e5d12d53043cee09e7").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8"))
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	/**
	 * 热门车型查询
	 * @throws Exception
	 */
	@Test
	public void testQueryHotKeyList() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/cpmap/access").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("MSM_HEADER", "CmdType=FHBX;CmdModule=CAR;CmdId=HotKeyList")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	
}
