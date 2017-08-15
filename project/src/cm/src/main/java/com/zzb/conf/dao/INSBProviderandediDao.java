package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBProviderandedi;

public interface INSBProviderandediDao extends BaseDao<INSBProviderandedi> {
	public int addproandedi(INSBProviderandedi bean);
	
	public int deleteRelation(String ediid);
	public List<Map<String,String>> selectByPid(String providerid);
}