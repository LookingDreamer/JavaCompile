package com.cninsure.jobpool.timer.job;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.jobpool.timer.SchedulerService;

/**
 * 工作池 定时器执行回收
 * 
 * @author hxx
 */
@Service
public class TaskOvertimeJob4EdiFairy  implements Job {
	@Resource
	private  SchedulerService schedulerService;
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		
		//定时任务名称   主流程id+供应商   作为参数传递
		String ProInstanceId_Prvcode = context.getJobDetail().getKey().getName();
		//超时任务处理逻辑
		schedulerService.dealOverTimeJob(ProInstanceId_Prvcode);
	}
	
	
	public static void main(String args[]){
//		Date startTime = new Date();
//		System.out.println(DateUtil.toDateTimeString(startTime));
		String taskName = "EDI核保暂存";
		switch(taskName){
			case "精灵核保暂存":
			case "精灵自动核保":	
				System.out.println("task"+taskName);
				break;
			case "EDI核保暂存":
			case "EDI自动核保":
				System.out.println("task"+taskName);
				break;
			default:
				break;
		}
	}
}
