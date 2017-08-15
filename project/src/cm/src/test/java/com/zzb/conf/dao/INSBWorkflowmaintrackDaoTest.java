package com.zzb.conf.dao;

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
public class INSBWorkflowmaintrackDaoTest {
	@Resource 
	private INSBWorkflowmaintrackDao dao;
	
	@Test
	public void Test1(){
		dao.selectByInstanceId4h5("270");
	}
	
	@Test
	public void testSelectAllTrack(){
		List<Map<String,Object>> result= dao.selectAllTrack("111111");
		System.out.println(result.get(0));
	}
}