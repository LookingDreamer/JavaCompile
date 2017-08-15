package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCSchedule;
import com.cninsure.system.entity.INSCUser;

public interface INSCScheduleService extends BaseService<INSCSchedule> {
	public String executebyids(List<String> ids);

	public String getSchedule(String id);

	public String deleteByIds(List<String> ids);

	public String saveOrUpdateSchedule(INSCUser user, INSCSchedule schedule);
	
	public Map<String,Object> getEditInfo(String scheduleId,int offset,int limit);
	
	public String getScheduleList(Map<String, Object> map);
}