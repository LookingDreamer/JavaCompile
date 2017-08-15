package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarowneinfo;

public interface INSBCarowneinfoDao extends BaseDao<INSBCarowneinfo> {

	public INSBCarowneinfo selectByTaskId(String taskid);
	
	public List<Map<String,String>> selectMonitorInfo(String taskid,String inscomcode);

}