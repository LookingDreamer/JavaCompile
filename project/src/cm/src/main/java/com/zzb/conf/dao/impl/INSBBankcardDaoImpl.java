package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBBankcardDao;
import com.zzb.conf.entity.INSBBankcard;

@Repository
public class INSBBankcardDaoImpl extends BaseDaoImpl<INSBBankcard> implements
		INSBBankcardDao {

	@Override
	public List<Map<Object, Object>> selectBankCardListPaging(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryByBankCardId"),map);
	}

	@Override
	public int addBankCard(INSBBankcard bankcard) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),bankcard);
	}

	@Override
	public List<Map<String, String>> selectBanknamelist() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBanknamelist"));
	}

	@Override
	public List<Map<Object, Object>> queryByBanktoname(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryByBanktoname"),map);
	}

}