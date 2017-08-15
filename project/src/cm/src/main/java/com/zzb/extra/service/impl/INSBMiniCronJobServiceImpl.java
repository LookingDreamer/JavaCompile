package com.zzb.extra.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.zzb.extra.service.INSBMiniCronJobService;
import com.zzb.extra.task.TaskJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Created by Heweicheng on 2016/6/16.
 */
@Service
public class INSBMiniCronJobServiceImpl implements INSBMiniCronJobService {

    @Resource
    public Scheduler scheduler;
    @Override
    public String beginCronJob() {
        // 创建定时任务
        //Scheduler scheduler = AbsSchedulerManager.getScheduler();
        // 创建定时任务内容
        try{
            int intervalTime = 30000;//
            //String schedulerKey = "RefreshRedPacketsAndCommission";
            String schedulerKey = "refreshExtraTrigger";
            JobDataMap jobMap = new JobDataMap();
            JobDetail job = JobBuilder.newJob(TaskJob.class).withIdentity(schedulerKey).setJobData(jobMap)
                    .build();
            LogUtil.info("======Create refreshExtraTrigger job success！======");
            // 创建触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerKey)
                    .withSchedule(simpleSchedule().withIntervalInSeconds(intervalTime).repeatForever()).build();


            LogUtil.info("======Create refreshExtraTrigger trigger success!======");
            // 组装各个组件job，Trigger
            scheduler.scheduleJob(job, trigger);
            LogUtil.info("======refreshExtraTrigger job begin ！======");
            // 开始定时器
            if(!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch(Exception e){
            e.printStackTrace();
            LogUtil.info("======refreshExtraTrigger error ！======"+e.getMessage());
        }

        return null;
    }
}
