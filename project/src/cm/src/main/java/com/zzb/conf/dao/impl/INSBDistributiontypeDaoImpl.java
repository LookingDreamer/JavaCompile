package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBDistributiontypeDao;
import com.zzb.conf.entity.INSBDistributiontype;

@Repository
public class INSBDistributiontypeDaoImpl extends BaseDaoImpl<INSBDistributiontype> implements
		INSBDistributiontypeDao {

	@Override
	public Long deleteByExceptIds(List<String> ids, String agreementid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		params.put("agreementid",agreementid);
		return (long) this.sqlSessionTemplate.delete(this.getSqlName("deleteByExceptIds"),params);
	}

    public Long deleteByAgreementId(String agreementid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("agreementid",agreementid);
        return (long) this.sqlSessionTemplate.delete(this.getSqlName("deleteByAgreementId"),params);
    }
}