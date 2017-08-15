package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBDevice;

public interface INSBDeviceService extends BaseService<INSBDevice> {
	
	public Map<String, Object> initDeviceList(Map<String, Object> data);
	
}