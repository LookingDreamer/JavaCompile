package com.cninsure.system.controller;

import java.util.Date;

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
public class RoleControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testAddRole() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/role/addrole").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("MSM_HEADER", "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Create")			
						.param("MSM_PARAM", "CmdVer=100;Token=test&MSM_PARAM=key=奥迪A6;count=10;off=1")			
						.param("createtime", new Date().toString())			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	@Test
	public void testAddRole2() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/role/addrole").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("createtime", new Date().toString())			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	@Test
	public void testRmoveRole() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/role/rmoverole").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("roldId", "a8bf82c0213311e52f4870d5debfa276")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	@Test
	public void testModifyRole() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/role/updaterole").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("id", "584362f0213611e5e19970ca83844e16")			
						.param("operator", "22222222222222222222")			
						.param("rolecode", "TEST")			
						.param("rolename", "测试测试")			
						.param("branchinnerCode", "111111111")			
						.param("modifytime", new Date().toString())			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	@Test
	public void testListRole() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/role/showrolelist").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("modifytime", new Date().toString())			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
	@Test
	public void testQueryRole() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/role/list").
						contentType(MediaType.APPLICATION_XML).sessionAttr("userId", "1234567890")
						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
						.param("rolename", "管理员")			
						)
						.andDo(MockMvcResultHandlers.print()).andReturn();
		
		Assert.assertNotNull(result.toString());
	}
	
}

