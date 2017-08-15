package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBTasksetrulebseDao;
import com.zzb.conf.entity.INSBRuleBase;
import com.zzb.conf.entity.INSBTasksetrulebse;

@Repository
public class INSBTasksetrulebseDaoImpl extends BaseDaoImpl<INSBTasksetrulebse> implements
		INSBTasksetrulebseDao {
	@Override
	public List<INSBRuleBase> selectListPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(
				"INSBGrouprulebse_selectPageListByParam", map);
	}

	@Override
	public long selectListCountPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(
				"INSBGrouprulebse_selectPageCountListByParam", map);
	}

	@Override
	public List<INSBTasksetrulebse> selectByTaskSetId(String tasksetid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListByTasksetId"), tasksetid);
	}

	@Override
	public void deleteUnionByRuleId(String rulebaseid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByRuleId"),rulebaseid);
	}

	@Override
	public List<INSBTasksetrulebse> selectByRuleId(String ruleId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListByRuleId"), ruleId);
	}

	@Override
	public List<String> selectList4Rule() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectList4Rule"));
	}

	
	@Override
	public List<String> selectListPage4Taskset(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListPage4Taskset"),map);
	}
}