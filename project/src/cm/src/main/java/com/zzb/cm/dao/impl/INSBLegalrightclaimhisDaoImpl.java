package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.entity.INSBLegalrightclaimhis;

@Repository
public class INSBLegalrightclaimhisDaoImpl extends BaseDaoImpl<INSBLegalrightclaimhis> implements INSBLegalrightclaimhisDao {

	@Override
	public int deleteByObj(INSBLegalrightclaimhis insbLegalrightclaimhis) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbLegalrightclaimhis);
	}

}