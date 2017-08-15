package com.zzb.conf.service;

import java.util.List;
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


public class INSBWorkflowmaintrackServiceTest {
	
	@Resource
	private INSBWorkflowmaintrackService service;

	
	@Test
    public void testGetWorkflowStatusByInstanceId(){
		List<Map<String,String>>  result = service.getWorkflowStatusByInstanceId("5808","5809");
    	
    	System.out.println(result);
    }
}