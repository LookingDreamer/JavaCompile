package com.cninsure.jobpool.timer.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.timer.TimerJobDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCUser;


/**
 * 工作池 定时器执行回收
 * 
 * @author hxx
 */
@Service
@Deprecated
public class TaskOvertimeJob implements Job {
	@Resource
	private DispatchService dispatService;
	@Resource
	private INSCUserDao userdao;
	@Resource
	TimerJobDao timerdao;
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		
		
		//定时任务名称   主流程id+供应商   作为参数传递
		String ProInstanceId_Prvcode = context.getJobDetail().getKey().getName();
		// 任务执行的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
		
		String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());
		INSCUser user = new INSCUser();
		user.setName("系统用户");
		
		String proInstanceId = ProInstanceId_Prvcode.split("_")[0];
		String prvcode = ProInstanceId_Prvcode.split("_")[1];
		
		if (timerdao.findTimerJob(proInstanceId, prvcode).isRunflag()) {
			
			//得到任务池key
//			PoolKeyModel keyModel = dispatService.getPoolKey4InstanceId(proInstanceId,"main");
			
//			dispatService.recycle(Pool.get(keyModel,proInstanceId,null,prvcode), user);
		}
		// 输出任务执行情况
		System.out.println("任务 : " + ProInstanceId_Prvcode + " 在  "+ jobRunTime + " 执行了 ");
	}
}
