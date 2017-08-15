package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRuleBase;
import com.zzb.conf.entity.INSBTasksetrulebse;

/**
 * Union代表关系表查询
 * 
 * 剩下代表规则查询
 * 
 * @author Administrator
 *
 */
public interface INSBTasksetrulebseDao extends BaseDao<INSBTasksetrulebse> {

	/**
	 * 
	 * 带分页查询所有数据
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBRuleBase> selectListPage(Map<String, Object> map);

	/**
	 * 
	 * 查询条数
	 * 
	 * @param map
	 * @return
	 */
	public long selectListCountPage(Map<String, Object> map);

	/**
	 * 根据任务组id查出所有数据
	 * 
	 * @param tasksetId
	 * @return
	 */
	public List<INSBTasksetrulebse> selectByTaskSetId(String tasksetId);

	/**
	 * 根据规则id 查询数据
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<INSBTasksetrulebse> selectByRuleId(String ruleId);

	/**
	 * 得到所有已经关联的规则信息
	 * 
	 * @return
	 */
	public List<String> selectList4Rule();


	public void deleteUnionByRuleId(String ruleId);
	
	/**
	 * 
	 * 通过规则查询任务组信息
	 * @param map
	 * @return
	 */
	public List<String> selectListPage4Taskset(Map<String,Object> map);
	
	
}