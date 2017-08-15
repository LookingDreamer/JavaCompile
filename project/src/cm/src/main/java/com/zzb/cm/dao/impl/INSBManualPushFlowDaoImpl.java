package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.controller.vo.ManualPushFlowVo;
import com.zzb.cm.dao.INSBManualPushFlowDao;
import com.zzb.conf.entity.INSBPolicyitem;
/**
 * 人工推送工作流
 * @author 杨威 	2016/7/1
 */
@Repository
public class INSBManualPushFlowDaoImpl extends BaseDaoImpl<INSBPolicyitem> implements INSBManualPushFlowDao{
	
	/**
	 * 任务列表 	杨威   2016/7/1
	 */
	@Override
	public List<Map<String, Object>> showTaskList(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("insbPushWorkflow.getListByMap", param);
	}
	
	/**
	 * 任务列表数目	杨威
	 */
	@Override
	public Long showTaskListCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("insbPushWorkflow.getCountByMap",param);
	}
	
	/**
	 * 查询当前工作流的forname(即taskcode)
	 */
	@Override
	public Map<String, Object> queryTaskByProcessinstanceid(String instanceid) {
		return this.sqlSessionTemplate.selectOne("insbPushWorkflow.queryTaskByInstanceid",instanceid);
	}

	/**
	 * 查询当前工作流数据
	 */
	@Override
	public Map<String, Object> getTaskByProcessinstanceid(String instanceid) {
		return this.sqlSessionTemplate.selectOne("insbPushWorkflow.getTaskByInstanceid",instanceid);
	}

	/**
	 * 查询当前 主流程与对应工作流的状态
	 */
	@Override
	public Map<String, Object> queryMainTaskByMaininstanceid(String mainInstanceId) {
		return this.sqlSessionTemplate.selectOne("insbPushWorkflow.queryMainAndTask", mainInstanceId);
	}

	/**
	 * 查询当前 子流程与对应工作流的状态
	 */
	@Override
	public Map<String, Object> querySubTaskBySubinstanceid(String subInstanceId) {
		return this.sqlSessionTemplate.selectOne("insbPushWorkflow.querySubAndTask", subInstanceId);
	}
	
	/**
	 * 错误工作流任务列表 	askqvr   2017/5/15
	 */
	@Override
	public List<ManualPushFlowVo> queryErrorStateTasks(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("insbPushWorkflow.queryErrorStateTasks", param);
	}
}
