package com.zzb.conf.dao;

import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBOperatingsystem;

public interface INSBOperatingsystemDao extends BaseDao<INSBOperatingsystem> {
	public String queryOperatingSystemlist(Map<String, String> para);
	
	public String  queryTypeId(Map<String, String> para);
}