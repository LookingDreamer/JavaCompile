package com.zzb.conf.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBRiskkind;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBRiskkindServiceTest {
	
	@Resource
	private INSBRiskkindService service;
	
	
	@Test
	public void testSelectByModifyDate(){
		List<INSBRiskkind> list = service.selectByModifyDate("2015-7-23");
		System.out.println(list);
	}
}
