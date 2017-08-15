package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMiniPermissionDao;
import com.zzb.extra.entity.INSBMiniPermission;
@Repository
public class INSBMiniPermissionDaoImpl extends BaseDaoImpl<INSBMiniPermission> implements INSBMiniPermissionDao {



	@Override
	public INSBMiniPermission findById(String id) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniPermission_selectById", id);
	}

	@Override
	public void saveObject(INSBMiniPermission insbMiniPermission) {
		this.sqlSessionTemplate.insert("INSBMiniPermission_insert",insbMiniPermission);
		
	}

	@Override
	public void updateObject(INSBMiniPermission insbMiniPermission) {
		this.sqlSessionTemplate.update("INSBMiniPermission_updateById", insbMiniPermission);// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObect(String id) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.delete("INSBMiniPermission_deleteById", id);
	}

	@Override
	public long queryCountPermission(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniPermission_AccountId", map);
	}

	@Override
	public List<Map<String,Object>> queryPermissionList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniPermission_select", map);
	}

	@Override
	public List<INSBMiniPermission> queryPermission(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniPermission_selectList", map);
	}

	@Override
	public long queryCountPermissionList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return  this.sqlSessionTemplate.selectOne("INSBMiniPermission_selectListCount", map);
	}

	@Override
	public Long selectMaxPercode(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniPermission_selectMaxTmpcode", map);
	}

	@Override
	public List<Map<Object, Object>> queryMiniOrderList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniPermission_queryAgentOrderByUserid", map);
	}

	@Override
	public Long queryAgentOrderByUseridCount(Map<String, Object> agentid) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniPermission_queryAgentOrderByUseridCount", agentid);
	}

	

}
