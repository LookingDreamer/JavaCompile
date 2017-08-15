package com.zzb.conf.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBOperatingsystemDao;
import com.zzb.conf.entity.INSBOperatingsystem;

@Repository
public class INSBOperatingsystemDaoImpl extends BaseDaoImpl<INSBOperatingsystem> implements
		INSBOperatingsystemDao {


	@Override
	public String queryOperatingSystemlist(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("mapcheck"),para);
	}

	@Override
	public String queryTypeId(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("typeid"),para);
	}
}