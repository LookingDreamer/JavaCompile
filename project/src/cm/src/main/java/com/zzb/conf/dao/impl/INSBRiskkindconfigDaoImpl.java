package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBRiskkindconfig;

@Repository
public class INSBRiskkindconfigDaoImpl extends BaseDaoImpl<INSBRiskkindconfig> implements
		INSBRiskkindconfigDao {

	@Override
	public INSBRiskkindconfig selectByDatamp(String datatemp) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByDatatemp"), datatemp);
	}

	@Override
	public List<INSBRiskkindconfig> selectNotAll() {		
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSection"));
	}

	@Override
	public INSBRiskkindconfig selectKindByKindcode(String riskkindcode) {		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectkindBycode"), riskkindcode);
	}

	@Override
	public List<INSBRiskkindconfig> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

	@Override
	public List<INSBRiskkindconfig> selectListIn(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListIn"),params);
	}

}