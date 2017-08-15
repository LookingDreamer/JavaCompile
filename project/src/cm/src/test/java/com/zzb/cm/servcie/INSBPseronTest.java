package com.zzb.cm.servcie;

import java.io.FileNotFoundException;
import java.util.List;

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

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.cm.service.INSBPersonService;

import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class INSBPseronTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Autowired
	private INSBPersonService insbPersonService;
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
	@Test
	public void test2(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "name1");
		jsonObject.put("enName", "enName1");
		jsonObject.put("sex", null);
		jsonObject.put("address", "address1");
		jsonObject.put("postCode", "postCode1");
		jsonObject.put("birthday", "2017-01-06");
		jsonObject.put("idCardType", "1");
		jsonObject.put("idCard", "2");
		jsonObject.put("mobile", "mobile1");
		jsonObject.put("email", "email1");
		
		String taskId="1870221";
		String insuranceCompanyId="";
		String personId=UUIDUtils.create();
		String oldPersonId="4d110510d3f811e6d8006b30f290ad35";
		String classType="";
		String property="";
		boolean setDefaultValue =false;
		List<String> list= insbPersonService.getSelectDelId("1874213");
		System.out.println(list.toString());
		//insbPersonService.insertJosn(jsonObject, taskId, insuranceCompanyId, personId, oldPersonId, classType, property, setDefaultValue);
	}
}
