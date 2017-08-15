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
public class AppBaseDataMsgCenterServiceTest {

	@Resource
	private AppBaseDataMsgCenterService service;
	@Test
	public void getAllMessagesTest(){
		
//		CommonModel model = service.getAllMessages("u0001");
//		System.out.println(model.getStatus());
//		System.out.println(model.getMessage());
//		System.out.println(model.getBody());
	}
	@Test
	public void getNotReadMessagesTest(){
//		
//		CommonModel model = service.getNotReadMessages("u0001");
//		System.out.println(model.getStatus());
//		System.out.println(model.getMessage());
//		System.out.println(model.getBody());
	}
	
	@Test
	public void updateMessageStatusTest(){
		
		CommonModel model = service.updateMessageStatus("ea7e9b50659011e534f59430a8717b39","3");
		System.out.println(model.getStatus());
		System.out.println(model.getMessage());
		System.out.println(model.getBody());
	}
	
	@Test
	public void deleteMessageTest(){
		
//		CommonModel model = service.deleteMessage("ea7e9b50659011e534f59430a8717b39");
//		System.out.println(model.getStatus());
//		System.out.println(model.getMessage());
//		System.out.println(model.getBody());
	}
	
}
