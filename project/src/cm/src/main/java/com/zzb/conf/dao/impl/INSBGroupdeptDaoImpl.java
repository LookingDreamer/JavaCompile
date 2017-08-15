package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBGroupdeptDao;
import com.zzb.conf.entity.INSBGroupdept;
import com.zzb.conf.entity.INSBProvider;

@Repository
public class INSBGroupdeptDaoImpl extends BaseDaoImpl<INSBGroupdept> implements
		INSBGroupdeptDao {

	@Override
	public List<INSBGroupdept> selectListByGruopId(String groupId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByGroupId"), groupId);
	}

	@Override
	public int deleteByGroupId(String groupId) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByGroupId"), groupId);
	}

	@Override
	public int deleteByDeptId(String deptId) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByDeptId"), deptId);
	}

	@Override
	public List<INSBGroupdept> selectPageByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParam"), map);
	}

	@Override
	public List<String> selectGroupIdIdByDeptId4Task(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdIdByDeptId4Task"), deptid);
	}

	/**
	 * 查询没有机构的业管组
	 *
	 * @return
	 */
	@Override
	public List<String> selectNoDeptGroupIdId() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectNoDeptGroupIdId"));
	}

	@Override
	public List<String> selectDeptIdsByGroupId(String id) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptIdsByGroupId"), id);
	}
	
	@Override
	public List<INSBProvider> getGroupProviderByGroup(String groupId){
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllGroupProvider"),groupId);
	}
	
	@Override
	public List<INSBProvider> getGroupProviderByDept(String deptId, String groupId) {
		Map<String , Object>map = new HashMap<String,Object>();
		map.put("deptId", deptId);
		map.put("groupId", groupId);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupProviderByDeptId"),map);
	}

}