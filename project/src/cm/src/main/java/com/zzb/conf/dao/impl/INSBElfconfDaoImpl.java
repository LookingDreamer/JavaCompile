package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBElfconfDao;
import com.zzb.conf.entity.INSBElfconf;

@Repository
public class INSBElfconfDaoImpl extends BaseDaoImpl<INSBElfconf> implements
		INSBElfconfDao {

	@Override
	public List<Map<Object, Object>> selectELFConfListPaging(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectrownum"),map);
	}

	@Override
	public List<Map<Object, Object>> abilityListByelfidPaging(Map<String, Object> map) throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("abilityListByelfidPaging"),map);
	}
	
	@Override
	public int abilityListByelfidPagingCount(Map<String, Object> data) throws DaoException{
		return this.sqlSessionTemplate.selectOne(this.getSqlName("abilityListByelfidPagingCount"),data);
	}
	
	@Override
	public List<INSBElfconf> selectByPid(String providerid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByPid"),providerid);
	}


	@Override
	public List<INSBElfconf> queryElfAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryElfAll"));
	}


}