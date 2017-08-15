package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBSpecialkindconfig;

@Repository
public class INSBSpecialkindconfigDaoImpl extends BaseDaoImpl<INSBSpecialkindconfig> implements
		INSBSpecialkindconfigDao {
	public List<INSBSpecialkindconfig> selectByTaskIdAndInscomcode(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskIdAndInscomcode"),map);
	}
} 