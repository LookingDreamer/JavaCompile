package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBTasksetscope;

public interface INSBTasksetscopeDao extends BaseDao<INSBTasksetscope> {
	
	/**
	 * 
	 * 通过任务组得到区域列表 带分页
	 * @param taksetid
	 * @return
	 */
	public List<Map<String,Object>> selectScopListByTaskSetId(Map<String,Object> map);
	
	
	/**
	 * 得到总数量
	 * @param taksetid
	 * @return
	 */
	public long selectScopListCountByTaskSetId(String taksetid);
	
	
	/**
	 * 按网点id批量删除区域信息
	 * 
	 * @param deptIds
	 */
	public void deleteBatchByDeptIds(String[] deptIds);
	
	/**
	 * 
	 * 通过网点得到对应任务组
	 * @param deptid
	 * @return
	 */
	public List<String> selectTaskSetIdByDeptId(String deptid);
	/**
	 * 通过网点和任务组id得到关联的数量
	 * @param map
	 * @return
	 */
	public int selectScopListCountByDeptid(Map<String,Object> map);

}