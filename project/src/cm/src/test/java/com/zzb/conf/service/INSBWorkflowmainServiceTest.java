package com.zzb.conf.service;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.jobpool.Task;
import com.zzb.conf.entity.INSBWorkflowmain;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBWorkflowmainServiceTest {
	@Resource
	private INSBWorkflowmainService service;
	
	@Test
	public void testAddTask2Poll(){
		Task task = new Task();
		task.setSonProInstanceId("132");
		task.setTaskName("人工承保");
		task.setPrvcode("100112");
		
		service.addTask2Poll(task);
	}

	
	@Test
	public void testDeleteWorkFlowMainData(){
		service.deleteWorkFlowMainData("111111");
	}
	
	@Test
	public void testUpdateWorkFlowMainData(){
		INSBWorkflowmain workflowModel = new INSBWorkflowmain();
		
		workflowModel.setTaskcode("27");
		workflowModel.setTaskid("54022");
		workflowModel.setCreateby("33");
		workflowModel.setCreatetime(new Date());
		workflowModel.setOperator("2222222");
		workflowModel.setTaskstate("Ready");
		
		service.updateWorkFlowMainData(workflowModel,"");
	}
}