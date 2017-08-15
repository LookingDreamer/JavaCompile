package com.zzb.conf.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRealtimetaskDao;
import com.zzb.conf.entity.INSBRealtimetask;

import java.util.Map;

@Repository
public class INSBRealtimetaskDaoImpl extends BaseDaoImpl<INSBRealtimetask> implements
		INSBRealtimetaskDao {

	@Override
	public int deleteRealtimetask(Map<String, Object> map) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteRealtimetask"), map);
	}
}