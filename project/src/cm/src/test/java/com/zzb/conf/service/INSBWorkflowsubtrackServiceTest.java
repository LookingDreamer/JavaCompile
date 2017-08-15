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
public class INSBWorkflowsubtrackServiceTest {

	@Resource
	private INSBWorkflowsubtrackService sv;
	
	
	@Test
	public void testAddTracks4EndOrClose(){
		Map<String,String> param = new HashMap<String, String>();
		
		param.put("mainTaskId", "41915");
		param.put("subTaskId", "41919");
		param.put("noti", "代理人取消");
		sv.addTracks4EndOrClose(param);
	}
}