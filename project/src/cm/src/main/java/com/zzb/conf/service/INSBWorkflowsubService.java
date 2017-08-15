package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBWorkflowsub;

public interface INSBWorkflowsubService extends BaseService<INSBWorkflowsub> {

	/**
	 * 新增或者修改工作流子流程状态
	 */
	public void updateWorkFlowSubData(String mainTaskId, String subTaskId, String taskCode, String taskState, String taskName, String taskFeedBack, String operator);

	/**
	 * 新增或者修改工作流子流程状态
	 * 
	 * @param workflowModel
	 */
	public void updateWorkFlowSubData(INSBWorkflowsub workflowModel,String fromOperator);
	
	
	/**
	 * 结束实例时 删除sub表状态，更新轨迹表
	 * 
	 * @param instanceId
	 */
	public void deleteWorkFlowSubData(String instanceId);
	
	/**
	 * 通过子流程表中 主流程instanceid 查找所有子流程实例id
	 * 
	 * @param instanceId
	 * @return
	 */
	public List<String> queryInstanceIdsByMainInstanceId(String instanceId);

	/**
	 * 车险任务查看任务流程
	 */
	public Map<String, Object> showWorkflowTrack(String instanceId, String inscomcode);
	
	/**
	 * 
	 * 通过主实例id 得到子实例id和供应商id
	 * @param instanceId
	 * @return
	 */
	public Map<String,String> getDataByMainInstanceId(String instanceId);


	/**
	 * 获取二次支付任务的支付信息
	 * @param string
	 */
	public Map<String,Object> getMediumPayInfo(Map<String, Object> map);


	/**
	 * @param maininstanceid
	 * @return
	 */
	public String getInstanceidByMaininstanceId(String maininstanceid);


	public String getTransformTaskInstanceid(Map<String, String> map);
	public List<INSBWorkflowsub> selectSubModelByMainInstanceId(String mainInstanceId);
	public List<Map<String, Object>> selectSubModelInfoByMainInstanceId(String mainInstanceId);
}