package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRiskplanrelationDao;
import com.zzb.conf.entity.INSBRiskplanrelation;

@Repository
public class INSBRiskplanrelationDaoImpl extends BaseDaoImpl<INSBRiskplanrelation> implements
		INSBRiskplanrelationDao {

	@Override
	public List<INSBRiskplanrelation> selectAllPlankey() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllPlankey"));
	}

}