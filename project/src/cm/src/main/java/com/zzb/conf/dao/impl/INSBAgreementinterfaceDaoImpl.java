package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.entity.INSBAgreementinterface;

@Repository
public class INSBAgreementinterfaceDaoImpl extends BaseDaoImpl<INSBAgreementinterface> implements
		INSBAgreementinterfaceDao {

	@Override
	public List<Map<String, Object>> getInterfaceInfo(String agreementid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getInterfaceInfo"),agreementid);
	}

	@Override
	public List<Map<String, Object>> getByChannelinnercode(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectList(this.getSqlName("getByChannelinnercode"), map);
	}

	@Override
	public String getIdByChannelinnercode(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getIdByChannelinnercode"), map);
	}
	
}