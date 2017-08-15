package com.cninsure.system.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCRoleMenuDao;
import com.cninsure.system.entity.INSCRoleMenu;


@Repository
public class INSCRoleMenuDaoImpl extends BaseDaoImpl<INSCRoleMenu> implements INSCRoleMenuDao {

	@Override
	public void deleteByRoleIdMenuId(INSCRoleMenu model) {

		this.sqlSessionTemplate.delete("com.cninsure.system.entity.INSCRoleMenu_deleteByRoleidMenuId",model);
	}

	@Override
	public List<INSCRoleMenu> selectMenusByRoleId(String roleId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByRoleId"),roleId);
		
	}

	@Override
	public List<String> selectMenuIdsByRoleIds4Menu(List<String> roleIds) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectMenuIdsByRoleIds4Menu"),roleIds);
	}

	@Override
	public List<INSCRoleMenu> selectAll() {
		return sqlSessionTemplate.selectList(getSqlName("select"));
	}

}
