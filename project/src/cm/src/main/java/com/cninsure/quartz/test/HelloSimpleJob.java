package com.cninsure.quartz.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class HelloSimpleJob {
	public static int j = 1;
	Scheduler sched;
	public HelloSimpleJob(){
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			sched = sf.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

//		for (int i = 0; i < 100; i++) {
//			Thread t = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					HelloSimpleJob j = new HelloSimpleJob();
//					HelloSimpleJob.j++;
//					try {
//						j.execute(String.valueOf(HelloSimpleJob.j), "group","trigger"+HelloSimpleJob.j);
//					} catch (SchedulerException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
//			t.start();
//		}
		
//		for (int i = 0; i < 1; i++) {
//			HelloSimpleJob j = new HelloSimpleJob();
//			try {
//				j.execute(String.valueOf(i), "group","trigger"+i);
//			} catch (SchedulerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	public synchronized void execute(String jobname, String groupname,String triggername)
			throws SchedulerException {
		// 日期格式化
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
		Date startTime = DateBuilder.nextGivenSecondDate(new Date(), 2);
		// job1 将只会执行一次
		JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity(jobname, groupname).build();
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggername, groupname).startAt(startTime).build();
		// 把job1 和 trigger加入计划 . ft:此任务要执行的时间
		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey().getName() + " 将在 : "+ dateFormat.format(ft) + " 时运行.并且重复: "+ trigger.getRepeatCount() + " 次, 每次间隔 "
				+ trigger.getRepeatInterval() / 1000 + " 秒");
	}

}
