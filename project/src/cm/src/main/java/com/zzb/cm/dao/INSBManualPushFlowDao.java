package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.controller.vo.ManualPushFlowVo;
import com.zzb.conf.entity.INSBPolicyitem;

public interface INSBManualPushFlowDao extends BaseDao<INSBPolicyitem> {
	/** 
	 * 任务列表 	杨威   2016/7/1
	 */
	public List<Map<String,Object>> showTaskList(Map<String,Object> param);
	
	/**
	 * 任务列表数目 	杨威
	 */
	public Long showTaskListCount(Map<String,Object> param);
	
	/**
	 * 查询当前工作流的forname
	 */
	public Map<String,Object> queryTaskByProcessinstanceid(String instanceid);

	/**
	 * 查询当前工作流数据
	 */
	public Map<String, Object> getTaskByProcessinstanceid(String instanceid);

	/**
	 * 查询当前主流程与对应工作流的状态
	 */
	public Map<String, Object> queryMainTaskByMaininstanceid(String mainInstanceId);

	/**
	 * 查询当前子流程与对应工作流的状态
	 */
	public Map<String, Object> querySubTaskBySubinstanceid(String subInstanceId);
	
	/**
	 * 获取工作流跟cm状态不一致的任务
	 * @param param
	 * @return
	 */
	public List<ManualPushFlowVo> queryErrorStateTasks(Map<String, Object> param);
}
