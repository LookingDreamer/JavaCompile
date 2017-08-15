package com.zzb.conf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.quartz.manager.abs.AbsSchedulerManager;
import com.cninsure.jobpool.timer.job.TaskOvertimeJob4EdiFairy;
import com.zzb.conf.entity.INSBWorkflowsub;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBWorkflowsubServiceTest {

	@Resource
	private INSBWorkflowsubService service;
	
	
	@Test
	public void changeParState(){
		INSBWorkflowsub ParamSubModel = new INSBWorkflowsub();
		ParamSubModel.setMaininstanceid("37714");
		ParamSubModel.setInstanceid("37723");
//		service.changeSubInstance2PusePay(ParamSubModel);
	}
	
	@Test
	public void tesGetDataByMainInstanceId(){
		Map<String,String> result = service.getDataByMainInstanceId("3315");
		System.out.println(result);
	}
	
	@Test
	public void testQueryInstanceIdsByMainInstanceId(){
		String id = "20151019";
		List<String> result = service.queryInstanceIdsByMainInstanceId(id);
		System.out.println(result);
	}
	
	@Test
	public void testDeleteWorkFlowSubData(){
		service.deleteWorkFlowSubData("333");
	}
	
	@Test
	public void testUpdateWorkFlowSubData(){
		INSBWorkflowsub model = new INSBWorkflowsub();
		
		model.setInstanceid("333");
		model.setTaskcode("21");
		model.setTaskid("2222222222");
		model.setTaskstate("2");
		model.setTaskstate("Ready");
		model.setCreatetime(new Date());
		model.setCreateby("22222222");
		model.setOperator("22222222");
		model.setMaininstanceid("1000");
		model.setStarttaskid("000000000000000");
		
		service.updateWorkFlowSubData(model,"zhang");
	}
	
	
	@Test
	public void testJob() {
		Scheduler sched = AbsSchedulerManager.getScheduler();
		long nowDate = new Date().getTime();
		nowDate = nowDate; 
		Date startTime = new Date(nowDate);
		
		
		
		JobKey jobKey = JobKey.jobKey("3456_张");
		try {
			boolean opflage =  sched.deleteJob(jobKey);
			sched.deleteJob(jobKey);
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
		JobDetail job = JobBuilder.newJob(TaskOvertimeJob4EdiFairy.class).withIdentity("3456_张").build();
		System.out.println(job.getKey());
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("3456_张").startAt(startTime).build();
		//加入调度
		try {
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}