package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBBankcard;

public interface INSBBankcardService extends BaseService<INSBBankcard> {
	
	public Map<String, Object> initBankCardList(Map<String, Object> map);
	
	public int addBankCard(INSBBankcard bankcard);
	
	public List<Map<String, String>> selectBanknamelist();

	public Map<String, Object> queryByBanktoname(Map<String, Object> map);
	
}