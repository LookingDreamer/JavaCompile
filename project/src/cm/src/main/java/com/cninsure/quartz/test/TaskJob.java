package com.cninsure.quartz.test;

import com.cninsure.core.utils.LogUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
public class TaskJob extends QuartzJobBean{
	private static final long serialVersionUID = 1L;
	private SimpleService simpleService;  
    
    public void setSimpleService(SimpleService simpleService) {  
        this.simpleService = simpleService;  
    }  
  
    @Override  
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {

        LogUtil.info("开始执行定时器方法executeInternal！");
        simpleService.expiredClose();
    }
}