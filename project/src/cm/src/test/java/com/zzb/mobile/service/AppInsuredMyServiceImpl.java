package com.zzb.mobile.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.PersonalInfoModel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class AppInsuredMyServiceImpl {
    @Resource
private AppInsuredMyService appInsuredMyService;
    
    
	@Test    //ok
	public void getMyPolicies() {
//		CommonModel result1 = appInsuredMyService.getMyPolicies("1234567", "沪B1234");
//		System.out.println("body="+result1.getBody());
//		System.out.println("message="+result1.getMessage());
//		System.out.println("status="+result1.getStatus());
	}

	@Test        //ok
	public void getMyCustomers() {
		
//		CommonModel result2 = appInsuredMyService.getMyCustomers("000","小李");
//		System.out.println("body="+result2.getBody());
//		System.out.println("message="+result2.getMessage());
//		System.out.println("status="+result2.getStatus());
	}

	@Test     //ok
	public void getMyPersonalInfo() {
//		PersonalInfoModel result3 = appInsuredMyService.getMyPersonalInfo("620077558");
//		System.out.println("body="+result3);

	}

}
