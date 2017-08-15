package com.cninsure.jobpool.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.timer.job.IFairyInsureQuery;
import com.cninsure.jobpool.timer.job.TaskCifQuerytimeJob;
import com.cninsure.jobpool.timer.job.TaskOvertimeJob4EdiFairy;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.TaskConst;
import com.common.WorkFlowUtil;
import com.common.WorkflowFeedbackUtil;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.INSBLoopunderwritingDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.entity.INSBLoopunderwriting;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRulequerycarinfo;
import com.zzb.cm.service.INSBManualPushFlowService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.LoopUnderWritingService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.ProviderListBean;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.service.AppInsuredQuoteService;

import net.sf.json.JSONObject;

@Repository
public class SchedulerServiceimpl implements SchedulerService{
	private static String TIMER_TYPE = "";
	private static String TIME = "";
	static {
		// 读取相关的配置  
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		TIMER_TYPE = resourceBundle.getString("scheduler.proccessType");
		TIME = resourceBundle.getString("scheduler.timer");
	}
	@Resource
	public  Scheduler sched;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
    private INSBAgentDao insbAgentDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;// 报价信息表
	@Resource
	private AppInsuredQuoteService appInsuredQuoteServiceImpl;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private InterFaceService interFaceService;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSCDeptService inscDeptservice;
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	private INSBLoopunderwritingDao loopunderwritingDao;
	@Resource
	private LoopUnderWritingService loopUnderWritingService;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBManualPushFlowService insbManualPushFlowService;
	@Resource
	private IFairyInsureQuery approvedFairyInsureQuery;

	/**
	 * 删除历史定时任务并重新启动新任务
	 * @param jobName
	 * @param taskName
	 * @param startTime
	 * @param providerId
	 * @param timeout
	 * @throws SchedulerException 
	 */
	public void deleteHistoryJobAndStartNewJob(String jobName,String taskName,Date startTime,String providerId,String timeout, String dealClass) throws SchedulerException{
		if("1".equals(TIMER_TYPE)){
			this.schedulerProccess(jobName, taskName, startTime, providerId, timeout, dealClass);
		}else{//redis超时处理加入定时任务逻辑
			LogUtil.info(jobName+"=jobName,redis超时处理加入定时任务逻辑.时间="+timeout);
			CMRedisClient.getInstance().set(6, "Scheduler", jobName, providerId,Integer.valueOf(timeout)/1000);
		}
	}

	public void deleteJob(String jobName) {
		CMRedisClient.getInstance().del(6, "Scheduler", jobName);
	}

	/**
	 * 定时任务处理轮询任务
	 * @param key
	 * @param taskName
	 * @param startTime
	 * @param value
	 * @param timeout
	 * @param dealClass 处理超时任务的job类
	 */
	@Override
	public void dealLoopOverTime(String key, String taskName, Date startTime, String value, String timeout,
			String dealClass, int totalCount) throws SchedulerException {
		if(StringUtil.isEmpty(taskName)) {
			LogUtil.info("dealLoopOverTime, 参数key=%s, taskName=%s  不能为空!", key, taskName);
			return;
		}
		LogUtil.info("dealLoopOverTime,%s, 参数key=%s, taskName=%s  定时轮询任务执行时间是%s!", startTime, key, taskName, DateUtil.toDisplayDateTime(startTime));
		switch(taskName){
			case "核保轮询":
				CMRedisClient.getInstance().set(6, "Scheduler", key, value,Integer.valueOf(timeout)/1000);
				break;
			default:
				break;
		}
	}
	
	//quarts处理加入定时任务逻辑
	private void schedulerProccess(String jobName,String taskName,Date startTime,String providerId,String timeout, String dealClass) throws SchedulerException{
		LogUtil.info("清除历史定时任务===%s==job=%s===", taskName, jobName);
		JobKey jobKey = new JobKey(jobName);
		try {
			sched.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		//开启任务前清除历史定时任务
		LogUtil.info("开始设定定时任务===%s==%s超时设置：providerId=%s ,timeout=%s", jobName, startTime, providerId, timeout);
		JobDetail job = null;
		if("TaskOvertimeJob4EdiFairy".equals(dealClass)){
			job = JobBuilder.newJob(TaskOvertimeJob4EdiFairy.class).withIdentity(jobName).build();
		}else if("TaskCifQuerytimeJob".equals(dealClass)){
			job = JobBuilder.newJob(TaskCifQuerytimeJob.class).withIdentity(jobName).build();
		}
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName).startAt(startTime).build();
		//加入调度
		sched.scheduleJob(job, trigger);
	}

	/**
	 * 超时任务处理逻辑
	 * @param ProInstanceId_taskName
	 * @throws SchedulerException
	 */
	public void dealOverTimeJob(String ProInstanceId_taskName){
		try{
			LogUtil.info("======== 定时任务---开始执行---jobid="+ProInstanceId_taskName);
			String[] taskNameParams = ProInstanceId_taskName.split("_");
			String subInstanceId = (taskNameParams[0]).replaceAll("Scheduler:", "");
			String taskName = taskNameParams[1];
			String redisOverTimeKey="";
			int redisValue=0;
			INSBWorkflowsub workflowsub = insbWorkflowsubDao.selectByInstanceId(subInstanceId);

			switch (taskName) {
			
			case "承保查询":
				approvedFairyInsureQuery.execute();
				break;
			
			case "同步节点":
				insbManualPushFlowService.autosyncWorkflowStatus("auto");
				approvedFairyInsureQuery.timerForFairyInsureQuery("setFairyInsureQuery_承保查询", TIME);//额外处理一个逻辑，加一个每晚八点执行一次待支付承保查询的定时任务触发器
				break;
			
			case "核保轮询":
				if(null != workflowsub && "38".equals(workflowsub.getTaskcode())){
					//subInstanceId_taskName_inscomcode_loopid_totalCount
					Map<String, Object> contextParam = new HashMap<String, Object>();
					if("0".equals(taskNameParams[4])){//最后一次查询结束
						//更新轮询结果
						Map<String, Object> submodel = insbWorkflowsubDao.selectModelInfoByInstanceId(subInstanceId);
						if (submodel != null) {
							String inscomcode = String.valueOf(submodel.get("inscomcode"));
							if (StringUtil.isNotEmpty(inscomcode)) {
								INSBLoopunderwriting queryloopunderwriting = new INSBLoopunderwriting();
								queryloopunderwriting.setTaskid(workflowsub.getMaininstanceid());
								queryloopunderwriting.setInscomcode(inscomcode);
								INSBLoopunderwriting loopunderwriting = loopunderwritingDao.selectOne(queryloopunderwriting);
								if (loopunderwriting != null && "start".equals(loopunderwriting.getLoopstatus())) {
									insbWorkflowmaintrackService.updateLoopUnderWritingDetail(workflowsub.getMaininstanceid(), subInstanceId, inscomcode, "stop", "轮询完成");
									//interFaceService.TaskResultWriteBack(workflowsub.getMaininstanceid(), inscomcode, workflowsub.getOperator(), "false", "核保查询", "B1", "3", taskName, subInstanceId);
								}
							} else {
								LogUtil.error("inscomcode为空%s",subInstanceId);
							}
						}
						LogUtil.info("SchedulerServiceimpl定时任务处理核保轮询(卡住或超时), taskid=" + workflowsub.getMaininstanceid());
						WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "38", "Completed", taskName, "轮询完成", "admin");
						insbWorkflowmaintrackService.loopWorkFlowToNext("", subInstanceId, "", "1", null);
						break;
					}else{
						contextParam.put("nextloop", Integer.parseInt(taskNameParams[4])-1);
						// 获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
						List<String> underwritingList = insbOrderDao.getUnderwritingTrackStr(subInstanceId);
						contextParam.put("underwritingList", underwritingList);
						contextParam.put("maininstanceId",workflowsub.getMaininstanceid());
						contextParam.put("subInstanceId",workflowsub.getInstanceid());
						contextParam.put("inscomcode",taskNameParams[2]);
				        contextParam.put("loopid",taskNameParams[3]);
						loopUnderWritingService.executeService(contextParam);
					}
				} else {
					LogUtil.info("SchedulerServiceimpl处理核保轮询异常或轮询完成, taskid=" + (workflowsub!=null?workflowsub.getMaininstanceid():"null") +
							", taskcode=" + (workflowsub!=null?workflowsub.getTaskcode():""));
				}
				break;

			case "精灵报价":
				LogUtil.info("========定时任务---精灵报价---jobid="+ProInstanceId_taskName);
	
				redisOverTimeKey = subInstanceId+"_quote_robot";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---精灵报价---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵报价---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");

					if(null!=workflowsub){//增加判断是否存在 了其他备用平台查询回来的平台信息
						WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "4", "Completed", taskName, WorkflowFeedbackUtil.quote_timeout, "admin");
						INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
						Map<String, Object> datamap = new HashMap<String, Object>();
						datamap = appInsuredQuoteServiceImpl.getPriceParamWay(datamap, workflowsub.getMaininstanceid(), quote.getInscomcode(), "2");
						WorkFlowUtil.completeTaskWorkflowRecheck("1",subInstanceId, "admin",taskName,datamap);
						
						INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
						insbRulequerycarinfo.setTaskid(workflowsub.getMaininstanceid());
						List<INSBRulequerycarinfo> list = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo);
						String status = String.valueOf(CMRedisClient.getInstance().get(Constants.CM_GLOBAL, workflowsub.getMaininstanceid() + "startedBakQuery"));
						if(list == null || list.isEmpty() || !"guizequery".equals(list.get(0).getOperator()) || (!StringUtil.isEmpty(status) && status.equals("0"))){//再次重启备用平台信息查询
							int isoverAutoquote = 0;
							INSBWorkflowsub param = new INSBWorkflowsub();
							param.setMaininstanceid(workflowsub.getMaininstanceid());
							List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectList(param);
							for(INSBWorkflowsub sub:subs){
								if(TaskConst.QUOTING_3.equals(sub.getTaskcode())||TaskConst.QUOTING_4.equals(sub.getTaskcode())){
									isoverAutoquote++;
									LogUtil.info("taskid=" + sub.getMaininstanceid() + ",subtaskid=" + subInstanceId + ",还有报价未返回");
								}
							}
							if(isoverAutoquote <= 1){ // 所有报价已完成并且没有平台信息
								LogUtil.info("taskid=" + workflowsub.getMaininstanceid() + ",subtaskid=" + subInstanceId + ",CM中未查到平台信息,启动备用平台查询");
								String tmpJobName = subInstanceId + "_平台查询_" + workflowsub.getMaininstanceid();
								long nowDate = new Date().getTime();
								nowDate = nowDate + 1000; // 1秒超时,相当于立即启动备用平台查询
								Date startTime = new Date(nowDate);
								try {
									deleteHistoryJobAndStartNewJob(tmpJobName, taskName, startTime, "", "1000","TaskOvertimeJob4EdiFairy");
									LogUtil.info("taskid=" + workflowsub.getMaininstanceid() + ",subtaskid=" + subInstanceId + ","+ isoverAutoquote+"=isoverAutoquote,精灵报价超时之后平台查询加入调度成功,jobname=" + tmpJobName + ",执行时间=" + DateUtil.toDateTimeString(startTime));
								} catch (SchedulerException e) {
									LogUtil.info("taskid=" + workflowsub.getMaininstanceid() + ",subtaskid=" + subInstanceId + ",精灵报价超时之后平台查询加入调度失败,jobname=" + tmpJobName);
									e.printStackTrace();
								}
							}
						}
					}
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵报价---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				
				break;
			case "EDI报价":	
				LogUtil.info("========定时任务---EDI报价---jobid="+ProInstanceId_taskName);
				redisOverTimeKey = subInstanceId+"_quote_edi";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---EDI报价---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI报价---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "3", "Completed", taskName, WorkflowFeedbackUtil.quote_timeout, "admin");
					
					INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
					Map<String, Object> datamap = new HashMap<String, Object>();
					datamap = appInsuredQuoteServiceImpl.getPriceParamWay(datamap, workflowsub.getMaininstanceid(), quote.getInscomcode(), "1");
					WorkFlowUtil.completeTaskWorkflowRecheck("1",subInstanceId, "admin",taskName,datamap);
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI报价---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "精灵核保暂存":
				LogUtil.info("========定时任务---精灵核保暂存---jobid="+ProInstanceId_taskName);
	
				redisOverTimeKey = subInstanceId+"_insure_robot";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---精灵核保暂存---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵核保暂存---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "17", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,null);
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵核保暂存---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				
				break;
			case "精灵自动核保":	
				LogUtil.info("========定时任务---精灵核保---jobid="+ProInstanceId_taskName);
	
				redisOverTimeKey = subInstanceId+"_autoinsure_robot";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---精灵自动核保---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵自动核保---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "41", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,null);
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵自动核保---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "EDI核保暂存":
				LogUtil.info("========定时任务---EDI核保暂存---jobid="+ProInstanceId_taskName);
				redisOverTimeKey = subInstanceId+"_insure_edi";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---EDI核保暂存---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI核保暂存---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "16", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					try {
						INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
						INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectById(quote.getQuotetotalinfoid());
						String result = workflowmainService.getContracthbType(quotetotalinfo.getTaskid(), quote.getInscomcode(), "", "underwritingType");
						JSONObject jsonObject = JSONObject.fromObject(result);
						LogUtil.info("=====EDI核保暂存超时结束=====getContracthbType="+result);
						WorkFlowUtil.completeTaskWorkflow(jsonObject.optString("quotecode"),subInstanceId, "admin",taskName,null);
					} catch (Exception e) {
						WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,null);
					}
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI核保暂存---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "EDI自动核保":	
				LogUtil.info("========定时任务---EDI自动核保---jobid="+ProInstanceId_taskName);
				redisOverTimeKey = subInstanceId+"_autoinsure_edi";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---EDI自动核保---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI自动核保---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "40", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					try {
						INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
						INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectById(quote.getQuotetotalinfoid());
						String result = workflowmainService.getContracthbType(quotetotalinfo.getTaskid(), quote.getInscomcode(), "", "underwritingType");
						JSONObject jsonObject = JSONObject.fromObject(result);
						LogUtil.info("=====EDI自动核保超时结束=====getContracthbType="+result);
						WorkFlowUtil.completeTaskWorkflow(jsonObject.optString("quotecode"),subInstanceId, "admin",taskName,null);
					} catch (Exception e) {
						WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,null);
					}
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI自动核保---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "精灵承保":	
				LogUtil.info("========定时任务---精灵承保---jobid="+ProInstanceId_taskName);
				redisOverTimeKey = subInstanceId+"_approved_robot";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---精灵承保---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵承保---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(subInstanceId, null, "26", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectSubModelByMainInstanceId(subInstanceId);
					String acceptway = "3";//默认到人工承保
					for(INSBWorkflowsub sub:subs){
						if("33".equals(sub.getTaskcode())){
							INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(sub.getInstanceid());
							acceptway = JSONObject.fromObject(workflowmainService.getContractcbType(subInstanceId, quote.getInscomcode(), "2", "contract")).getString("quotecode");
						}
					}
					Map<String, String> datamap = new HashMap<String, String>();
					datamap.put("acceptway", acceptway);
					WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,datamap);
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---精灵承保---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "EDI承保":	
				LogUtil.info("========定时任务---EDI承保---jobid="+ProInstanceId_taskName);
				redisOverTimeKey = subInstanceId+"_approved_edi";
				redisValue = (int) CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, redisOverTimeKey);
				LogUtil.info("========定时任务---EDI承保---jobid="+ProInstanceId_taskName+"---全局锁---redisOverTimeKey="+redisOverTimeKey+"---redisValue="+redisValue);
				if(redisValue==3){
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI承保---jobid="+ProInstanceId_taskName+"---删除后全局锁---redisOverTimeKey="+redisOverTimeKey+"---删除后全局锁---redisValue="+redisValue+"---通知工作流");
					WorkflowFeedbackUtil.setWorkflowFeedback(subInstanceId, null, "25", "Completed", taskName, WorkflowFeedbackUtil.underWriting_timeout, "admin");
					List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectSubModelByMainInstanceId(subInstanceId);
					String acceptway = "3";//默认到人工承保
					for(INSBWorkflowsub sub:subs){
						if("33".equals(sub.getTaskcode())){
							INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(sub.getInstanceid());
							acceptway = JSONObject.fromObject(workflowmainService.getContractcbType(subInstanceId, quote.getInscomcode(), "1", "contract")).getString("quotecode");
						}
					}
					Map<String, String> datamap = new HashMap<String, String>();
					datamap.put("acceptway", acceptway);
					WorkFlowUtil.completeTaskWorkflow("1",subInstanceId, "admin",taskName,datamap);
				}else{
					CMRedisClient.getInstance().del(Constants.CM_GLOBAL, redisOverTimeKey);
					LogUtil.info("========定时任务---EDI承保---jobid="+ProInstanceId_taskName+"---未通知工作流");
				}
				break;
			case "平台查询":
				// 这里是第一次40秒超时,不管是否还有精灵报价未返回,都需要启动备用平台查询,并且保证只启动一次
				// 修改判断如果有平台信息存在则不用启动备用平台查询 20161206
				String taskid = ProInstanceId_taskName.split("_")[2];
				INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(taskid);
				INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
				INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
				insbRulequerycarinfo.setTaskid(quotetotalinfo.getTaskid());
				List<INSBRulequerycarinfo> list = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo);
				String status = String.valueOf(CMRedisClient.getInstance().get(Constants.CM_GLOBAL, taskid + "startedBakQuery"));
				if(list == null || list.isEmpty() || !"guizequery".equals(list.get(0).getOperator()) || "0".equals(status)){//启备用平台信息查询
					INSBAgent insbAgent = insbAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
					INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
					//还是需要考虑协议能力的
					SearchProviderModel model = new SearchProviderModel();
					model.setProcessinstanceid(quotetotalinfo.getTaskid());
					model.setAgentid(insbAgent.getId());
					model.setChannel("");
					model.setCity(quotetotalinfo.getInscitycode());
					model.setProvince(quotetotalinfo.getInsprovincecode());
					CommonModel commonModel = appInsuredQuoteServiceImpl.searchProvider(model);
					LogUtil.info("subtaskid=" + subInstanceId+"taskid=" + quotetotalinfo.getTaskid() + ",查询备用平台能力结果：" + (commonModel != null ? commonModel.getStatus() : "-fail"));
					if ("fail".equals(commonModel.getStatus())) {// 查询备用平台信息能力失败
						LogUtil.info("%s========查询协议里平台能力公司失败=%s,status=%s", taskid, ProInstanceId_taskName, commonModel.getStatus());
					}
					//不考虑是否平台查询公司在代理人所属子机构的协议里，只考虑机构平台的平台信息查询能力改造(还是需要考虑协议能力的)
					List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoService.getQuoteinfosByInsbQuotetotalinfoid(quotetotalinfo.getId());
					long nowDate = new Date().getTime();
					String jobName = subInstanceId + "_备用平台查询_"+taskid;
					int i = CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, quotetotalinfo.getTaskid() + "startedBakQuery");
					if(i == 1)
						CMRedisClient.getInstance().expire(Constants.CM_GLOBAL, quotetotalinfo.getTaskid() + "startedBakQuery", 600);

					List<ProviderListBean> providerListBeans = (List<ProviderListBean>) commonModel.getBody();
					LogUtil.info("taskid=" + quotetotalinfo.getTaskid() + ",subtaskid=" + subInstanceId + ",开始启动备用平台查询,i=" + i);
					int queryNum = 0;
					List<String> providers = new ArrayList<String>();
					for (ProviderListBean bean : providerListBeans) {
						if ("0".equals(bean.getReservedPlatformresult())) { // 有平台查询能力
							if (queryNum >= 5)
								break;
						}else{
							continue;
						}
						boolean isQuoteInscom = false;
						for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
							if (quoteinfo.getInscomcode().startsWith(bean.getProId())) {
								isQuoteInscom = true;
							}
						}
						// 包含在报价中,不启动平台查询
						if (isQuoteInscom)
							continue;
						// 配置有问题或取配置的程序有问题,通过这种方式去重
						if (providers.contains(bean.getProId().substring(0, 4)))
							continue;
						providers.add(bean.getProId().substring(0, 4));
						if(i == 1){ // 仅第一次时启动备用平台查询
							taskthreadPool4workflow.execute(new Runnable() {
								@Override
								public void run() {
									try {
										if (null != bean.getBranchProBeans() && bean.getBranchProBeans().size() > 0) {
											LogUtil.info("subtaskid=" + subInstanceId+"taskid=" + quotetotalinfo.getTaskid() + ",providerid=" + bean.getProId() + ",启动备用平台查询");
											if(null == insbQuoteinfo){
												interFaceService.goRobot(quotetotalinfo.getTaskid(),  bean.getBranchProBeans().get(0).getDeptId(),
														"admin", "quote@reserved@" + insbQuoteinfos.get(0).getInscomcode(), insbQuoteinfos.get(0).getWorkflowinstanceid());
											}else{
												interFaceService.goRobot(quotetotalinfo.getTaskid(),  bean.getBranchProBeans().get(0).getDeptId(),
														"admin", "quote@reserved@" + insbQuoteinfo.getInscomcode(), insbQuoteinfo.getWorkflowinstanceid());
											}
										}
									} catch (Exception e) {
										LogUtil.info("subtaskid=" + subInstanceId+"taskid=" + quotetotalinfo.getTaskid() + ",providerid=" + bean.getProId() + ",启动备用平台查询异常");
										e.printStackTrace();
									}
								}
							});
						}
						queryNum++;
					}
					int timeout = 40000;
					if (queryNum > 0){
						CMRedisClient.getInstance().set(Constants.CM_GLOBAL, quotetotalinfo.getTaskid() + "startedBakQuery", "1", 20 * 60);
						nowDate = nowDate + timeout;
					}else{//如果没有启动备用则再次查询机构所属平台是否有平台能力，有则重新尝试启用备用平台查询
						INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbAgent.getDeptid());
						List<INSBAutoconfigshow> configs = insbAutoconfigshowService.getOneAbilityByDeptIdQuotype(null!=deptPlatform?deptPlatform.getComcode():insbAgent.getDeptid().substring(0,5)+"00000", "05", "02");
						providers.clear();
						for (INSBAutoconfigshow bean : configs) {
							// 配置有问题或取配置的程序有问题,通过这种方式去重
							if (providers.contains(bean.getProviderid().substring(0, 4)))
								continue;
							providers.add(bean.getProviderid().substring(0, 4));
							if(i == 1){ // 仅第一次时启动备用平台查询
								taskthreadPool4workflow.execute(new Runnable() {
									@Override
									public void run() {
										try {
											if (StringUtil.isNotEmpty(bean.getProviderid())) {
												LogUtil.info("subtaskid=" + subInstanceId+"taskid=" + quotetotalinfo.getTaskid() + ",providerid=" + bean.getProviderid() + ",启动备用平台查询2");
												if(null == insbQuoteinfo){
													interFaceService.goRobot(quotetotalinfo.getTaskid(),  bean.getProviderid(),
															"admin", "quote@reserved@" + insbQuoteinfos.get(0).getInscomcode(), insbQuoteinfos.get(0).getWorkflowinstanceid());
												}else{
													interFaceService.goRobot(quotetotalinfo.getTaskid(),  bean.getProviderid(),
															"admin", "quote@reserved@" + insbQuoteinfo.getInscomcode(), insbQuoteinfo.getWorkflowinstanceid());
												}
											}
										} catch (Exception e) {
											LogUtil.info("subtaskid=" + subInstanceId+"taskid=" + quotetotalinfo.getTaskid() + ",providerid=" + bean.getProviderid() + ",启动备用平台查询异常2");
											e.printStackTrace();
										}
									}
								});
							}
							queryNum++;
						}
						LogUtil.info("subtaskid=" + subInstanceId+providers.toString()+"=providers,启动备用平台查询2taskid=" + quotetotalinfo.getTaskid() + ",queryNum=" + queryNum + ",i=" + i);
						if (queryNum > 0){
							CMRedisClient.getInstance().set(Constants.CM_GLOBAL, quotetotalinfo.getTaskid() + "startedBakQuery", "1", 20 * 60);
							nowDate = nowDate + timeout;
						}else{
							timeout = 1000;
							CMRedisClient.getInstance().set(Constants.CM_GLOBAL, quotetotalinfo.getTaskid() + "startedBakQuery", "0", 20 * 60);
							nowDate = nowDate + timeout;
						}
					}
					Date startTime = new Date(nowDate);
					// 开启任务前清除历史定时任务
					try {
						deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, "", ""+timeout,"TaskOvertimeJob4EdiFairy");
						LogUtil.info("taskid=" + quotetotalinfo.getTaskid() + ",subtaskid=" + subInstanceId + ",加入调度成功,jobname=" + jobName + ",执行时间=" + DateUtil.toDateTimeString(startTime));
					} catch (SchedulerException e) {
						LogUtil.info("taskid=" + quotetotalinfo.getTaskid() + ",subtaskid=" + subInstanceId + ",加入调度失败,jobname=" + jobName);
						e.printStackTrace();
					}
				}else{
					if(TaskConst.QUOTEWAIT_53.equals(workflowsub.getTaskcode()) && !"Completed".equals(workflowsub.getTaskstate())){
						INSBQuoteinfo quoteInfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(workflowsub.getInstanceid());
						Map<String, Object> datamap = new HashMap<String, Object>(10);
						datamap.put("gzway", JSONObject.fromObject(workflowmainService.getGZway(taskid, quoteInfo.getInscomcode())).getString("gzway"));
						WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "53", "Completed", "平台查询", "完成", "admin");
						String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(datamap,subInstanceId, "admin", "平台查询", "1");
						LogUtil.info(subInstanceId+"=subInstanceId平台查询超时处理时发现有平台信息推平台查询成功。callback=" + callback);
					}
				}
			break;
			case "备用平台查询":
				INSBRulequerycarinfo insbRulequerycarinfo1 = new INSBRulequerycarinfo();
				insbRulequerycarinfo1.setTaskid(ProInstanceId_taskName.split("_")[2]);
				List<INSBRulequerycarinfo> list1 = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo1);
				if(list1 == null || list1.isEmpty() || !"guizequery".equals(list1.get(0).getOperator())){//再次检查是否存在平台信息，不存在则推平台查询失败，存在则推平台查询成功
					LogUtil.info(ProInstanceId_taskName.split("_")[2]+"subtaskid=" + subInstanceId + ",备用平台信息查询超时未回写");
					if(TaskConst.QUOTEWAIT_53.equals(workflowsub.getTaskcode()) && !"Completed".equals(workflowsub.getTaskstate())){
						WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "53", "Completed", "平台查询", "备用平台查询超时未回写", "admin");
						Map<String, Object> datamap = new HashMap<String, Object>();
						INSBQuoteinfo quote = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
						datamap = appInsuredQuoteServiceImpl.getPriceParamWay(datamap, workflowsub.getMaininstanceid(), quote.getInscomcode(), "1");
						WorkFlowUtil.completeTaskWorkflowRecheck("1",subInstanceId, "admin", "平台查询",datamap);
					}
				}else{
					if(TaskConst.QUOTEWAIT_53.equals(workflowsub.getTaskcode()) && !"Completed".equals(workflowsub.getTaskstate())){
						INSBQuoteinfo quoteInfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(workflowsub.getInstanceid());
						Map<String, Object> datamap = new HashMap<String, Object>(10);
						datamap.put("gzway", JSONObject.fromObject(workflowmainService.getGZway(workflowsub.getMaininstanceid(), quoteInfo.getInscomcode())).getString("gzway"));
						WorkflowFeedbackUtil.setWorkflowFeedback(null, subInstanceId, "53", "Completed", "平台查询", "完成", "admin");
						String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(datamap,subInstanceId, "admin", "平台查询", "1");
						LogUtil.info(ProInstanceId_taskName.split("_")[2]+"taskid"+subInstanceId+"=subInstanceId平台查询超时处理时发现有平台信息推平台查询成功。callback=" + callback);
					}
				}
				break;
			default:
				break;
			}
			LogUtil.info("========定时任务---执行完成---jobid="+ProInstanceId_taskName);
		} catch (Exception e) {
			LogUtil.info(e.getMessage()+" 处理任务超时异常结束====="+ProInstanceId_taskName);
			e.printStackTrace();
		}
	}
}
