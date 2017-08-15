package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.core.exception.ServiceException;
import com.cninsure.system.entity.INSCUserRole;

public interface INSCUserRoleService extends BaseService<INSCUserRole> {
	public List<String> selectRoleidByUserid(String userid)
			throws ServiceException;

	// 根据用户id查询用户拥有的角色逗号分隔
	public String queryRoleIdsByUid(String id);

	// 根据用户id，角色id，插入用户角色关系表
	public void insertUserRole(String userId, String roleids);

	/**
	 * 通过角色id得到所有用户信息
	 * 
	 * 通过用户信息得到当前用户所属所有角色信息
	 * 
	 * 客户端分页
	 * 
	 * @param roleId
	 * @return
	 */
	public Map<String, Object> getUsersByRoleid(Map<String, Object> map);
	
	
	public Map<String,Object> saveUsersByRoleId(String userIds,String roleId);
}
