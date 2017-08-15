package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.jobpool.Task;
import com.zzb.cm.controller.vo.MyTaskVo;
import com.zzb.conf.entity.INSBWorkflowmain;

public interface INSBWorkflowmainService extends BaseService<INSBWorkflowmain> {
	
	/**
	 * 新增或者修改工作流主流程状态
	 * 
	 * 同时更新主流程轨迹表
	 * 
	 * @param workflowModel 
	 */
	public void updateWorkFlowMainData(INSBWorkflowmain workflowModel,String fromOperator);
	
	public String getQuoteType(String taskid,String providerid,String quotecode);

	/**
	 * 获取当前业管的任务的总条数
	 * 
	 * @param myTaskVo
	 * @return
	 */
	public long getMyTaskTotals(MyTaskVo myTaskVo);
	/**
	 * 用分页方式查询
	 * @param map myTaskVo
	 * @return
	 */
	public List<Map<String,Object>> getMyTaskInPage(Map<String, Object> map);
	/**
	 * 用InstanceId查询INSBWorkflowmain
	 * @param taskid
	 * @return
	 */
	public INSBWorkflowmain selectByInstanceId(String taskid);
	
	
	/**
	 * 结束实例时 删除main表状态
	 * 
	 * @param instanceId
	 */
	public void deleteWorkFlowMainData(String instanceId);
	
	/*
	 * 订单管理页面任务申请任务接口
	 */
	public String interApplyForTask(String userid,String processinstanceid);
	
	/**
	 * 获取承保、核保方式
	 * @param taskid
	 * @param providerid
	 * @param lasttype
	 * @return
	 */
	public String getContracthbType(String taskid,String providerid,String lasttype,String type);
	
	/**
	 * 获取承保
	 * @param taskid
	 * @param providerid
	 * @param lasttype
	 * @return
	 */
	public String getContractcbType(String taskid,String providerid,String lasttype,String type);
	
	/**
	 * 任务新增遍历执行
	 * @param task
	 */
	public void addTask2Poll(Task task);
	
	
	/**
	 * 任务完成移除任务
	 * @param task
	 */
	public void removeTaskFromPool(Task task);

	/**
	 * 根据instanceid查询taskid
	 */
	public String selectaskcode(String taskid);

	public String getPTway(String taskid, String providerid);

	public String getGZway(String taskid, String providerid);
	
	/**
	 * TIP:规则平台查询只会发起一次
	 * 1  需要启动   0 不需要启动
	 * taskid +"@rulequery"  KEY  放入redis中，有值就证明规则平台查询已经启动
	 */
	public String getGZPTway(String taskid, String providerid) ;
	/**
	 * TIP:EDI平台查询只会发起一次
	 * 1  需要启动   0 不需要启动
	 * taskid +"@rulequery"  KEY  放入redis中，有值就证明规则平台查询已经启动
	 */
	public String getEDIPTway(String taskid, String providerid);
}