package com.zzb.lzgapi.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class AppLoginServiceTest {
	@Resource
	private AppLoginService appLoginService;
	
	@Test
	public void testlzgLogin(){
		CommonModelforlzglogin lzgLogin = appLoginService.lzgLogin("ff8080815244c9840000015200000004", null);
		System.out.println(lzgLogin.getStatus());
		System.out.println(lzgLogin.getMessage());
		System.out.println(lzgLogin.getBody());
	}
}
