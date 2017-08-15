package com.cninsure.jobpool.timer;

import org.quartz.SchedulerException;

/**
 * author: wz
 * date: 2017/3/29.
 */
public interface FairyInsureQueryScheduler {
    void scheduleQuery(FairyInsureQuery query) throws SchedulerException;

    void unscheduleQuery(FairyInsureQuery query) throws SchedulerException;
}
