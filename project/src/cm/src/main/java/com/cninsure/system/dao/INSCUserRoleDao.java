package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.exception.DaoException;
import com.cninsure.system.entity.INSCUserRole;

public interface INSCUserRoleDao extends BaseDao<INSCUserRole> {
	public List<String> selectRoleidByUserid(String userId) throws DaoException;
	public String queryRoleIdsByUid(String id);
	/**
	 * 根据用户id，角色id，查询用户角色关系表id
	 * param userId
	 * param roleId
	 * @return
	 */
	public String selectOneByUidAndRid(Map<String, String> map);
	
	
	public List<INSCUserRole> selectUsersByRoleId(String roleId);
	
	
	/**
	 * 带分页查询
	 * 
	 * param roleId
	 * @return
	 */
	public List<INSCUserRole> selectPageUsersByRoleId(Map<String,Object> map);
	/**
	 * 带分页查询数目
	 * 
	 * param roleId
	 * @return
	 */
	public long selectPageCountUsersByRoleId(Map<String,Object> map);
	
	/**
	 * 在userid   roleid 作为逻辑主键 可以确定唯一一条数据
	 * 
	 * param ids
	 * @return
	 */
	public int deleteByUserIdRoleId(Map<String,String> urId);
}
