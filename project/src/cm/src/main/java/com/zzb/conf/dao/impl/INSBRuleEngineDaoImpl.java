package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zzb.conf.dao.INSBRuleEngineDao;

@Repository
public class INSBRuleEngineDaoImpl implements INSBRuleEngineDao {
	@Autowired(required = true)
	private SqlSession sqlSessionTemplate;

	public void setSqlSession1(SqlSession sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public List<Map<String, Object>> selectByParamMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("INSBRuleEngine_selectByParam", map);
	}
	@Override
	public List<Map<String, Object>> selectListByCity(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("INSBRuleEngine_queryTruleByCity",map);
	}

	@Override
	public String getAgreementrulename(String agreementrule) {
		return this.sqlSessionTemplate.selectOne("INSBRuleEngine_getagreementrulename",agreementrule);
	}
}