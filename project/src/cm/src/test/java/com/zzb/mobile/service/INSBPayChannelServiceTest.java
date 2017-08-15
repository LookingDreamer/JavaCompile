package com.zzb.mobile.service;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPayChannelServiceTest{
	
	@Resource
	private INSBPayChannelService ser;
	
	
	
	@Test
	public void testShowPayChannel(){
		String result = ser.showPayChannel("1241191001", "201141", "1859020", "1859023", "web");
		System.out.println(JSONObject.parse(result));
		System.out.println(JSONObject.toJSONString(JSONObject.parse(result), true));
	}
}
