package com.zzb.conf.service;

import java.util.Map;

import com.zzb.conf.entity.INSBRuleBase;

public interface INSBRulebseService{

	/**
	 * 得到最外层组织机构信息
	 * 
	 * @return
	 */
	public Map<String,Object> getParentDeptData();
	/**
	 * 按条件得到规则列表
	 * 
	 * @param type	任务列表1   规则查询2
	 * @param ruletype	调度规则/权重规则
	 * @param param2	规则名称/规则描述 
	 * @param param3	value
	 * @param rulebaseStatus	规则状态
	 * @param deptId	组织机构
	 * @param map	分页信息
	 * @return
	 */
	public Map<String,Object> getListPageByParam(int type,Map<String, Object> tempMap,Map<String, Object> map);
	
	/**
	 * 通过规则id得到任务组列表
	 * 
	 * @param ruleid
	 * @return
	 */
	public Map<String,Object> initTasksetId(String ruleid);
	
	/**
	 * 初始化所有的规则信息
	 * @param para
	 * @return
	 */
	public Map<String, Object> initRuleBase(Map<String, Object> para);
	
	/**
	 * 根据id超找规则
	 * @param id
	 * @return
	 */
	public INSBRuleBase selectById(String id);
	/**
	 * 根据id超找规则名称
	 * @param id
	 * @return
	 */
	public String selectByagreementrule(String agreementrule);
}