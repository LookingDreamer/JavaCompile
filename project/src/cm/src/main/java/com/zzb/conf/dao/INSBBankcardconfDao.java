package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBBankcardconf;

public interface INSBBankcardconfDao extends BaseDao<INSBBankcardconf> {
	
	/**
	 * 支付渠道id 查询银行卡配置信息
	 * 
	 * @param paychannelid
	 * @return
	 */
	public List<INSBBankcardconf> queryBankcardConfInfo(String paychannelid);
	
	public int addBankCardConf(INSBBankcardconf bankcard);
}