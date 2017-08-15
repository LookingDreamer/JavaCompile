package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBBankcard;

public interface INSBBankcardDao extends BaseDao<INSBBankcard> {
	public List<Map<Object, Object>> selectBankCardListPaging (Map<String, Object> data);
	
	public int addBankCard(INSBBankcard bankcard);

	public List<Map<String, String>> selectBanknamelist();

	public List<Map<Object, Object>> queryByBanktoname(Map<String, Object> map);
	
}