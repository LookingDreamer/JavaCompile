package com.zzb.conf.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBQuoteconfigDao;
import com.zzb.conf.entity.INSBQuoteconfig;

@Repository
public class INSBQuoteconfigDaoImpl extends BaseDaoImpl<INSBQuoteconfig> implements
		INSBQuoteconfigDao {

//	@Override
//	public String insertReturnId(INSBQuoteconfig qc) {
//		String id = UUIDUtils.random();
//		qc.setId(id);
//		this.sqlSessionTemplate.insert(this.getSqlName("insert"),qc);
//		
//		return id;
//	}
//
//	@Override
//	public List<INSBQuoteconfig> selectByAgreementId(String agreementid) {
//		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByAgreementId"), agreementid);
//	}

}