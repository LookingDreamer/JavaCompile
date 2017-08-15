package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.entity.INSBWorkflowmain;

@Repository
public class INSBWorkflowmainDaoImpl extends BaseDaoImpl<INSBWorkflowmain> implements
		INSBWorkflowmainDao {

	@Override 
	public INSBWorkflowmain selectByInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectIdByInstanceId"), instanceid);
	}

	@Override
	public long getMyTaskTotals(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getMyTaskCount"), map);
	}

	@Override
	public List<Map<String, Object>> getMyTaskInPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("getMyTaskInPage", map);
	}
	
	@Override
	public List<INSBQuotetotalinfo> selectCarList(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("selectCarListCount", param);
	}

	
	/*
	 *手机端接口， 查询我的订单接口的实现 
	 */
	@Override
	public List<Map<String, Object>> getMyOrderList(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderList"),queryParams);
	}
	@Override
	public List<Map<String, Object>> getMyOrderListForMinizzb(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderListForMinizzb"),queryParams);
	}

	@Override
	public List<Map<String, Object>> getMyOrderListForChn(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderListForChn"), queryParams);
	}

	@Override
	public long getMyTaskSimpleTotals(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getMyTaskCount"), map);
	}

	@Override
	public INSBWorkflowmain selectINSBWorkflowmainByInstanceId(String instanceid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("instanceid", instanceid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("select"), map);
	}

	@Override
	public void deleteByInstanceId(String instanceid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByInstanceId"), instanceid);
	}

	@Override
	public List<Map<String, Object>> selectOperatorCount(List<String> userIds) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOperatorCount"), userIds);
	}

	@Override
	public Long selectPayment(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("selectPayMentListCount", param);
	}

	@Override
	public List<Map<String, Object>> getQuoteInfoByTaskId(
			String processInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteInfoByTaskId"), processInstanceId);
	}

	/**
	 * 得到流程示意图主流程信息
	 */
	@Override
	public List<Map<String, Object>> getMainWorkflowViewInfo(String instanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMainWorkflowViewInfo"), instanceId);
	}
	
	
	@Override
	public long myOrderCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("myOrderCount", param);
	}
	@Override
	public long myOrderCountForMinizzb(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("myOrderCountForMinizzb", param);
	}
	@Override
	public long myOrderCountForChn(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("myOrderCountForChn", param);
	}

	@Override
	public List<Map<String, Object>> getMyOrderList2Pay(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderList2Pay"), queryParams);
	}

	@Override
	public List<Map<String, Object>> getMyOrderList2PayForMinizzb(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderList2PayForMinizzb"),queryParams);
	}
	
	@Override
	public List<Map<String, Object>> getMyOrderList2PayForChn(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderList2PayForChn"), queryParams);
	}

	@Override
	public long myOrderCount2Pay(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("myOrderCount2Pay", param);
	}

	@Override
	public Map<String, String> selectIdByInstanceId4Task(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectIdByInstanceId4Task"),instanceid);
	}

	@Override
	public List<Map<String, Object>> getMyOrderListNew(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderListNew"), queryParams);
	}
	@Override
	public List<Map<String, Object>> getMyOrderListNewForMinizzb(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderListNewForMinizzb"),queryParams);
	}
	@Override
	public List<Map<String, Object>> getMyOrderListNewForChn(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMyOrderListNewForChn"), queryParams);
	}

	@Override
	public List<INSBWorkflowmain> getDataByGroupId4UserLogin(List<String> groupIds) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getDataByGroupId4UserLogin"), groupIds);
		
	}
	
	@Override
	public String selectaskcode(String taskid){
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectaskcode"),taskid);
	}
}