package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBAgentproviderDao;
import com.zzb.conf.entity.INSBAgentprovider;

@Repository
public class INSBAgentproviderDaoImpl extends BaseDaoImpl<INSBAgentprovider> implements
		INSBAgentproviderDao {

	@Override
	public List<INSBAgentprovider> selectListByAgentId(String agentId) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBAgentprovider_selectByAgentId", agentId);
	}

	@Override
	public int deleteByAgentIdproviderId(Map<String, String> ids) {
		
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBAgentprovider_deleteByAgentIdProviderId", ids);
	}

	@Override
	public int deleteByAgentI(String agentId) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBAgentprovider_deleteByAgentId",agentId);
	}

}