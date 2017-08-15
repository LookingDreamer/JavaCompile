package com.cninsure.system.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCRoleMenu;


public interface INSCRoleMenuDao extends BaseDao<INSCRoleMenu> {

	public void deleteByRoleIdMenuId(INSCRoleMenu model);
	
	public List<INSCRoleMenu> selectMenusByRoleId(String roleId);
	
	/**
	 * 
	 * 菜单初始化
	 * @param roleId
	 * @return
	 */
	public List<String> selectMenuIdsByRoleIds4Menu(List<String> roleIds);
	@Deprecated
	public List<INSCRoleMenu> selectAll();
}
