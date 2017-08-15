package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBMonitor;

public interface INSBMonitorService extends BaseService<INSBMonitor> {

	public List<Map<String, String>> queryOrgInfo();
	
	public Map<String, Object> queryList(Map<String, Object> monitorModel);
	
	public Map<String, Object> queryEdiList(Map<String, Object> monitorModel);
	
	public String queryPrvNames(Map<String, Object> data);
	
	public Map<String, Object> queryTaskList(Map<String, Object> monitorModel);
	
	public Map<String, Object> querytaskinfoList(Map<String, Object> monitorModel);
}