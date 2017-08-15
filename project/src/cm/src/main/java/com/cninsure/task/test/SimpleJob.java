package com.cninsure.task.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cninsure.jobpool.timer.TimerJobDao;

public class SimpleJob implements Job {  
    @Override  
    public void execute(JobExecutionContext context)  
            throws JobExecutionException {  
        // job 的名字  
        String taskId = context.getJobDetail().getKey().getName();  
        // 任务执行的时间  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");  
        String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());  
        // 输出任务执行情况 
//        TimerJobDao  timerDao = new TimerJobDao();
//        timerDao.del(taskId);
        System.out.println("任务 : " + taskId + " 在  " +jobRunTime + " 执行了 ");  
}  
}  
