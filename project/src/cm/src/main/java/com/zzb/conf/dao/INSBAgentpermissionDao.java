package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgentpermission;

public interface INSBAgentpermissionDao extends BaseDao<INSBAgentpermission> {
	/**
	 * 代理人权限关系表 通过逻辑主键得到
	 * 
	 * @param map
	 * @return
	 */
	public INSBAgentpermission selectListByLogicId(Map<String,String> map);
	
	/**
	 * 根据代理人id 删除所有代理人权限关系
	 * 
	 * @param agentId
	 * @return
	 */
	public int deleteByAgentId(String agentId);
	
	
	/**
	 * 查询代理人权限信息
	 * 
	 * @param agentId
	 * @return
	 */
	public List<INSBAgentpermission> selectByAgentId(String agentId);

	/**
	 * 查询代理人试用权限
	 * @param map
	 * @return
     */
	public INSBAgentpermission selectByAgentIdAndName(Map<String,String> map);

	/**
	 * 
	 * 查询当前代理人是否分配权限
	 * @param agentId
	 * @return
	 */
	public long  selectCountByAgentId(String agentId);

	/**
	 * 更新试用用户试用权限使用次数（剩余次数）
	 * @param agentpermission
	 * @return
     */
	public int updateSurplusnum(INSBAgentpermission agentpermission);

	public void insertBatch(List<INSBAgentpermission> list);

	public int deleteByDeptinnercode(String deptinnercode);
	
	/**
	 * 批量插入数据
	 */
	public void insertAll(List<INSBAgentpermission> list);
	
}