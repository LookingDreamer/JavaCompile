package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCSchedule;

public interface INSCScheduleDao extends BaseDao<INSCSchedule> {
	public List<INSCSchedule> getScheduleStateListByIds(List<String> roleidList);
	public long selectPagingCount(Map<String, Object> data);
	public List<Map<Object, Object>> selectScheduleListPaging(Map<String, Object> map);
}