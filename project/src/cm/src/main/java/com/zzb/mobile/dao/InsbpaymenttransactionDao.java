package com.zzb.mobile.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.Insbpaymenttransaction;



public interface InsbpaymenttransactionDao extends BaseDao<Insbpaymenttransaction>{
	

	public Insbpaymenttransaction selectOne(String date);
	
	public void update(Insbpaymenttransaction payment);
	
}
