package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBBwagentDao;
import com.zzb.conf.entity.INSBBwagent;

@Repository
public class INSBBwagentDaoImpl extends BaseDaoImpl<INSBBwagent> implements
INSBBwagentDao {

	@Override
	public List<INSBBwagent> selectBwagentData(String maxSyncdateStr) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBwagentData"),maxSyncdateStr);
	}

	@Override
	public List<INSBBwagent> getBwagentDataByAgcode(String agcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getBwagentDataByAgcode"),agcode);
	}
	
	@Override
	public List<INSBBwagent> getBwagentDataByOrg(String orgCode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getBwagentDataByOrg"),orgCode);
	}
	
}