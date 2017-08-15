package com.zzb.mobile.controller;

import java.io.FileNotFoundException;

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
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.context.WebApplicationContext;

import com.zzb.mobile.service.AppInsuredQuoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class AppInsuredQuoteControllerTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Autowired
	private AppInsuredQuoteService appInsuredQuoteService;
	 static {  
	        try {  
	            Log4jConfigurer.initLogging("classpath:config/log4j.properties");  
	        } catch (FileNotFoundException ex) {  
	            System.err.println("Cannot Initialize log4j");  
	        }  
	    }  

	@Before
	public void setUp() {
		//mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	@Test
	public void testInsurancescheme() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/mobile/insured/quote/lastinsuredrenewabycar").
						contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.content("{\"processinstanceid\":\"8\",\"carNumber\":\"辽AS3B50\",\"carOwer\":\"luo\"}")
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		System.out.println("haoba");
		Assert.assertNotNull(result.toString());
		
	}
	//1
	@Test
	public void test3(){
		appInsuredQuoteService.saveAllPersonInfo("1875352");
	}
	//3
	@Test
	public void test2(){
		appInsuredQuoteService.createHisTableInit("1875352");
	}
}
