package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBTaskset;

public interface INSBTasksetDao extends BaseDao<INSBTaskset> {

	/**
	 * 带分页按条件查询
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBTaskset> selectListByPage(Map<String, Object> map);

	/**
	 * 带分页按条件查询总条数
	 * @param map
	 * @return
	 */
	public long selectListCountByPage(Map<String, Object> map);
	
	public List<INSBTaskset> selectListByDeptId(String deptid);
	
	/**
	 * 
	 * 得到启用的任务组
	 * @param id
	 * @return
	 */
	public INSBTaskset selectOnUseById(String id);
	
	
	/**
	 * 
	 * 通过保险公司查询任务组id
	 * @return
	 */
	public String selectTaskSetIdByProviderId(String providerid);
	
	/**
	 * 
	 * 根据供应商和任务类型的到业管信息
	 * @param map
	 * @return
	 */
	public List<String> selectTaskSetIdByProviderIdTaskType(Map<String,String> map);
	
	/**
	 * 
	 * 通过任务类型查询任务组
	 * @param taskType
	 * @return
	 */
	public List<String> selectIdByTaskSetType(String taskType);
	
	
	
}