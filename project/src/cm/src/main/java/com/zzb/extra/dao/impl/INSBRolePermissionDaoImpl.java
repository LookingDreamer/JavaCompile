package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBRolePermissionDao;
import com.zzb.extra.entity.INSBRolePermission;
@Repository
public class INSBRolePermissionDaoImpl extends BaseDaoImpl<INSBRolePermission> implements INSBRolePermissionDao {




	
	@Override
	public void saveObject(INSBRolePermission insbRolePermission) {
		this.sqlSessionTemplate.insert("INSBRolePermission_insert",insbRolePermission);
		
	}

	@Override
	public void updateObject(INSBRolePermission insbMiniPermission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBRolePermission_deleteById", id);
		
	}

	@Override
	public INSBRolePermission findById(String id) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBRolePermission_selectById", id);
	}

	@Override
	public List<INSBRolePermission> queryRolePerByPerid(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("INSBRolePermission_select", map);
	}

	@Override
	public List<INSBRolePermission> queryRperByRoleid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBRolePermission_select", map);
	}



	

}
