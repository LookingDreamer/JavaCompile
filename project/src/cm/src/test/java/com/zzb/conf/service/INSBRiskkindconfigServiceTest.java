package com.zzb.conf.service;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBRiskkindconfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBRiskkindconfigServiceTest {
	
	@Resource
	private INSBRiskkindconfigService s;
	
	public void testInsert(){
		INSBRiskkindconfig kc = new INSBRiskkindconfig();
		kc.setCreatetime(new Date());
		kc.setOperator("test");
		
		s.insert(kc);
	}
}