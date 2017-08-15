package com.cninsure.jobpool.timer;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import com.cninsure.core.quartz.manager.abs.AbsSchedulerManager;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.timer.job.TaskOvertimeJob;

/**
 * 工作池简单定时器 调度
 * 
 * 1.启动调度 2.存储调度信息
 * 
 * @author hxx
 *
 */
@Service
@Deprecated
public class SchedulerTimer {

	
	
	/**
	 * 超时时间 秒
	 */
	public int overtime = 30;
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";  

	/**
	 * 启动 默认定时器 TaskOvertimeJob
	 * 
	 * @throws SchedulerException
	 */
	public void start(Task task) throws SchedulerException {
		start(task, TaskOvertimeJob.class, overtime);
	}

	/**
	 * 启动定时器
	 * 
	 * @throws SchedulerException
	 */
	public void start(Task task, Class<? extends Job> jobClass, int overtime)
			throws SchedulerException {
		
		
		//定时任务rides信息
		TimerJob timerJob = new TimerJob();
		timerJob.setTask(task);
		timerJob.setStartDate(String.valueOf(new Date().getTime()));
		
		Date excDate = DateBuilder.nextGivenSecondDate(new Date(timerJob.getStartDate()),overtime);
		
		timerJob.setExcDate(String.valueOf(excDate.getTime()));
		timerJob.setJobClass(jobClass);
		LogUtil.info("======定时任务中 任务信息======"+timerJob.toString());
		start(timerJob);
	}

	/**
	 * 启动定时器
	 * 
	 * @throws SchedulerException
	 */
	public void start(TimerJob timerJob) throws SchedulerException {
		
		Scheduler sched = AbsSchedulerManager.getScheduler();
		// 日期格式化
		Date startTime = DateBuilder.nextGivenSecondDate(new Date(), 30);
		// job1 将只会执行一次
		
		String mainInstanceIdStr = timerJob.getTask().getProInstanceId();
		String providerId = timerJob.getTask().getPrvcode();
		//任务名称
		String jobName = mainInstanceIdStr+"_"+providerId;
		
		JobDetail job = JobBuilder.newJob(timerJob.getJobClass()).withIdentity(jobName,JOB_GROUP_NAME).build();
		
		//创建并配置一个触发器 30秒触发
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName,TRIGGER_GROUP_NAME).startAt(startTime).build();
		
		//加入调度
		sched.scheduleJob(job, trigger);
		
		//应该把当前 定时任务删除  TODO
		TimerJobDao jobDao = new TimerJobDao();
		jobDao.saveorupdate(timerJob);
	}
	
	/** 
	 * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
	 * @param jobName 
	 */  
	public  void removeJob(String jobName) {  
	    try {  
	        Scheduler sched = AbsSchedulerManager.getScheduler();  
	        sched.pauseTrigger(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器  
	        sched.unscheduleJob(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器  
	        sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));// 删除任务  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	} 

}
