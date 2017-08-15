package com.zzb.conf.service;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
public class INSBPaychannelmanagerServiceTest {

	@Resource
	private INSBPaychannelmanagerService s;
	
	
	@Test
	public void testDelteDataBylogicId(){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("providerid", "207921");
		paramMap.put("deptid", "1291111068");
		
		s.delteDataBylogicId(paramMap);
	}

}