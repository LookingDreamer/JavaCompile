package com.zzb.cif;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.cssj.JUnit4ClassRunner;
import com.cninsure.quartz.test.TaskJob;


@RunWith(JUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml",
		"classpath:config/spring-config-quartz.xml",})
public class quartzTest {			
	@Resource
	public Scheduler scheduler;
	@Test
	public void qt() throws SchedulerException{
		for (int i = 0; i < 4; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					JobDataMap jobMap = new JobDataMap();	
					String key ="key"+UUIDUtils.create();
					// 创建定时任务内容
					JobDetail job = JobBuilder.newJob(TaskJob.class).withIdentity(key).setJobData(jobMap)
							.build();
					LogUtil.info("======job！======"+key);
					// 创建触发器
					Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key)
							.withSchedule(simpleSchedule().withIntervalInSeconds(1000 * 60)// 重复间隔
									.withRepeatCount(5)) // 重复次数
							.build();
					try {
						scheduler.scheduleJob(job, trigger);
						if(!scheduler.isStarted()){
							scheduler.start();
						}
						
					} catch (SchedulerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			t.run();
		}
		
		
	}
				
}
