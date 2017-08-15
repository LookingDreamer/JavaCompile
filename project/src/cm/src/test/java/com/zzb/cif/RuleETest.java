package com.zzb.cif;
import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cninsure.cssj.JUnit4ClassRunner;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.RULE_engineDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.RULE_engine;
import com.zzb.cm.service.RULE_engineService;

@RunWith(JUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class RuleETest {
	@Resource
	public RULE_engineDao RuleEngineDao;
	@Resource
	public RULE_engineService engineService ;
	@Resource
	public INSBOrderDao dao;
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	public static void main(String[] args) {
		Random r  = new Random();
		System.out.println(r.nextInt(00));
	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
//	public void testQueryVehicleByType() throws Exception {
//		MvcResult result = mockMvc
//				.perform(MockMvcRequestBuilders.post("/systemdata/ruleengine").
//						contentType(MediaType.TEXT_PLAIN)
//						.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
//						.param("id", "5000")			
//						.param("rule_engine_id", "5000111")
//						.param("rule_base_type", "5000")
//						.param("city_id", "5000")
//						.param("company_id", "5000")
//						.param("rule_base_name", "5000")
//						.param("rule_base_postil", "5000")
//						.param("status", "5000")
//						)
//						.andDo(MockMvcResultHandlers.print()).andReturn();
//		
//		Assert.assertNotNull(result.toString());
//	}

//	@Test
	public void test() throws Exception{
//		RULE_engine ruleEngine  = new RULE_engine();
//		ruleEngine.setId("50000");
//		ruleEngine.setRule_engine_id(212121212);
//		ruleEngine.setRule_base_type(50000);
//		ruleEngine.setCity_id(50000);
//		ruleEngine.setCompany_id(50000);
//		ruleEngine.setRule_base_name("50000");
//		ruleEngine.setRule_base_postil("50000");
//		ruleEngine.setStatus(50000);
//		ruleEngine.setLast_updated(new Date());
//		engineService.saveOrudpateRuleEngine(ruleEngine);
		
		
			
	}

}