package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBProviderandedi;

public interface INSBProviderandediService extends BaseService<INSBProviderandedi> {
	public int addproandedi(INSBProviderandedi bean);
	
	public int deleteRelation(String ediid);
	
}