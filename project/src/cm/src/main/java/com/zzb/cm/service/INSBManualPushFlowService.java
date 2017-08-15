package com.zzb.cm.service;

import com.zzb.cm.controller.vo.ManualPushFlowVo;

import java.util.List;
import java.util.Map;

public interface INSBManualPushFlowService{
	/**  
	 * 任务列表查询		杨威
	 */
	public String showTaskList(Map<String,Object> param);

	/**
	 * 同步工作流状态
	 * @param manualPushFlowVoList
	 * @param operator
	 * @return
	 */
	public Map<String,String> syncWorkflowStatus(List<ManualPushFlowVo> manualPushFlowVoList, String operator);
	
	/**
	 * 定时任务自动同步工作流状态
	 * @param operator
	 * @return
	 */
	public void autosyncWorkflowStatus(String operator);
	
	/**
	 * 报价/核保任务成功推送工作流
	 */
	public String underWriteSuccess(String instanceid, String operator, String forname, String maininstanceid, String inscomcode);
	
	/**
	 * 支付/二次支付成功推送工作流
	 */
	public String paySuccess(String instanceid, String operator, String forname, String maininstanceid, String inscomcode);
	
	/**
	 * 承保打单成功推送工作流
	 */
	public String undwrtSuccess(String instanceid, String operator, String forname, String inscomcode);
	
	/**
	 * 承保打单配送成功,推工作流
	 */
	public String unwrtDeliverySuccess(String instanceid, String operator, String forname, String inscomcode);

	/**
	 * 配送成功,推工作流
	 */
	public String deliverySuccess(String instanceid, String operator, String inscomcode);

	/**
	 * 支付失败
	 */
	public String underwritefail(String instanceid);
	
	/**
	 * 退回修改, 推工作流
	 */
	public String callBackInterface(String instanceid, String operator, String forname);
	
	/*
	 * 精灵或edi转人工
	 */
	public  String completeTaskWorkflow(String ruleResult, String processinstanceid, String userid, String taskName, String taskCode);
	
	/**
	 * 强转人工
	 */
	public String pushManualInterface(String instanceId, String operator);
}
