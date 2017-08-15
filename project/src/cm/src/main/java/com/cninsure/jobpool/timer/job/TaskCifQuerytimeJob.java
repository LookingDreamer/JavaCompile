package com.cninsure.jobpool.timer.job;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.service.RulePlatformQueryService;
import com.zzb.mobile.service.AppInsuredQuoteService;
/**
 * 普通平台查询定时器
 * @author hejie
 *
 */
@Service
public class TaskCifQuerytimeJob implements Job {

	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private RulePlatformQueryService rulePlatformQueryService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//主流程id#子流程id#供应商id#cifqueryjob
		//主流程id#子流程id#供应商id#gzqueryjob
		String jobname = context.getJobDetail().getKey().getName();
		LogUtil.info("平台查询定时器，任务名称="+jobname +"当前时间="+new Date());
		String[] arr = jobname.split("#");
		if(arr.length == 4){
			String taskid = arr[0];
			String subinstanceid = arr[1];
			String inscomcode = arr[2];
			String flag = arr[3];
			LogUtil.info("平台查询定时器,主流程id="+ taskid +"子流程id="+ subinstanceid+"供应商id="+ inscomcode +"=flag="+flag);
			if("cifqueryjob".equals(flag)){//普通平台查询
				appInsuredQuoteService.saveInsuredDateBycifBack(taskid, subinstanceid, inscomcode);
			}else{//规则平台查询
				rulePlatformQueryService.nextTodoRuleQuery(taskid, inscomcode, subinstanceid);
			}
		}else{
			LogUtil.info("平台查询定时器，任务名称="+jobname +"任务名称有误执行不成功！");
		}
	}
}
