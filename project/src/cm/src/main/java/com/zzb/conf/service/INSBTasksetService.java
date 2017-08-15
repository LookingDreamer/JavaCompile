package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBTaskset;

public interface INSBTasksetService extends BaseService<INSBTaskset> {
	
	/**
	 * 带分页 初始化列表页面
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryByParamPage(String tasksetkind,
			String tasksetname, String setstatus, Map<String, Object> map);
	
	
	/**
	 * 新增或修改
	 * 
	 * @param set
	 */
	public void saveOrUpdate(INSCUser user,INSBTaskset set);
	
	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public void deleteByIds(String ids);
	
	
	/**
	 * 批量修改任务组状态
	 * 
	 * @param ids
	 */
	public void updateStatusByIds(String ids);
	
	/**
	 * 保存任务组也管组关系表
	 * 
	 * @param groupIds
	 * @param tasksetId
	 */
	public void saveTasksetGroup(INSCUser user,String groupIds,String tasksetId);
	
	
	/**
	 * 初始化业管组列表页面
	 * 
	 * @param tasksetId
	 * @return
	 */
	public Map<String,Object> queryData2groupList(String tasksetId);
	
	/**
	 * 初始化规则组列表页面
	 * 
	 * @param tasksetId
	 * @return
	 */
	public Map<String, Object> queryData2ruleList(String tasksetId);
	
	/**
	 * 根据机构ID获得任务组列表
	 * 
	 * @param deptid
	 * @return
	 */
	public List<INSBTaskset> findListByDeptId(String deptid);
	
	/**
	 * 转到新增页面前初始化数据
	 * 
	 * @return
	 */
	public Map<String, Object> initMian2addData();
	
	/**
	 * 初始化编辑页面数据
	 * 
	 * @return
	 */
	public Map<String, Object> initMain2editData(String id);
	
	/**
	 * 为任务添加规则
	 * 
	 * @param groupIds
	 * @param tasksetId
	 */
	public void saveTasksetRuleBase(INSCUser user,String ruleIds, String tasksetId);
	
	
	/**
	 * 为工作流过滤当前在线业管
	 * 
	 * @param param
	 * @return
	 */
	public Map<String,String> selectOnlineUser4WorkFlow(Map<String,String> param);
	
	
	/**
	 * 得到供应商树
	 * 
	 * @param id 任务id
	 * @param parentcode
	 * @param providerIds
	 * @param checked
	 * @return
	 */
	public List<Map<String, String>> getProviderTreeList(String id,String parentcode,
			String providerIds, String checked);
	
	/**
	 * 
	 * 通过父节点得到机构子节点数据
	 * @param pid
	 * @return
	 */
	public List<Map<String,String>> getdeptbypid(String pid);
	
	
	/**
	 * 
	 * 保存当前任务组对应网点
	 * 
	 * @param tsksetId
	 * @param deptId
	 * @return
	 */
	public void saveTaskSetScop(INSCUser user,String tsksetId,String deptId);
	
	
	/**
	 * 通过任务组id得到区域数据
	 * 
	 * @param tasksetId
	 * @return
	 */
	public Map<String,Object> getScopListByTaskSetId(Map<String, Object> map,String tasksetId);
	
	
	/**
	 * 通过网点id删除任务组区域信息
	 * 
	 * @param deptIds
	 */
	public void removeTaskSetScop(String deptIds);
	
	
	
	/**
	 * 精灵续保查询得到业管code
	 * @param providerId
	 * @return
	 */
	public Map<String,String> getRenewalUserData(String deptId);
	
	/**
	 * 
	 * 平台查询
	 * @param providerId
	 * @return
	 */
	public Map<String,String> getPlatformData(String deptId);
	
}