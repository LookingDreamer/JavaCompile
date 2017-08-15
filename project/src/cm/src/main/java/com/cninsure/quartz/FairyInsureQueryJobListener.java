package com.cninsure.quartz;

import com.cninsure.core.utils.LogUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * author: wz
 * date: 2017/3/29.
 */
public class FairyInsureQueryJobListener extends JobListenerSupport {



    @Override
    public String getName() {
        return "MyJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        super.jobToBeExecuted(context);
        LogUtil.info("%s is to be executed.", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        super.jobExecutionVetoed(context);
        getLog().info("% execution is vetoed.", context.getJobDetail().getKey());

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        super.jobWasExecuted(context, jobException);
        getLog().info("%s was executed.", context.getJobDetail().getKey());
    }
}
