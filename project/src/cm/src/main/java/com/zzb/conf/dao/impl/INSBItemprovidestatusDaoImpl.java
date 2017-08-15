package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBItemprovidestatusDao;
import com.zzb.conf.entity.INSBItemprovidestatus;

@Repository
public class INSBItemprovidestatusDaoImpl extends BaseDaoImpl<INSBItemprovidestatus> implements
		INSBItemprovidestatusDao {

	@Override
	public List<INSBItemprovidestatus> selectListBySetId(String setId) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBItemprovidestatus_selectBySetId",setId);
	}

	@Override
	public int deleteBySetIdproviderId(Map<String,String> ids) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBItemprovidestatus_deleteBySetIdProviderId", ids);
	}

	@Override
	public void deleteBySetId(String setId) {
		this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBItemprovidestatus_deleteBySetId", setId);
	}

	@Override
	public List<String> selectProviderIdBySetId(String setId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectProviderBySetId"), setId);
	}


}