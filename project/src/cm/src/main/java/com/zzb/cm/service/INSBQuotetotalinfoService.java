package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBQuotetotalinfo;

public interface INSBQuotetotalinfoService extends BaseService<INSBQuotetotalinfo> {
  
	/**
	 * 根据流程实例id
	 * 查询非报价期间的保险公司列表
	 */
	public List<String> getInscomcodeListByInstanceId(String instanceId,String inscomcode);

	List<Map<String, Object>> getInscomInfo(String taskid);
	/**
	 * 人工调整页面调整完成页面按钮
	 */
	public String adjustcomplete(String processInstanceId,String result);
	
	/**
	 * 根据流程实例id、任务节点状态编码、登录用户的code
	 * 查询报价期间的保险公司列表
	 */
	public List<String> getInscomcodeListByInstanceId(String instanceId, String taskcode, String usercode);
	/**
	 * 流程图： 代理人信息
	 */
	public Map<String,Object> getAgentInfoByTaskId(String taskid);

	/**
	 * 通过业管查找订单列表
	 * @param map
	 * @return
	 */
	public String getQuotetotalinfoByUserid(Map<String, Object> map);
	
	/**
	 * 根据流程实例id
	 * 查询报价期间的保险公司列表
	 */
	public List<String> getInscomcodeListByInstanceId(String taskid);

	
	/**
	 * 条件查询订单列表
	 * @param map
	 * @return
	 */
	public String getQuotetotalinfoByParams(Map<String, Object> map);
	/**
	 * 根据任务号查询保险公司code，name
	 * @param taskid
	 * @param userCode 
	 * @return
	 */
	public List<Map<String, String>> getInscomcodeAndNameListByInstanceId(String taskid, String taskCode, String userCode);
}