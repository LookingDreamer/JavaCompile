package com.lzgapi.order.controller;

import net.sf.json.JSONObject;

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

import com.lzgapi.order.model.LzgOrderCancleModel;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class LzgOrderContorllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testGetrisk4Cif() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/cm4lzg/order/cancelorder").
						contentType(MediaType.APPLICATION_XML)
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("params", "{'managerid':'ff8080814fb01bc00000014f00000020','orderno':'41167','requirementid':'ff80808152f79991015311fd21e6003c','token':'ff80808152f79abc00000153000000fd','type':'1'}")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	public static void main(String[] args) {
		LzgOrderCancleModel cancleModel = new LzgOrderCancleModel();
		JSONObject aa = JSONObject.fromObject(cancleModel);
		System.out.println(aa.toString());
	}
}
