package com.cninsure.jobpool;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 任务派发逻辑
 * 
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class PoolServiceTest {
	
	@Resource
	private PoolService service;
	
	
	@Test
	public void dispathGroupByTask(){
		Task task = new Task();
		task.setProInstanceId("24660");
		task.setPrvcode("200537");
		
		service.dispathGroupByTask(task);
	}
	

}
