package com.zzb.mobile.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.mobile.model.CommonModel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class AppRegisteredServiceTest {

	@Resource
	private AppRegisteredService service;
	@Test
	public void testSendVerificationCode(){
		
		CommonModel commonModel = service.sendValidateCode(null, "123456789", null, null);
		System.out.println("Status="+commonModel.getStatus());
		System.out.println("Message="+commonModel.getMessage());
		System.out.println("Body="+commonModel.getBody());
	}
	@Test
	public void testSubmitRegInfo(){
		
		CommonModel commonModel = service.submitRegInfo(
				"{\"phone\":\"123456788\",\"passWord\":\"123456789\",\"readNum\":\"620041138\","
				+ "\"provinceCode\":\"110000\","
				+ "\"cityCode\":\"110100\",\"countyCode\":\"110101\"}");
		System.out.println("Status="+commonModel.getStatus());
		System.out.println("Message="+commonModel.getMessage());
		System.out.println("Body="+commonModel.getBody());
	}
	
}
