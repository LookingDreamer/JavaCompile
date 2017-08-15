package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.dao.INSBMiniRoleDao;
import com.zzb.extra.entity.INSBMiniRole;
import com.zzb.extra.model.INSBAgentUserQueryModel;
@Repository
public class INSBMiniRoleDaoImpl extends BaseDaoImpl<INSBMiniRole>  implements INSBMiniRoleDao {

	@Override
	public INSBMiniRole findById(String id) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniRole_selectById", id);
	}

	@Override
	public void saveObject(INSBMiniRole insbMiniRole) {
		
		this.sqlSessionTemplate.insert("INSBMiniRole_insert",insbMiniRole);
	}

	@Override
	public void updateObject(INSBMiniRole insbMiniRole) {
		this.sqlSessionTemplate.update("INSBMiniRole_updateById", insbMiniRole);
		
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBMiniRole_deleteById", id);
		
	}

	@Override
	public INSBMiniRole queryRoleByRolecode(Map<String, Object> rolecode) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniRole_select", rolecode);
	}

	@Override
	public List<INSBMiniRole> queryMiniRole(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("INSBMiniRole_select", map);
	}

	@Override
	public long queryCountRole(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("INSBMiniRole_AccountId", map);
		 
	}

	@Override
	public void updateAgentReferrer(INSBAgent model) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.update("INSBAgent_updateAgentById", model);
		
	}

	
}
