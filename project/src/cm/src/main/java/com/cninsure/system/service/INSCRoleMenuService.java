package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCRoleMenu;

public interface INSCRoleMenuService extends BaseService<INSCRoleMenu> {
	/**
	 * 得到当前角色菜单ids 集合A
	 * 得到以前菜单ids 集合B
	 * 计算分别 后进行新增删除
	 * @param session
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	public Map<String, String> repairRoleMenu(HttpSession session,String roleId, String menuIds);
	
	
	/**
	 * 得到当前角色所有菜单权限
	 * @param roleId
	 * @return
	 */
	public List<INSCRoleMenu> queryMenusByRoleId(String roleId);

	@Deprecated
	public List<INSCRoleMenu> queryAll();
	
	
}
