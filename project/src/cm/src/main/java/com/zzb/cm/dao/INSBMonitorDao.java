package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBMonitor;

public interface INSBMonitorDao extends BaseDao<INSBMonitor> {
	public List<String> queryList(Map<String, Object> parp);
	public List<String> queryAllList(Map<String, Object> parp);
	public long queryCountList(Map<String, Object> parp);
	public List<String> queryPrvNames(Map<String, Object> parp);
	public List<INSBMonitor> queryTaskList(Map<String, Object> parp);
	public long queryCountTaskList(Map<String, Object> parp);
	public void updateMonitorStatus(Map<String, String> parp);
	public List<Map<String,Object>> getAllMonitorInfo(Map<String, Object> parp);
	public List<INSBMonitor> getAllMonitorTaskInfo(Map<String, Object> parp);
	public long getAllCountMonitorTaskInfo(Map<String, Object> parp);
}