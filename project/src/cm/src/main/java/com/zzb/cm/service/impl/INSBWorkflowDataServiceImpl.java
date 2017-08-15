package com.zzb.cm.service.impl;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.Pool;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.jobpool.timer.SchedulerService;
import com.common.*;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBMsgPushService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.model.WorkFlow4TaskModel;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dai on 2017/2/22.
 */
@Service
public class INSBWorkflowDataServiceImpl implements INSBWorkflowDataService {
    @Resource
    private SchedulerService schedule;
    @Resource
    private INSBWorkflowmainService workflowmainService;
    @Resource
    private INSBWorkflowsubService workflowsubService;
    @Resource
    private DispatchTaskService dispatchService;
    @Resource
    private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private InterFaceService interFaceService;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private CHNChannelService channelService;
    @Resource
    private AppQuotationService appQuotationService;
    @Resource
	private AppInsuredQuoteService appInsuredQuoteService;
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private IRedisClient redisClient;
    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBFlowerrorDao insbFlowerrorDao;
    @Resource
    private INSBMsgPushService insbMsgPushService;
    @Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
    @Resource
    private INSBUsercommentDao insbUsercommentDao;
    @Resource
    private INSBWorkflowmaintrackService insbWorkflowmaintrackService;

    public void startTaskFromWorlFlow(WorkFlow4TaskModel dataModel) {
        LogUtil.info("开启流程 =====" + dataModel);

        //----处理并发导致的流程数据重复问题----
        String redisKey = null;
        if (dataModel != null && "21".equals(dataModel.getTaskCode()) && "Ready".equals(dataModel.getTaskStatus())) {
            redisKey = "dhq_21_Ready_" + dataModel.getMainInstanceId() + "_" + dataModel.getSubInstanceId();
            try {
                int sync = redisClient.atomicIncr(Constants.CM_GLOBAL, redisKey);
                if (sync != 1) {
                    LogUtil.error(dataModel.getMainInstanceId() + "_" + dataModel.getSubInstanceId() + "_21_Ready_正在处理中,等待2秒");
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //--------------------------------

        try {
            transformData(dataModel, "start");
            //删除在工作流发起端设置的请求跟踪记录
            WorkflowAttachHandleUtil.delWfRecord(dataModel.getMainInstanceId(), dataModel.getSubInstanceId(), dataModel.getTaskCode(), dataModel.getTaskStatus());
            
            //启动定时任务，定时在凌晨1点开始处理工作流和cm互相通知异常的任务
            timerForDealSyncWorkflowAndCmStatus();
        } finally {
            if (redisKey != null) {
                try {
                    redisClient.del(Constants.CM_GLOBAL, redisKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //发消息
        insbMsgPushService.sendMsg(dataModel);

        //通知渠道
        taskthreadPool4workflow.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    channelService.callback(dataModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 启动定时任务处理工作流和cm流程状态不一致的数据
     */
    private void timerForDealSyncWorkflowAndCmStatus() {
		// TODO Auto-generated method stub
    	String status = String.valueOf(CMRedisClient.getInstance().get(6,"Scheduler", "syncworkflowstatus_同步节点"));
    	if(StringUtil.isEmpty(status)){//如果当前没有加入一个定时执行的任务处理
    		Date startTime = new Date();
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	long timeout = 60*60*1000;//默认多一个小时
    		LogUtil.info(status+"开始启动定时任务用于凌晨1点开始处理工作流与cm节点状态不一致，加入定时任务时间：" + startTime);
    		try {
        		timeout = (DateUtil.parse(format.format(startTime)+" 24:00:00", "yyyy-MM-dd HH:mm:ss").getTime()-startTime.getTime())+timeout;
        		startTime = new Date(timeout+startTime.getTime());
    			schedule.deleteHistoryJobAndStartNewJob("syncworkflowstatus_同步节点","syncworkflowstatus",startTime,"1",timeout+"","SyncWorkflowStatus");
    			LogUtil.info("开始启动定时任务用于凌晨1点开始处理工作流与cm节点状态不一致，开始启动任务时间：" + startTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}

	public void endTaskFromWorkFlow(WorkFlow4TaskModel dataModel) {
    	LogUtil.info("关闭流程====="+dataModel);
        transformData(dataModel, "end");
        //删除在工作流发起端设置的请求跟踪记录
        WorkflowAttachHandleUtil.delWfRecord(dataModel.getMainInstanceId(), dataModel.getSubInstanceId(), dataModel.getTaskCode(), dataModel.getTaskStatus());
        //启动定时任务，定时在凌晨1点开始处理工作流和cm互相通知异常的任务
        //timerForDealSyncWorkflowAndCmStatus();
        //通知渠道
        if("33".equals(dataModel.getTaskCode())){
            taskthreadPool4workflow.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        channelService.callback(dataModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * @param dataModel
     *            更新数据
     * @param type
     *            1：入池 2：出池
     * @throws Exception
     */
    private void transformData(WorkFlow4TaskModel dataModel, String type)  {
        Task task = new Task();

        // 主流程
        if (dataModel.getSubInstanceId() == null) {
            INSBWorkflowmain workflowmainModel = new INSBWorkflowmain();

            if (dataModel.getDataFlag() == 1) {
                task.setProInstanceId(dataModel.getMainInstanceId());
                task.setTaskName(dataModel.getTaskName());
                task.setPrvcode(dataModel.getProviderId());
                // 得到派发任务信息
                if ("start".equals(type)) {
                    workflowmainModel.setInstanceid(dataModel.getMainInstanceId());
                    workflowmainModel.setTaskname(dataModel.getTaskName());
                    workflowmainModel.setTaskcode(dataModel.getTaskCode());
                    workflowmainModel.setTaskstate(dataModel.getTaskStatus());
                    workflowmainModel.setProviderId(dataModel.getProviderId());
                    LogUtil.info("开启流程---主流程---CM保存数据");
                    saveOrUpdateWorkFlowMain(workflowmainModel,"admin");
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.info("开启流程---主流程---自动分人");
                            dispatchTask(task);
                        }
                    });

                } else {
                    // 现更新cm状态
                    workflowmainModel.setInstanceid(dataModel.getMainInstanceId());
                    workflowmainModel.setTaskname(dataModel.getTaskName());
                    workflowmainModel.setTaskstate(dataModel.getTaskStatus());
                    workflowmainModel.setTaskcode(dataModel.getTaskCode());
                    workflowmainModel.setProviderId(dataModel.getProviderId());
                    LogUtil.info("关闭流程---入池---主流程---CM保存数据");
                    saveOrUpdateWorkFlowMain(workflowmainModel,"admin");
                    // 结束任务
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.info("关闭流程---入池---主流程");
                            endTask(task);
                        }
                    });

                }
            } else {
                workflowmainModel.setOperator("admin");
                workflowmainModel.setInstanceid(dataModel.getMainInstanceId());
                workflowmainModel.setTaskname(dataModel.getTaskName());
                workflowmainModel.setTaskstate(dataModel.getTaskStatus());
                workflowmainModel.setTaskcode(dataModel.getTaskCode());
                workflowmainModel.setProviderId(dataModel.getProviderId());
                saveOrUpdateWorkFlowMain(workflowmainModel,"admin");
            }

        // 更新子流程表
        } else {
            INSBWorkflowsub WorkflowsubModel = new INSBWorkflowsub();
            //补全信息
            if (dataModel.getMainInstanceId() == null) {
                String  mainInstancId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(dataModel.getSubInstanceId());
                dataModel.setMainInstanceId(mainInstancId);
            }
            if (dataModel.getDataFlag() == 1) {
                task.setSonProInstanceId(dataModel.getSubInstanceId());
                task.setTaskName(dataModel.getTaskName());
                task.setProInstanceId(dataModel.getMainInstanceId());
                task.setPrvcode(dataModel.getProviderId());
                if ("start".equals(type)) {
                    WorkflowsubModel.setMaininstanceid(dataModel.getMainInstanceId());
                    WorkflowsubModel.setInstanceid(dataModel.getSubInstanceId());
                    WorkflowsubModel.setTaskname(dataModel.getTaskName());
                    WorkflowsubModel.setTaskcode(dataModel.getTaskCode());
                    WorkflowsubModel.setTaskstate(dataModel.getTaskStatus());
                    WorkflowsubModel.setProviderId(dataModel.getProviderId());
                    LogUtil.info("开启流程---入池---子流程---CM保存数据");
                    saveOrUpdateWorkflowSub(WorkflowsubModel, "admin");
                    // 得到派发任务信息 加入任务池 开启全部配发
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.info("开启流程---入池---子流程---自动分人");
                            dispatchTask(task);
                        }
                    });

                } else {
                    WorkflowsubModel.setMaininstanceid(dataModel.getMainInstanceId());
                    WorkflowsubModel.setInstanceid(dataModel.getSubInstanceId());
                    WorkflowsubModel.setTaskname(dataModel.getTaskName());
                    WorkflowsubModel.setTaskcode(dataModel.getTaskCode());
                    WorkflowsubModel.setTaskstate(dataModel.getTaskStatus());
                    WorkflowsubModel.setProviderId(dataModel.getProviderId());
                    LogUtil.info("关闭流程---子流程---CM保存数据");
                    saveOrUpdateWorkflowSub(WorkflowsubModel,"admin");
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.info("关闭流程---子流程");
                            endTask(task);
                        }
                    });

                }
            } else {
                WorkflowsubModel.setOperator("admin");
                WorkflowsubModel.setMaininstanceid(dataModel.getMainInstanceId());
                WorkflowsubModel.setInstanceid(dataModel.getSubInstanceId());
                WorkflowsubModel.setTaskname(dataModel.getTaskName());
                WorkflowsubModel.setTaskstate(dataModel.getTaskStatus());
                WorkflowsubModel.setTaskcode(dataModel.getTaskCode());
                WorkflowsubModel.setProviderId(dataModel.getProviderId());
                saveOrUpdateWorkflowSub(WorkflowsubModel,"admin");
                taskthreadPool4workflow.execute(new Runnable() {
                    @Override
                    public void run() {
                        //进入支付
                        if ("20".equals(WorkflowsubModel.getTaskcode())&&"Reserved".equals(WorkflowsubModel.getTaskstate())) {
                            LogUtil.info("========推送数据到cif--开始========");
                            String taskid = null;
                            if (WorkflowsubModel.getMaininstanceid() == null) {
                                taskid = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(WorkflowsubModel.getInstanceid());
                            } else {
                                taskid = WorkflowsubModel.getMaininstanceid();
                            }
                            saveInsuredInfoToCif(taskid, WorkflowsubModel.getInstanceid());
                        }
                    }

                });
            }
        }

        if ("start".equals(type)) {
            String taskName = dataModel.getTaskName();
            switch (taskName) {
                case "精灵报价":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = dataModel.getSubInstanceId()+"_"+taskName;
                            try {
                                String  timeout = getTimeout(dataModel.getProviderId(), "robot", "quote", "60000");
                                Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                LogUtil.info(timeout+"subtaskid=" + dataModel.getMainInstanceId() + ",开始精灵报价,jobname=" + jobName);
                                //开启任务前清除历史定时任务
                                schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                //调用精灵
                                interFaceService.goRobot(dataModel.getMainInstanceId(),dataModel.getProviderId(), "admin", "quote",dataModel.getSubInstanceId());
                            } catch (Exception e) {
                                LogUtil.info("subtaskid=" + dataModel.getMainInstanceId() + ",开始精灵报价异常结束," + e.getMessage());
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                Map<String, Object> datamap = new HashMap<String, Object>();
        						datamap = appInsuredQuoteService.getPriceParamWay(datamap, dataModel.getMainInstanceId(), dataModel.getProviderId(), "2");
        						WorkFlowUtil.completeTaskWorkflowRecheck("1",dataModel.getSubInstanceId(), "admin",taskName,datamap);
        						schedule.deleteJob(jobName);
                            }
                        }
                    });
                    break;

                case "EDI报价":
                    //请求规则获取折扣系数
                    appQuotationService.updatePolicyDiscount(dataModel.getSubInstanceId(), dataModel.getMainInstanceId(), dataModel.getProviderId());

                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = dataModel.getSubInstanceId()+"_"+taskName;
                            try {
                                String  timeout = getTimeout(dataModel.getProviderId(), "edi", "quote", "60000");
                                Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                LogUtil.info(timeout+"subtaskid=" + dataModel.getMainInstanceId() + ",开始EDI报价,jobname=" + jobName);
                                //开启任务前清除历史定时任务
                                schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                //通知EDI
                                interFaceService.goEDi(dataModel.getMainInstanceId(),dataModel.getProviderId(), "admin", "quote",dataModel.getSubInstanceId());
                            } catch (Exception e) {
                                LogUtil.info("taskid=" + dataModel.getMainInstanceId() + "=====EDI报价异常结束=====");
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                Map<String, Object> datamap = new HashMap<String, Object>();
        						datamap = appInsuredQuoteService.getPriceParamWay(datamap, dataModel.getMainInstanceId(), dataModel.getProviderId(), "1");
        						WorkFlowUtil.completeTaskWorkflowRecheck("1",dataModel.getSubInstanceId(), "admin",taskName,datamap);
                                schedule.deleteJob(jobName);
                            }
                        }
                    });
                    break;

                case "精灵承保":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = null;
                            boolean togo = false;
                            try {
                                String approvedQueryStatus = (String)redisClient.get(Constants.CM_GLOBAL, dataModel.getMainInstanceId()+"_approvedQueryStatus");
                                if("D".equals(approvedQueryStatus)){
                                    //如果承保查询成功，直接推待承保打单
                                    Map<String,Object> data = new HashMap<String, Object>();
                                    data.put("result", "31");
                                    LogUtil.info("taskid=" + dataModel.getMainInstanceId() + "承保查询succeed开始推待承保打单,providerid=" + dataModel.getProviderId());
                                    WorkflowFeedbackUtil.setWorkflowFeedback(dataModel.getMainInstanceId(), null, dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.underWrittn_complete, "admin");
                                    WorkFlowUtil.undwrtSuccess(dataModel.getMainInstanceId(), "admin", "精灵承保", data);//推工作流 - 待承保打单
                                }else if("D1".equals(approvedQueryStatus)){
                                    //如果承保查询失败，直接推人工承保
                                    Map<String,Object> data = new HashMap<String, Object>();
                                    data.put("result", "1");
                                    data.put("acceptway", "3");
                                    LogUtil.info("taskid=" + dataModel.getMainInstanceId() + "承保查询false开始推人工承保,providerid=" + dataModel.getProviderId());
                                    WorkflowFeedbackUtil.setWorkflowFeedback(dataModel.getMainInstanceId(), null, dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.underWrittn_sendback, "admin");
                                    WorkFlowUtil.undwrtSuccess(dataModel.getMainInstanceId(), "admin", "精灵承保", data);//推工作流 - 人工承保
                                }else {
                                    togo = true;
                                    jobName = dataModel.getMainInstanceId()+"_"+taskName;
                                    String timeout = getTimeout(dataModel.getProviderId(), "robot", "insure", "180000");
                                    Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                    LogUtil.info(timeout + "taskid=" + dataModel.getMainInstanceId() + ",开始精灵承保,providerid=" + dataModel.getProviderId());
                                    //开启任务前清除历史定时任务
                                    schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                    interFaceService.goRobot(dataModel.getMainInstanceId(), dataModel.getProviderId(), "admin", "approved", dataModel.getMainInstanceId());
                                }
                                redisClient.del(Constants.CM_GLOBAL, dataModel.getMainInstanceId()+"_approvedQueryStatus");
                            } catch (Exception e) {
                                LogUtil.info("taskid=" + dataModel.getMainInstanceId() + "=====精灵承保保异常结束=====");
                                e.printStackTrace();
                                if (togo) {
                                    WorkflowFeedbackUtil.setWorkflowFeedback(dataModel.getMainInstanceId(), null, dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                }
                                Map<String, String> datamap = new HashMap<String, String>();
                                datamap.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(dataModel.getMainInstanceId(), dataModel.getProviderId(), "2", "contract")).getString("quotecode"));
                                WorkFlowUtil.completeTaskWorkflow("1",dataModel.getMainInstanceId(), "admin",taskName,datamap);
                                if (togo) {
                                    schedule.deleteJob(jobName);
                                }
                            }
                        }
                    });
                    break;

                case "EDI承保":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = dataModel.getMainInstanceId()+"_"+taskName;
                            try {
                                String  timeout = getTimeout(dataModel.getProviderId(), "edi", "insure", "180000");
                                Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                LogUtil.info(timeout+"taskid=" + dataModel.getMainInstanceId() + ",开始EDI承保,providerid=" + dataModel.getProviderId());
                                //开启任务前清除历史定时任务
                                schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                interFaceService.goEDi(dataModel.getMainInstanceId(),dataModel.getProviderId(), "admin","approved",dataModel.getMainInstanceId());
                            } catch (Exception e) {
                                LogUtil.info("taskid=" + dataModel.getMainInstanceId() + "=====EDI承保异常结束=====");
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(dataModel.getMainInstanceId(), null, dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                Map<String, String> datamap = new HashMap<String, String>();
                                datamap.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(dataModel.getMainInstanceId(), dataModel.getProviderId(), "1", "contract")).getString("quotecode"));
                                WorkFlowUtil.completeTaskWorkflow("1",dataModel.getMainInstanceId(), "admin",taskName,datamap);
                                schedule.deleteJob(jobName);
                            }
                        }
                    });
                    break;

                case "精灵核保暂存":
                case "精灵自动核保":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = dataModel.getSubInstanceId()+"_"+taskName;
                            try {
                                String  timeout = getTimeout(dataModel.getProviderId(), "robot", "underwriting", "180000");
                                Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                LogUtil.info(timeout+"taskid=" + dataModel.getMainInstanceId() + ",开始精灵核保,jobname=" + jobName);
                                //开启任务前清除历史定时任务
                                schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                String taskType = "精灵核保暂存".equals(taskName) ? "insure" : "autoinsure";
                                interFaceService.goRobot(dataModel.getMainInstanceId(),dataModel.getProviderId(), "admin",taskType,dataModel.getSubInstanceId());
                            } catch (Exception e) {
                                LogUtil.info("taskid=" + dataModel.getMainInstanceId() + ",开始精灵核保异常结束," + e.getMessage());
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                WorkFlowUtil.completeTaskWorkflow("1",dataModel.getSubInstanceId(), "admin",taskName,null);
                                schedule.deleteJob(jobName);
                            }
                        }
                    });
                    break;

                case "EDI核保暂存":
                case "EDI自动核保":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            String jobName = dataModel.getSubInstanceId()+"_"+taskName;
                            try {
                                String  timeout = getTimeout(dataModel.getProviderId(), "edi", "underwriting", "180000");
                                Date startTime = new Date(new Date().getTime() + Long.parseLong(timeout));
                                LogUtil.info(timeout + "taskid=" + dataModel.getMainInstanceId() + ",开始EDI核保,jobname=" + jobName);
                                //开启任务前清除历史定时任务
                                String taskType = "EDI核保暂存".equals(taskName) ? "insure" : "autoinsure";
                                schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), timeout, "TaskOvertimeJob4EdiFairy");
                                interFaceService.goEDi(dataModel.getMainInstanceId(),dataModel.getProviderId(),"admin", taskType,dataModel.getSubInstanceId());
                            } catch (Exception e) {
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.itf_fail, "admin");
                                String result = workflowmainService.getContracthbType(dataModel.getMainInstanceId(), dataModel.getProviderId(), "", "underwritingType");
                                JSONObject jsonObject = JSONObject.fromObject(result);
                                LogUtil.info(e.getMessage()+"=====EDI核保异常结束=====getContracthbType="+result);
                                WorkFlowUtil.completeTaskWorkflow(jsonObject.optString("quotecode"),dataModel.getSubInstanceId(), "admin",taskName,null);
                                schedule.deleteJob(jobName);
                            }
                        }
                    });
                    break;

                case "规则报价":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LogUtil.info("=====开始规则报价====taskid= "+dataModel.getMainInstanceId()+"-----" +dataModel);
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.quote_complete, "admin");
                                appQuotationService.getQuotationInfoForFlow(dataModel.getSubInstanceId(), dataModel.getMainInstanceId(), dataModel.getProviderId());
                            } catch (Exception e) {
                                e.printStackTrace();
                                WorkflowFeedbackUtil.setWorkflowFeedback(null, dataModel.getSubInstanceId(), dataModel.getTaskCode(), "Completed", taskName, WorkflowFeedbackUtil.quote_failed, "admin");
                                Map<String, Object> datamap = new HashMap<String, Object>();
        						datamap = appInsuredQuoteService.getPriceParamWay(datamap, dataModel.getMainInstanceId(), dataModel.getProviderId(), "3");
        						WorkFlowUtil.completeTaskWorkflowRecheck("1",dataModel.getSubInstanceId(), "admin",taskName,datamap);
                            }
                        }
                    });
                    break;

                case "平台查询":
                    // 直接判断同一个多方报价中有没有单方的精灵报价,有则需要设置40秒超时,没有则可以立刻启动备用平台查询
                    String taskid = dataModel.getMainInstanceId();
                    INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                    insbQuotetotalinfo.setTaskid(taskid);
                    INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
                    String robotQuote = null;

                    Object obj = CMRedisClient.getInstance().get(Constants.CM_GLOBAL, taskid + "robotQuote");
                    if (obj != null)
                        robotQuote = String.valueOf(obj);
                    if (StringUtils.isBlank(robotQuote)) {
                        List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoService.getQuoteinfosByInsbQuotetotalinfoid(quotetotalinfo.getId());
                        robotQuote = "";
                        for (INSBQuoteinfo insbQuoteinfo : insbQuoteinfos) {
                            String quotetype = getQuoteTypeOnly(Long.valueOf(taskid), insbQuoteinfo.getInscomcode(), "0");
                            // 如果是精灵报价
                            LogUtil.info("taskid=" + taskid + ",inscomcode=" + insbQuoteinfo.getInscomcode() + ",获取报价类型结果=" + quotetype);
                            if ("2".equals(JSONObject.fromObject(quotetype).optString("quotecode")))  robotQuote += insbQuoteinfo.getInscomcode() + ",";
                        }
                        CMRedisClient.getInstance().set(Constants.CM_GLOBAL, taskid + "robotQuote", robotQuote, 1 * 60);
                        LogUtil.info("taskid=" + taskid + ",subtaskid=" + dataModel.getSubInstanceId() + ",第一次判断是否有精灵报价,robotQuote=" + robotQuote);
                    } else {
                        LogUtil.info("taskid=" + taskid + ",subtaskid=" + dataModel.getSubInstanceId() + ",已判断有精灵正在报价,robotQuote=" + robotQuote);
                    }
                    String jobName = dataModel.getSubInstanceId() + "_" + taskName+"_"+taskid;
                    long nowDate = new Date().getTime();
                    int timeout = 40000;
                    if (StringUtils.isNotBlank(robotQuote)) {
                        LogUtil.info("taskid=" + taskid + ",多方中包括有精灵报价,设置40秒超时,jobname=" + jobName);
                        nowDate = nowDate + timeout; // 默认40秒超时
                    } else {
                        timeout = 1000;
                        LogUtil.info("taskid=" + taskid + ",多方中不包括精灵报价,设置1秒超时,jobname=" + jobName);
                        nowDate = nowDate + timeout; // 1秒超时,相当于立即启动备用平台查询
                    }
                    Date startTime = new Date(nowDate);
                    try {
                        // 开启任务前清除历史定时任务
                        schedule.deleteHistoryJobAndStartNewJob(jobName, taskName, startTime, dataModel.getProviderId(), ""+timeout, "TaskOvertimeJob4EdiFairy");
                        LogUtil.info("taskid=" + quotetotalinfo.getTaskid() + ",subtaskid=" + dataModel.getSubInstanceId() + ",平台查询加入调度成功,jobname=" + jobName + ",执行时间=" + DateUtil.toDateTimeString(startTime));
                    } catch (SchedulerException e) {
                        LogUtil.info("taskid=" + taskid + ",加入调度失败,jobname=" + jobName);
                        e.printStackTrace();
                    }
                    break;

                case "核保查询":
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, Object> result = insbWorkflowmaintrackService.toLoopUnderwriting(dataModel.getMainInstanceId(),
                                    dataModel.getSubInstanceId(), dataModel.getProviderId(), null);

                            LogUtil.info("toDoLoopUnderwriting taskid=" + dataModel.getMainInstanceId() +
                                    ",subtaskid=" +dataModel.getSubInstanceId() + ",result="+JsonUtil.serialize(result));
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        LogUtil.info("流程处理完成====="+dataModel.getMainInstanceId()+","+dataModel.getSubInstanceId());
    }

    /**
     * 更新主流程节点信息
     *
     * @param workflowmainModel
     * @return
     */
    private void saveOrUpdateWorkFlowMain(INSBWorkflowmain workflowmainModel,String fromOperator) {
        if (workflowmainModel != null) {
            workflowmainService.updateWorkFlowMainData(workflowmainModel,fromOperator);
        }
    }

    /**
     * 更新子流程信息
     *
     * @param WorkflowsubModel
     */
    private void saveOrUpdateWorkflowSub(INSBWorkflowsub WorkflowsubModel,String fromOperator) {
        if (WorkflowsubModel != null) {
            workflowsubService.updateWorkFlowSubData(WorkflowsubModel,fromOperator);
        }
    }

    private static final List<String> manworkcode = new ArrayList<>(9);
    static {
        manworkcode.add("6");//人工调整
        manworkcode.add("7");//人工规则报价
        manworkcode.add("8");//人工报价
        manworkcode.add("31");//人工回写
        manworkcode.add("18");//人工核保
        manworkcode.add("21");//二次支付确认
        manworkcode.add("27");//人工承保
        manworkcode.add("23");//打单
        manworkcode.add("24");//配送
    }

    public boolean isManWork(String taskcode) {
        return manworkcode.contains(taskcode);
    }

    /**
     * 开启任务分发任务
     *
     * @param task
     */
    @SuppressWarnings("static-access")
    private void dispatchTask(Task task) {

        Pool.addOrUpdate(task);
        try {
            if(!TaskConst.QUOTING_7_NAME.equals(task.getTaskName())){//非规则报价子流程先通过
                LogUtil.info("非规则报价子流程先通过,人工调度启动====task="+task);
                Thread.currentThread().sleep(1000);
                dispatchService.dispatchTask(task);
                return;
            }
            if(checkOthersonProccess(task)){//检查其他子流程是否有需要进入人工规则报价的,或者是否已经通过了人工规则报价的节点
                //继续检查不跟此任务相关的所有其他人工规则报价未分配业管处理的任务
                INSBWorkflowsub subarg0 = new INSBWorkflowsub();
                subarg0.setTaskcode(TaskConst.QUOTING_7);
                subarg0.setReclystate("dispatch");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String datestr = format.format(new Date());
                subarg0.setStartTime(datestr+" 00:00:00");
                subarg0.setEndTime(datestr+" 23:59:59");
                List<INSBWorkflowsub> subModels = workflowsubService.queryList(subarg0);
                StringBuffer mainWfids = new StringBuffer("");
                Task task1 = null;INSBQuoteinfo param = null;
                for(INSBWorkflowsub sub:subModels){
                    if(!mainWfids.toString().contains(sub.getMaininstanceid())){
                        mainWfids.append(sub.getMaininstanceid());
                        task1 = new Task();
                        param = new INSBQuoteinfo();
                        param.setWorkflowinstanceid(sub.getInstanceid());
                        param = insbQuoteinfoDao.selectOne(param);
                        if(null==param){
                            LogUtil.info("子任务报价表信息为空param"+param);
                            break;
                        }
                        task1.setPrvcode(param.getInscomcode());
                        task1.setProInstanceId(sub.getMaininstanceid());
                        task1.setSonProInstanceId(sub.getInstanceid());
                        task1.setTaskName(TaskConst.QUOTING_7_NAME);
                        checkOthersonProccess(task1);
                    }
                }
            }else{
                Thread.currentThread().sleep(1000);
                dispatchService.dispatchTask(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("人工调度启动异常.taskid="+task.getProInstanceId());
        }
    }

    private boolean checkOthersonProccess(Task task) throws InterruptedException {
        LogUtil.info("分配检查主任务taskid="+task.getProInstanceId());
        List<INSBWorkflowsub> subModels =  workflowsubService.selectSubModelByMainInstanceId(task.getProInstanceId());
        if(null!=subModels && subModels.size()==1){//如果只有一个子流程,则直接通过,进行调度
            return false;//通过,进行调度
        }else if(null!=subModels && subModels.size()>1){//如果多个子流程,则进行分析是否所有的都已经通过
            Task task7 = null;INSBQuoteinfo param = null;
            for(INSBWorkflowsub sub:subModels){
                if(TaskConst.QUOTING_7.equals(sub.getTaskcode())&&(null==sub.getOperator()||StringUtil.isEmpty(sub.getOperator()))){
                    task7 = new Task();
                    param = new INSBQuoteinfo();
                    param.setWorkflowinstanceid(sub.getInstanceid());
                    param = insbQuoteinfoDao.selectOne(param);
                    if(null==param){
                        LogUtil.info("子任务报价表信息为空param"+param);
                        break;
                    }
                    task7.setPrvcode(param.getInscomcode());
                    task7.setTaskName(TaskConst.QUOTING_7_NAME);
                    task7.setTaskcode(TaskConst.QUOTING_7);
                    task7.setProInstanceId(sub.getMaininstanceid());
                    task7.setSonProInstanceId(sub.getInstanceid());
                    Thread.currentThread().sleep(60000);//如果有人工规则报价等待60秒
                    break;
                }
            }
            //重新查询一次
            subModels =  workflowsubService.selectSubModelByMainInstanceId(task.getProInstanceId());
            int count = subModels.size();
            for(INSBWorkflowsub sub:subModels){
                if(TaskConst.QUOTING_7.equals(sub.getTaskcode()) && (null==sub.getOperator()||StringUtil.isEmpty(sub.getOperator()))
                        && task7.getProInstanceId().equals(sub.getMaininstanceid()) && !task7.getSonProInstanceId().equals(sub.getInstanceid())){
                    if(null==task7.getTaskRelated()){
                        task7.setTaskRelated(new ArrayList<String>());
                    }
                    task7.getTaskRelated().add(sub.getInstanceid());
                }else if(TaskConst.QUOTING_32.equals(sub.getTaskcode())||TaskConst.QUOTING_3.equals(sub.getTaskcode())||TaskConst.QUOTING_4.equals(sub.getTaskcode())){
                    count--;//还存在规则报价,精灵报价,或者EDI报价的流程则等待,减掉一个任务数标识此次人工规则报价任务需要等待
                    LogUtil.info(sub.getInstanceid()+"subinstanceid子任务报价表信息为空param"+sub.getTaskcode());
                }
            }
            if(count==subModels.size()){
                if(null != task7){
                    Thread.currentThread().sleep(1000);
                    dispatchService.dispatchTask(task7);
                }
                return false;//通过,进行调度
            }else{
                if(!TaskConst.QUOTING_7_NAME.equals(task.getTaskName())){//非规则报价子流程先通过
                    Thread.currentThread().sleep(1000);
                    dispatchService.dispatchTask(task);
                }
                return true;//不通过,等待再调度
            }
        }else{
            LogUtil.info(subModels.size()+"=size。checkOthersonProccess异常情况====taskid="+task.getProInstanceId());
            return true;//不通过,异常情况
        }
    }

    private void endTask(Task task) {
        dispatchService.completeTask(task,"end");
    }

    /**
     * 向平台推送车辆信息
     * @param taskid
     * @return
     */
    private void saveInsuredInfoToCif(String taskid, String subInstanceId){
        if(!StringUtil.isEmpty(taskid)){
            INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subInstanceId);
            String inscomcode = insbQuoteinfo.getInscomcode();
            INSBCarinfohis carinfohis = commonQuoteinfoService.getCarInfo(taskid, inscomcode);

            if(null != carinfohis){
                if(!StringUtil.isEmpty(carinfohis.getCarlicenseno()) && !"新车未上牌".equals(carinfohis.getCarlicenseno())){
                    //获取报价区域
                    INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                    insbQuotetotalinfo.setTaskid(taskid);
                    INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);

                    if(null != quotetotalinfo){
                        INSBCarinfo carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = insbCarinfoDao.selectOne(carinfo);

                        if (carinfo != null) {
                            INSBCarmodelinfohis carmodelinfohis = commonQuoteinfoService.getCarModelInfo(carinfo.getId(), inscomcode);
                            String areaId = quotetotalinfo.getInscitycode();
                            //根据车牌平台查询
                            queryLastInsuredInfoByCarinfo(taskid, carinfohis, areaId, carmodelinfohis);
                        }
                    }
                }
            }
        }
    }

    private LastYearPolicyInfoBean queryLastInsuredInfoByCarinfo(String taskid, INSBCarinfohis insbCarinfohis, String areaId, INSBCarmodelinfohis carmodelinfohis){
        JSONObject object = new JSONObject();
        object.put("flag", "XB");
        //quickflag  0 正常投保 1快速续保
        object.put("quickflag", "0");
        //issavecarinfo  0 保存  1 不保存
        object.put("issavecarinfo", "0");
        JSONObject inParas = new JSONObject();
        inParas.put("car.specific.license", insbCarinfohis.getCarlicenseno());
        object.put("inParas", inParas);
        JSONObject carinfo = new JSONObject();
        String vehiclename = "";
        //获取车型名称
        if(null != carmodelinfohis){
            vehiclename = carmodelinfohis.getStandardfullname();
        }
        carinfo.put("engineno", insbCarinfohis.getEngineno());//发动机号
        carinfo.put("vehicleframeno", insbCarinfohis.getVincode());//车架号
        carinfo.put("registerdate", DateUtil.toString(insbCarinfohis.getRegistdate()));//初登日期
        carinfo.put("vehiclename", vehiclename);//品牌型号
        carinfo.put("chgownerflag", insbCarinfohis.getIsTransfercar());//是否过户车  0不 是  1 是
        carinfo.put("usingnaturecode", cifUsePropsFrommap(insbCarinfohis.getCarproperty()));//使用性质编码
        carinfo.put("vehicletype", getProperty(insbCarinfohis.getProperty()));//车辆性质
        object.put("carinfo", carinfo);
        object.put("areaId", areaId);
        object.put("eid", taskid);

        LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
        try {
            LogUtil.info(object.toString());
            String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
            LogUtil.info("推送车辆信息数据到cif==taskid:"+ taskid + ",结果=" + resultJson);
            //JSONObject jsonObject=JSONObject.fromObject(resultJson);
            //lastYearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
        } catch (Exception e) {
            lastYearPolicyInfoBean = null;
            e.printStackTrace();
        }
        return lastYearPolicyInfoBean;
    }

    /**
     * 车辆性质与平台映射
     * @param key
     * @return
     */
    public String cifUsePropsFrommap(String key){
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("16", "701");
        mapping.put("15", "801");
        mapping.put("12", "240");
        mapping.put("11", "230");
        mapping.put("10", "220");
        mapping.put("1", "1");
        mapping.put("2", "101");
        mapping.put("3", "102");
        mapping.put("4", "103");
        mapping.put("6", "104");
        return null == mapping.get(key)||"".equals(mapping.get(key))?"1":mapping.get(key);
    }

    private String getProperty(String key){
        Map<String, String> map = new HashMap<String, String>();
        map.put("0", "个人用车");
        map.put("1", "企业用车");
        map.put("2", "机关团体");
        return StringUtil.isEmpty(key)? "" : map.get(key);
    }

    /**
     * 从配置文件中读取超时时间, 先取供应商前8位的配置，如果没有取前6位的配置，最后取前4位的配置
     * @param providerId 供应商id
     * @param processType 处理类型：精灵=robot、EDI=edi
     * @param taskName 任务类型：报价=quote、核保=underwriting
     * @param defaultTimeout 默认超时时间
     * @return 超时时间，以毫秒为单位
     */
    private String getTimeout(String providerId, String processType, String taskName, String defaultTimeout){
        String timeout = null;
        if(StringUtils.isNotBlank(providerId)){
            if(providerId.length() >= 8)
                timeout = ValidateUtil.getConfigValue(processType + "." + taskName + ".timeout." + providerId.substring(0, 8));
            if(StringUtils.isBlank(timeout) && providerId.length() >= 6)
                timeout = ValidateUtil.getConfigValue(processType + "." + taskName + ".timeout." + providerId.substring(0, 6));
            if(StringUtils.isBlank(timeout) && providerId.length() >= 4)
                timeout = ValidateUtil.getConfigValue(processType + "." + taskName + ".timeout." + providerId.substring(0, 4));
        }

        if(StringUtils.isBlank(timeout))
            timeout = ValidateUtil.getConfigValue(processType + "." + taskName + ".timeout.default");
        if(StringUtils.isBlank(timeout) && StringUtils.isNotBlank(defaultTimeout))
            timeout = defaultTimeout;
        if(StringUtils.isBlank(timeout))
            timeout = "60000";
        return timeout;
    }

    /**
     * 根据实例id和供应商id返回第N次报价方式
     *
     * @param processinstanceid
     *            实例id
     * @param providerid
     *            供应商id
     * @param quotecode          上次报价名称 （0：初始、1：EDI 、 2： 精灵 、 3：规则报价 、4：人工调整 、 5：人工规则录单 、6： 人工录单）
     * @return 当前报价方式（如果是精灵或者edi返回当前精灵或者edi的id）
     */
    public String getQuoteType(Long processinstanceid, String providerid, String quotecode) {
        String taskid = String.valueOf(processinstanceid);
        String result = workflowmainService.getQuoteType(taskid, providerid, quotecode);

        //需求895, 如果没有自动报价的能力,用于给前端提示
        String resQuotecode = JSONObject.fromObject(result).getString("quotecode");

        LogUtil.info("--" + taskid + "---报价能力, inscomcode=" + providerid + ", quotecode=" + quotecode + ",resQuotecode=" + resQuotecode);

        if(resQuotecode != null){
            if("0".equals(quotecode) && ("5".equals(resQuotecode) || "6".equals(resQuotecode))){
                INSBFlowerror queryflowerr = new INSBFlowerror();
                //判断是否直接规则推人工规则报价了
                queryflowerr.setTaskid(taskid);
                queryflowerr.setInscomcode(providerid);
                queryflowerr.setTaskstatus("guize");
                INSBFlowerror flowerr = insbFlowerrorDao.selectOne(queryflowerr);
                if(null != flowerr && null!=flowerr.getErrordesc() && flowerr.getErrordesc().contains("转人工规则")){//bug8044,直接转人工规则报价，不需要点击我要人工报价
                	LogUtil.info(flowerr.getErrordesc()+"===" + taskid + "直接转人工规则, inscomcode="+providerid);
                }else{
                	 queryflowerr.setTaskstatus("autoquote");
                     flowerr = insbFlowerrorDao.selectOne(queryflowerr);
                     LogUtil.info("--" + taskid + "---无法自动报价, inscomcode=" + providerid + ", quotecode=" + quotecode + ",resQuotecode=" + resQuotecode + ", flowerr=" + flowerr);
                     if(flowerr == null){
                         INSBFlowerror newflowerr = new INSBFlowerror();
                         newflowerr.setTaskid(taskid);
                         newflowerr.setOperator("admin");
                         newflowerr.setInscomcode(providerid);
                         newflowerr.setErrorcode("11"); //自动报价失败, 转人工报价
                         newflowerr.setFlowcode("A1");
                         newflowerr.setFlowname("报价能力");
                         newflowerr.setFiroredi("7");
                         newflowerr.setTaskstatus("autoquote");
                         newflowerr.setResult("false");
                         newflowerr.setErrordesc("该车无法进行自动报价，需要转人工处理，请点击[我要人工报价]");
                         newflowerr.setCreatetime(new Date());
                         insbFlowerrorDao.insert(newflowerr);
                     }
                }
            }

            //bug-4425-EDI、精灵报价失败且协议启用规则报价的情况下，单交+车船税报价有提交转人工备注的走人工规则报价
            if ("3".equals(resQuotecode) && ("1".equals(quotecode) || "2".equals(quotecode))) {
                int ic = commonQuoteinfoService.getInsureConfigType(taskid, providerid);
                LogUtil.info("%s,%s投保险种类型：%s", taskid, providerid, ic);

                if (ic == 4) {
                    INSBWorkflowmaintrack maintrack = new INSBWorkflowmaintrack();
                    maintrack.setInstanceid(taskid);
                    maintrack.setTaskcode("1");//报价时候提交的备注
                    List<INSBWorkflowmaintrack> maintracks = insbWorkflowmaintrackDao.selectList(maintrack);
                    if (maintracks != null && !maintracks.isEmpty()) {
                        maintrack = maintracks.get(maintracks.size()-1);//最早那个
                        INSBUsercomment usercomment = new INSBUsercomment();
                        usercomment.setTrackid(maintrack.getId());
                        usercomment.setTracktype(1);
                        usercomment.setCommenttype(1);
                        List<INSBUsercomment> usercomments = insbUsercommentDao.selectList(usercomment);
                        if (usercomments != null && !usercomments.isEmpty()) {
                            usercomment = usercomments.get(usercomments.size()-1);//最早那个
                            LogUtil.warn("%s,%s id为%s的usercomment的commentcontenttype：%s", taskid, providerid, usercomment.getId(), usercomment.getCommentcontenttype());

                            if (!StringUtil.isEmpty(usercomment.getCommentcontenttype())) {
                                Map<String, Object> map = appInsuredQuoteService.isNeedToManMade(String.valueOf(usercomment.getCommentcontenttype()));
                                if (null != map && (boolean) map.get("flag")) {
                                    LogUtil.info("%s,%s报价时提交了转人工的备注：%s，报价途径由3改为5", taskid, providerid, map.get("result"));

                                    Map map1 = com.alibaba.fastjson.JSON.parseObject(result, Map.class);
                                    map1.put("quotecode", "5");
                                    result = JSONObject.fromObject(map1).toString();
                                }
                            }
                        } else {
                            LogUtil.warn("%s,%s maintrackid为%s的usercomment为空", taskid, providerid, maintrack.getId());
                        }
                    } else {
                        LogUtil.warn("%s,%s为1的workflowmaintrack为空", taskid, providerid);
                    }
                }
            }
        }

        return result;
    }
    
    public String getQuoteTypeOnly(Long processinstanceid, String providerid, String quotecode) {
        String taskid = String.valueOf(processinstanceid);
        return workflowmainService.getQuoteType(taskid, providerid, quotecode);
    }

	@Override
	public List<String> getSubInstanceIdByInsbWorkflowMainInstanceId(String mainInstanceid) {
		List<String> subs = workflowsubService.queryInstanceIdsByMainInstanceId(mainInstanceid);
		if(null!=subs){
			return subs;
		}else{
			return null;
		}
	}
}
