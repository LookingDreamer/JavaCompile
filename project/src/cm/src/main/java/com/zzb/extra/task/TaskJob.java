package com.zzb.extra.task;
import com.cninsure.core.utils.LogUtil;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBMarketActivityService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * Created by Heweicheng on 2016/6/7.
 */
@DisallowConcurrentExecution
public class TaskJob implements Job {
    private static final long serialVersionUID = 1L;

    public INSBAccountDetailsService getInsbAccountDetailsService() {
        return insbAccountDetailsService;
    }

    public void setInsbAccountDetailsService(INSBAccountDetailsService insbAccountDetailsService) {
        this.insbAccountDetailsService = insbAccountDetailsService;
    }

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        /* TaskMonitor.clearMap();
         LogUtil.info("Begin refreshRedPackage ！");
         insbAccountDetailsService.refreshRedPackage();
         LogUtil.info("End refreshRedPackage ！");
         LogUtil.info("Begin detector ！");
         insbAccountDetailsService.detector();
         LogUtil.info("End detector ！");*/
    }
}
