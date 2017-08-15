package com.cninsure.quartz.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.jobpool.timer.TimerJobDao;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
@Service
public class SimpleJob implements Job {  
	@Resource
	INSCCodeDao dao ;
    @Override  
    public void execute(JobExecutionContext context)  
            throws JobExecutionException {  
        // job 的名字  
        String taskId = context.getJobDetail().getKey().getName();  
        // 任务执行的时间  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");  
        String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());  
        // 输出任务执行情况 
        INSCCode c=dao.selectById("393fa260770d11e5da4e4f9b3a5b2795");
        System.out.println(c.getCodename());
        System.out.println("任务 : " + taskId + " 在  " +jobRunTime + " 执行了 ");  
}  
}  
