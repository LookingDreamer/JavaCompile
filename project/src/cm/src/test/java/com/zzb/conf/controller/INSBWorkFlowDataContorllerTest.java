package com.zzb.conf.controller;

import javax.annotation.Resource;

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
public class INSBWorkFlowDataContorllerTest {
	@Resource
	INSBWorkFlowDataContorller  c  ;
	
	@Test
	public void t(){
//		taskid 25752--providerid---200537---lasttype--1--type---underwriting
		c.getUnderwritingType(25752l, "200537", "1");
	}
//
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	
	@Test
	public void testStartTask() throws Exception{

//		for(int i=0;i<100;i++){
			MvcResult result = mockMvc
					.perform(MockMvcRequestBuilders.get("/workflow/startTask").
							contentType(MediaType.APPLICATION_XML)
							.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
							.param("dataFlag", "1")			
							.param("mainInstanceId", "2000")			
							.param("subInstanceId", "3100")			
							.param("providerId", "100613")			
							.param("taskName", "精灵报价")			
							.param("taskStatus", "ready")			
							)
							.andDo(MockMvcResultHandlers.print()).andReturn();
			Assert.assertNotNull(result.toString());
		}
		
		
		
	
//	@Test
//	public void testEndTask() throws Exception{
//
//		MvcResult result = mockMvc
//				.perform(MockMvcRequestBuilders.get("/workflow/endtask").
//						contentType(MediaType.APPLICATION_XML)
//						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
//						.param("dataFlag", "1")			
//						.param("mainInstanceId", "306")			
//						.param("subInstanceId", "307")			
//						.param("providerId", "100613")			
//						.param("taskName", "人工录单")			
//						.param("taskStatus", "completed")			
//						)
//						.andDo(MockMvcResultHandlers.print()).andReturn();
//		
//		Assert.assertNotNull(result.toString());
//	}


}
