package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;


public interface INSBRuleEngineDao {

	/**
	 * 获取city查询规则
	 * @return 
	 */
	public List<Map<String, Object>> selectListByCity(Map<String, Object> map);
	public List<Map<String,Object>> selectByParamMap(Map<String,Object> map);
	/**
	 * 获取id查询规则名称
	 * @return 
	 */
	public String getAgreementrulename(String agreementrule);
}