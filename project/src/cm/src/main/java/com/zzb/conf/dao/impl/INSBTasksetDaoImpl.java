package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBTasksetDao;
import com.zzb.conf.entity.INSBTaskset;

@Repository
public class INSBTasksetDaoImpl extends BaseDaoImpl<INSBTaskset> implements
		INSBTasksetDao {

	@Override
	public List<INSBTaskset> selectListByPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageByParam"), map);
	}

	@Override
	public long selectListCountByPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByParam"), map);
	}

	@Override
	public List<INSBTaskset> selectListByDeptId(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListByDeptId"), deptid);
	}

	@Override
	public INSBTaskset selectOnUseById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOnUseById"), id);
	}

	@Override
	public String selectTaskSetIdByProviderId(String providerid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectTaskSetIdByProviderId"), providerid);
	}

	@Override
	public List<String> selectTaskSetIdByProviderIdTaskType(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectTaskSetIdByProviderIdTaskType"), map);
	}

	@Override
	public List<String> selectIdByTaskSetType(String tasktype) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectIdByTaskSetType"), tasktype);
	}

}