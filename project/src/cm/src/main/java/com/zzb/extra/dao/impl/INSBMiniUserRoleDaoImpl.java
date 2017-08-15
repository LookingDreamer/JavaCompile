package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.dao.INSBMiniUserRoleDao;
import com.zzb.extra.entity.INSBAgentUser;
import com.zzb.extra.entity.INSBMiniUserRole;
@Repository
public class INSBMiniUserRoleDaoImpl extends BaseDaoImpl<INSBMiniUserRole> implements INSBMiniUserRoleDao {



	@Override
	public INSBMiniUserRole findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveObject(INSBMiniUserRole insbMiniUserRole) {
		this.sqlSessionTemplate.insert("INSBMiniUserRole_insert",insbMiniUserRole);
		
	}

	@Override
	public void updateObject(INSBMiniUserRole insbMiniUserRole) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.update("INSBMiniUserRole_updateById", insbMiniUserRole);
	}

	@Override
	public void deleteObect(String id) {
	
		this.sqlSessionTemplate.delete("INSBMiniUserRole_deleteById", id);
		
	}

	@Override
	public List<INSBAgentUser> selectAgentUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniUserRole_selectAllAgentUser", map);
	}

	@Override
	public INSBAgentUser selectByOpenid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniUserRole_selectAllAgentUser", map);
	}

	@Override
	public List<Map<String, Object>> selectRoleByUserid(String userid) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniUserRole_selectByuserId", userid);
	}

	@Override
	public List<Map<String, Object>> selectRoleByRoleid(String roleid) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniUserRole_selectByRoleId", roleid);
	}

	@Override
	public INSBMiniUserRole queryObjectByUserid(String miniuserid) {
		// TODO Auto-generated method stub
		
		return this.sqlSessionTemplate.selectOne("INSBMiniUserRole_queryByuserId", miniuserid);
	}

	@Override
	public List<INSBMiniUserRole> selectUserByRoleId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniUserRole_select", map);
	}

	@Override
	public long selectCountUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniUserRole_selectAllCount", map);
	}

	@Override
	public INSBMiniUserRole queryObjectByUidAndRoleid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniUserRole_select", map);
	}

	@Override
	public void updateMiniObject(INSBAgent agent) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.update("INSBMiniAgent_updateById", agent);
	}

	

}
