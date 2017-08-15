package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.entity.INSBMiniRole;
import com.zzb.extra.model.INSBAgentUserQueryModel;

public interface INSBMiniRoleDao extends BaseDao<INSBMiniRole> {
	
	public INSBMiniRole  findById(String id);
	
	public void saveObject(INSBMiniRole insbMiniRole);
	
	public void updateObject(INSBMiniRole insbMiniRole);
	
	public void deleteObect(String id);
	
	public INSBMiniRole queryRoleByRolecode(Map<String, Object> rolecode);
	
	public List<INSBMiniRole> queryMiniRole(Map<String, Object> map);
	
	public long queryCountRole(Map<String,Object> map);
	
	public void updateAgentReferrer(INSBAgent model);
	
	
	
	//public List<INSBMiniRole> 
}