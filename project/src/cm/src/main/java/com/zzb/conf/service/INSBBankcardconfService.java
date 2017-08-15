package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBBankcardconf;

public interface INSBBankcardconfService extends BaseService<INSBBankcardconf> {
	
	public List<INSBBankcardconf> queryBankcardConfInfo(String paychannelid);
	
	public int addBankCardConf(INSBBankcardconf bankcard);
	
}