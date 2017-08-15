package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.zzb.conf.entity.INSBRuleBase;

public interface INSBRuleBaseDao {

	/**
	 * 通过id查询规则信息
	 * @param id
	 * @return
	 */
	public  INSBRuleBase selectById(String id);
	
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
	 * 查询规则匹配的任务组
	 * 
	 * @param map
	 * @return
	 */
	public List<String> selectTasksetidList(Map<String, Object> map);
	
	/**
	 * 查询count
	 * @return
	 */
	public Long selectCount();
	
	/**
	 * 分页查询规则表
	 * @param map
	 * @return
	 */
	public List<Map<Object, Object>> selectRuleBaseListPaging(Map<String, Object> map);
}