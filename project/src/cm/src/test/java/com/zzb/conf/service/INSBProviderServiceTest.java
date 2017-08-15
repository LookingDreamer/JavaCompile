package com.zzb.conf.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBProviderServiceTest {

	@Resource
	private INSBProviderService service;
	
	@Test
	public void testQueryDataByModifyTime(){
		String modifydate="2015-7-10";
		List<INSBProvider> resultList = service.queryDataByModifyTime(modifydate);
		System.out.println(resultList);
	}
	
}