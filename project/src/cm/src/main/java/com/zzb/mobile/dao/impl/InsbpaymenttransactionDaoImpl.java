package com.zzb.mobile.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.AppPaymentyzfDao;

import com.zzb.mobile.dao.InsbpaymenttransactionDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.entity.Insbpaymenttransaction;
import com.zzb.model.AppPaymentyzfRModel;

@Repository
public class InsbpaymenttransactionDaoImpl extends BaseDaoImpl<Insbpaymenttransaction> implements InsbpaymenttransactionDao {

	@Override
	public Insbpaymenttransaction selectOne(String date){
	
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), date);
	}
	@Override
	public void update(Insbpaymenttransaction payment){
		
		this.sqlSessionTemplate.update(this.getSqlName("update"), payment);
	}

}
