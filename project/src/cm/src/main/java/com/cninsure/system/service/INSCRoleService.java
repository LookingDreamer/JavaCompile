package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.core.exception.ServiceException;
import com.cninsure.system.entity.INSCRole;

public interface INSCRoleService extends BaseService<INSCRole> {

	public List<String> selectRolecodesByRoleids(List<String> rolecodeList)
			throws ServiceException;

	public Map<String, Object> showRoleList(Map<String, Object> map);

	/**
	 * 先检查 角色菜单关系表
	 * 
	 * 有关联关系（包括使用，停用） 提示 先解除关系再删除 无关联关系（提示成功 删除）
	 * 
	 * @param model
	 * @return
	 */
	public int deleteRoleById(String roldId);

	/**
	 * 
	 * 
	 * @param roldId
	 * @return
	 */
	public Map<String, String> updateRoleById(INSCRole model);

	@SuppressWarnings("unchecked")
	public INSCRole queryById(String id);

	/**
	 * 初始化所有树
	 * 
	 * 得到当前角色关联树节点
	 * 
	 * @param roleId
	 */
	public List<Map<String, String>> initRoleTree(String roleId);

	/**
	 * 批量删除角色信息 有关联关系（包括使用，停用） 提示 不删除 无关联关系（提示成功 删除）
	 */
	public Map<String, String> deleteRoleByIds(String roleIds);

	/**
	 * 批量删除用户信息
	 * 
	 */
	public Map<String, String> deleteUsersByRoleId(String userIds, String roleId);

	/**
	 * 得到当前角色未绑定用户用户信息
	 * 
	 * @param roleId
	 * @return
	 */
	public Map<String, Object> findRmainUsersByIds(Map<String,Object> param);
	/**
	 * 
	 * @return
	 */
	@Deprecated
	public List<INSCRole> queryAll();
}
