package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.entity.INSBWorkflowmain;

public interface INSBWorkflowmainDao extends BaseDao<INSBWorkflowmain> {
	
	/**
	 * 通过流程id查询主键
	 * 
	 * @param flowid
	 * @return id,createby,taskid,taskcode 
	 */
	public INSBWorkflowmain selectByInstanceId(String instanceid); 

	/**
	 * 获取当前业管的任务的总条数
	 */
	public long getMyTaskTotals(Map<String, Object> map);
	/**
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMyTaskInPage(Map<String, Object> map);
	/**
	 * 手机端接口， 查询我的订单接口的实现 
	 * wangyang
	 */
	public List<Map<String,Object>> getMyOrderList(Map<String ,Object> queryParams);
	public List<Map<String,Object>> getMyOrderListForMinizzb(Map<String ,Object> queryParams);
	public List<Map<String, Object>> getMyOrderListForChn(Map<String, Object> queryParams);

	public List<Map<String,Object>> getMyOrderListNew(Map<String ,Object> queryParams);
	public List<Map<String,Object>> getMyOrderListNewForMinizzb(Map<String ,Object> queryParams);
	public List<Map<String, Object>> getMyOrderListNewForChn(Map<String, Object> queryParams);

	public List<INSBQuotetotalinfo> selectCarList(Map<String, Object> param);
	
	
	public Long selectPayment(Map<String, Object> param);

	/**
	 * 我的任务简单搜索总条数
	 * @param map
	 * @return
	 */
	public long getMyTaskSimpleTotals(Map<String, Object> map);
	/**
	 * 通过流程id查询单个对象
	 * @param instanceid
	 * @return
	 */
	public INSBWorkflowmain selectINSBWorkflowmainByInstanceId(String instanceid);

	/**
	 *根据bean查找
	 */
	//public List<INSBWorkflowmain> SelectList(String jobNum);
	
	/**
	 * 
	 * 根据实例id删除流程节点状态
	 * @param instanceid
	 */
	public void deleteByInstanceId(String instanceid);
	
	
	/**
	 * 通过业管id查询传入业管处理任务数
	 * 
	 * @return
	 */
	public List<Map<String,Object>> selectOperatorCount(List<String> userIds);

	/**
	 * 根据taskid查询报价阶段的报价信息
	 * @param processInstanceId
	 * @return
	 */
	public List<Map<String, Object>> getQuoteInfoByTaskId(String processInstanceId);
	
	/**
	 * 得到流程示意图主流程信息
	 */
	public List<Map<String, Object>> getMainWorkflowViewInfo(String instanceId);
	
	public long myOrderCount(Map<String, Object> param) ;
	public long myOrderCountForMinizzb(Map<String, Object> param) ;
	public long myOrderCountForChn(Map<String, Object> param);

	public List<Map<String,Object>> getMyOrderList2Pay(Map<String ,Object> queryParams);
	public List<Map<String,Object>> getMyOrderList2PayForMinizzb(Map<String ,Object> queryParams);
	public List<Map<String,Object>> getMyOrderList2PayForChn(Map<String, Object> queryParams);

	public long myOrderCount2Pay(Map<String, Object> param) ;
	
	/**
	 * 
	 * 通过实力id得到 uuid
	 * @param instanceid
	 * @return
	 */
	public Map<String, String> selectIdByInstanceId4Task(String instanceid);
	
	/**
	 * 通过群组查找当前未分配的任务
	 * @return
	 */
	public List<INSBWorkflowmain> getDataByGroupId4UserLogin(List<String> groupIds);
	/**
	 * 通过主流程查询taskcode
	 */
	public String selectaskcode(String taskid);
}