package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBAgreementzoneDao;
import com.zzb.conf.entity.INSBAgreementzone;

@Repository
public class INSBAgreementzoneDaoImpl extends BaseDaoImpl<INSBAgreementzone> implements
 INSBAgreementzoneDao {

	@Override
	public List<Map<String, Object>> getProvinces(String agreementid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getprovincebyagreementid"),agreementid);
	}

	@Override
	public List<Map<String, Object>> getCityByProvince(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getcitybyprovince"),map);
	}

	@Override
	public void deleteGuanlian(String agreementid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteguanlian"), agreementid);
	}

	@Override
	public List<String> getCityByAgreementId(String agreementid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getcitysbyagreementid"), agreementid);
	}

}