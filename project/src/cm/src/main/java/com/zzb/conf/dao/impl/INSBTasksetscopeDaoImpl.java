package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBTasksetscopeDao;
import com.zzb.conf.entity.INSBTasksetscope;

@Repository
public class INSBTasksetscopeDaoImpl extends BaseDaoImpl<INSBTasksetscope> implements
		INSBTasksetscopeDao {

	@Override
	public List<Map<String, Object>> selectScopListByTaskSetId(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectScopListByTaskSetId"), map);
	}

	@Override
	public long selectScopListCountByTaskSetId(
			String taksetid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectScopListCountByTaskSetId"), taksetid);
	}

	@Override
	public void deleteBatchByDeptIds(String[] deptIds) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteBatchByDeptIds"), deptIds);
	}

	@Override
	public List<String> selectTaskSetIdByDeptId(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectTaskSetIdByDeptId"), deptid);
	}

	@Override
	public int selectScopListCountByDeptid(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectScopListCountByDeptid"), map);
	}

}