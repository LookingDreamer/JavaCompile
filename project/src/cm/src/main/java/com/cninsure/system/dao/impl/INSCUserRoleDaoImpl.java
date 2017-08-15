package com.cninsure.system.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.exception.DaoException;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.entity.INSCUserRole;

@Repository
public class INSCUserRoleDaoImpl extends BaseDaoImpl<INSCUserRole> implements
		INSCUserRoleDao {

	@Override
	public List<String> selectRoleidByUserid(String userid) throws DaoException {
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("selectRoleidByUserid"), userid);
	}

	@Override
	public String queryRoleIdsByUid(String id) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("queryRoleIdsByUid"), id);
	}

	@Override
	public String selectOneByUidAndRid(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectOneByUidAndRid"), map);
	}

	@Override
	public List<INSCUserRole> selectUsersByRoleId(String roleId) {
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("queryUsersByRoleId"), roleId);
	}

	@Override
	public int deleteByUserIdRoleId(Map<String, String> urId) {
		return this.sqlSessionTemplate.delete(
				this.getSqlName("deleteByUserIdRoleId"), urId);
	}

	@Override
	public List<INSCUserRole> selectPageUsersByRoleId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryPageUsersByRoleId"), map);
	}

	@Override
	public long selectPageCountUsersByRoleId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryPageCountUsersByRoleId"), map);
	}

}
