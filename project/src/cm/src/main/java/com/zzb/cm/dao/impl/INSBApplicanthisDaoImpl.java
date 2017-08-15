package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.entity.INSBApplicanthis;

@Repository
public class INSBApplicanthisDaoImpl extends BaseDaoImpl<INSBApplicanthis> implements INSBApplicanthisDao {

	@Override
	public int deleteByObj(INSBApplicanthis insbApplicanthis) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbApplicanthis);
	}

}