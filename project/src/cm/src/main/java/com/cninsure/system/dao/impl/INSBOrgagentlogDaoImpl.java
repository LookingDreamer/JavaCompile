package com.cninsure.system.dao.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.entity.INSBOrgagentlog;

@Repository
public class INSBOrgagentlogDaoImpl extends BaseDaoImpl<INSBOrgagentlog> implements
		INSBOrgagentlogDao {

	@Override
	public Date getMaxSyncdate(Integer type) {
		if(type != null){
			return sqlSessionTemplate.selectOne(getSqlName("getMaxSyncdate"), type);
		}
		return null;
	}

}