package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarowneinfo;

public interface INSBCarowneinfoService extends BaseService<INSBCarowneinfo> {

	public List<Map<String,String>> queryMonitorInfo(String taskid,String inscomcode);
	
}