package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBDevice;

public interface INSBDeviceDao extends BaseDao<INSBDevice> {
	
	public List<Map<Object, Object>> selectDeviceListPaging(Map<String, Object> data);
}