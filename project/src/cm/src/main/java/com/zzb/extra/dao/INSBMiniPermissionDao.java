package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMiniPermission;

public interface INSBMiniPermissionDao extends BaseDao<INSBMiniPermission> {

	
	
	public INSBMiniPermission  findById(String id);
	
	
	public void saveObject(INSBMiniPermission insbMiniPermission);
	
	public void updateObject(INSBMiniPermission insbMiniPermission);
	
	public void deleteObect(String id);
	
	public long queryCountPermission(Map<String, Object> map);
	
	public long queryCountPermissionList(Map<String, Object> map);
	
	public List<Map<String,Object>> queryPermissionList(Map<String, Object> map);
	
	public List<INSBMiniPermission> queryPermission(Map<String, Object> map);
	
	public Long selectMaxPercode(Map<String, Object> map);
	
	public List<Map<Object, Object>> queryMiniOrderList(Map<String, Object> map);
	
	public Long queryAgentOrderByUseridCount(Map<String, Object> agentid);
	
	//public List<INSBMiniRole> 
}