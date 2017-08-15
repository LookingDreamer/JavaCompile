package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBRolePermission;

public interface INSBRolePermissionDao extends BaseDao<INSBRolePermission> {
	
	public INSBRolePermission  findById(String id);
	
	public void saveObject(INSBRolePermission insbRolePermission);
	
	public void updateObject(INSBRolePermission insbRolePermission);
	
	public void deleteObect(String id);
	
	public List<INSBRolePermission> queryRolePerByPerid(Map<String, Object> map);
	
	public List<INSBRolePermission> queryRperByRoleid(Map<String, Object> map);
	
	//public List<INSBMiniRole> 
}