package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBGrouptaskset;

public interface INSBGrouptasksetDao extends BaseDao<INSBGrouptaskset> {
	
	/**
	 * 通过群组id查询关系表信息
	 * 
	 * @param groupid
	 * @return
	 */
	public List<INSBGrouptaskset> selectByGroupId(String groupid);
	
	
	
	/**
	 * 按条件查询带分页
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBGrouptaskset> selectPageByParam(Map<String,Object> map);
	
	
	/**
	 * 分页总数
	 * 
	 * @param map
	 * @return
	 */
	public long selectPageCountByParam(Map<String,Object> map);
	
	
	/**
	 * 通过业务组查询所有任务组
	 * 
	 * @param tasksetid
	 * @return
	 */
	public List<INSBGrouptaskset> selectByTaskSetId(String tasksetid);
	
	/**
	 * 通过业管组删除关系表数据
	 * @param groupid
	 */
	public void deleteByGroupId(String groupid);
	
	
	/**
	 * 通过任务组id找到所有群组id
	 * @param ids
	 * @return
	 */
	public List<String> selectGroupIdsByTaskSetId4Task(List<String> ids);

}