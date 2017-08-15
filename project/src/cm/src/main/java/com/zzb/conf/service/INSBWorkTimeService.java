package com.zzb.conf.service;

import java.util.Date;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.mysql.fabric.xmlrpc.base.Data;
import com.zzb.conf.entity.INSBWorktime;
import com.zzb.model.WorkTimeModel;

public interface INSBWorkTimeService extends BaseService<INSBWorktime> {


	public Map<Object, Object> queryworktimelistByPage(int countPage, String onlyfuture2);

	public Map<Object, Object> queryOneBydeptid(String inscdeptid);

	public Map<Object, Object> queryOneFuture(Map<Object,Object> map);

	public void updateOrAdd(WorkTimeModel model);
	
	public int inWorkTime(Date time, String deptid, Date payDate);
	
	
}