package com.cninsure.system.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.exception.DaoException;
import com.cninsure.system.dao.INSCRoleDao;
import com.cninsure.system.entity.INSCRole;

@Repository
public class INSCRoleDaoImpl extends BaseDaoImpl<INSCRole> implements
		INSCRoleDao {

	@Override
	public List<String> selectRolecodesByRoleids(List<String> roleidList)
			throws DaoException {
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("selectRolecodesByRoleids"), roleidList);
	}

	@Override
	public List<Map<Object, Object>> selectRoleLitByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRoleistByMap"), map);
	}

	@Override
	public String selectRoleNameById(String id) {
		return this.sqlSessionTemplate.selectOne("com.cninsure.system.entity.INSCRole_selectRoleNameByRoleid", id);
	}

	@Override
	public List<Map<String,Object>> selectAllList() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllList"));
	}

	@Override
	public List<INSCRole> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

}
