package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOperatingsystem;

public interface INSBOperatingsystemService extends BaseService<INSBOperatingsystem> {
	public String queryOperatingSystemlist(String payid,String ststemtype);
	
	public String queryTypeId(String id,String type);
}