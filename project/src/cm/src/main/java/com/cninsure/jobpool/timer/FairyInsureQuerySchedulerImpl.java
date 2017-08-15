package com.cninsure.jobpool.timer;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.jobpool.timer.job.FairyInsureQueryJob;
import com.cninsure.quartz.FairyInsureQueryJobListener;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

/**
 * author: wz
 * date: 2017/3/29.
 */
@Service
public class FairyInsureQuerySchedulerImpl implements FairyInsureQueryScheduler {

    private static final String DEFAULT_GROUP = "fairy_insure_query";
    private static final String DEFAULT_GROUP_RETRY = "fairy_insure_query_retry";

    @Resource
    Scheduler scheduler;

    @Override
    public void scheduleQuery(FairyInsureQuery query) throws SchedulerException {
        JobKey jobKey = new JobKey(buildJobName(query), DEFAULT_GROUP);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }

        LogUtil.info("精灵承保查询 - 新增加job. taskId: %s, prvId: %s", query.getTaskId(), query.getProviderId());

        TriggerKey triggerKey = new TriggerKey(buildTriggerName(query), DEFAULT_GROUP);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("taskId", query.getTaskId());
        jobDataMap.put("prvId", query.getProviderId());

        JobDetail jobDetail = JobBuilder.newJob(FairyInsureQueryJob.class)
                .setJobData(jobDataMap)
                .withIdentity(jobKey)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10))
                .startNow()
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleQuery(FairyInsureQuery query, int retryAfterSeconds) {

    }


    @Override
    public void unscheduleQuery(FairyInsureQuery query) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(buildTriggerName(query), DEFAULT_GROUP);
        LogUtil.info("精灵承保查询JOB - 移除job. taskId: %s, privId: %s", query.getTaskId(), query.getProviderId());
        scheduler.unscheduleJob(triggerKey);
    }

    public void unscheduleQuery(TriggerKey triggerKey) throws SchedulerException {
        scheduler.unscheduleJob(triggerKey);
    }

    private String buildTriggerName(FairyInsureQuery query) {
        return "trigger_" + query.getTaskId();
    }

    private String buildJobName(FairyInsureQuery query) {
        return "job_" + query.getTaskId();
    }
}
