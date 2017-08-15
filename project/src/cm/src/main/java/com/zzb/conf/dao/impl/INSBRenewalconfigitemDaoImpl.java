package com.zzb.conf.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRenewalconfigitemDao;
import com.zzb.conf.entity.INSBRenewalconfigitem;

@Repository
public class INSBRenewalconfigitemDaoImpl extends BaseDaoImpl<INSBRenewalconfigitem> implements
		INSBRenewalconfigitemDao {

    public int deleteByAgreementid(String agreementid) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteByAgreementid"), agreementid);
    }
}