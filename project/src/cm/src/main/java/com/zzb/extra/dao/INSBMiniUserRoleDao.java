package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.entity.INSBAgentUser;
import com.zzb.extra.entity.INSBMiniUserRole;

public interface INSBMiniUserRoleDao extends BaseDao<INSBMiniUserRole> {
	
	public INSBMiniUserRole  findById(String id);
	
	public void saveObject(INSBMiniUserRole insbMiniUserRole);
	
	public void updateObject(INSBMiniUserRole insbMiniUserRole);
	
	public void deleteObect(String id);
	
	public INSBMiniUserRole queryObjectByUserid(String userid);
	
	public List<INSBAgentUser> selectAgentUser(Map<String, Object> map);
	
	public List<INSBMiniUserRole> selectUserByRoleId(Map<String, Object> map);
	
	public long selectCountUser(Map<String, Object> map);
	
	public INSBAgentUser selectByOpenid(Map<String, Object> map);
	
	public List<Map<String, Object>> selectRoleByUserid(String userid);
	
	public List<Map<String, Object>> selectRoleByRoleid(String roleid);
	
	public INSBMiniUserRole queryObjectByUidAndRoleid(Map<String, Object> map);
	
	public void updateMiniObject(INSBAgent agent);
	//public List<INSBMiniRole> 
}