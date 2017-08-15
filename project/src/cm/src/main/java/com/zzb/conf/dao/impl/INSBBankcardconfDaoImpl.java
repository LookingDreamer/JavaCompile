package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBBankcardconfDao;
import com.zzb.conf.entity.INSBBankcardconf;

@Repository
public class INSBBankcardconfDaoImpl extends BaseDaoImpl<INSBBankcardconf> implements
		INSBBankcardconfDao {

	@Override
	public List<INSBBankcardconf> queryBankcardConfInfo(String paychannelid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("bankconfinfo"),paychannelid);
	}

	@Override
	public int addBankCardConf(INSBBankcardconf bankcard) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),bankcard);
	}

}