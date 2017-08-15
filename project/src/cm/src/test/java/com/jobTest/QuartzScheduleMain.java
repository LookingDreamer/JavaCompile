package com.jobTest;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

public class QuartzScheduleMain {
	/**
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {

		Scheduler sched = QuartzScheduleMgr.getInstanceScheduler();
		// computer a time that is on the next round minute
		long nowDate = new Date().getTime();
		nowDate = nowDate + 30000;
		Date runTime = new Date(nowDate);
		JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + runTime);
		sched.start();
		sched.pauseTrigger(trigger.getKey());// 停止触发器
		sched.unscheduleJob(trigger.getKey());// 移除触发器
		sched.deleteJob(job.getKey());// 删除任务
		Trigger trigger2 = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		System.out.println(job.getKey());
		System.out.println(trigger2.getKey());
		sched.scheduleJob(job, trigger2);
	}

	public static void main(String[] args) throws Exception {

		QuartzScheduleMain example = new QuartzScheduleMain();
		example.run();

	}
}
