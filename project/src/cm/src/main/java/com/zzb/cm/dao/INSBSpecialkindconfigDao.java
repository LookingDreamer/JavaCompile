package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBSpecialkindconfig;

public interface INSBSpecialkindconfigDao extends BaseDao<INSBSpecialkindconfig> {
	public List<INSBSpecialkindconfig> selectByTaskIdAndInscomcode(Map<String, String> map);
} 