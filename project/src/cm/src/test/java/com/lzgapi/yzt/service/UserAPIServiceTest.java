package com.lzgapi.yzt.service;

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
		"classpath:config/spring-mvc-config.xml",})
public class UserAPIServiceTest {
	
	@Resource
	private UserAPIService s;
	
	
	@Test
	public void testUp2LZGManager(){
		s.up2LZGManager("761be05217d9a6510287104e767ab8a6");
	}
	
}
