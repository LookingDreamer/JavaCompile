package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.model.EDIConfModel;

public interface INSBEdiconfigurationService extends BaseService<INSBEdiconfiguration> {
	
	public Map<String, Object> initEDIList(Map<String, Object> map);
	
	public int addedi(EDIConfModel edimodel);
	
	public List<INSBEdidescription> queryediinfoByid(String id);
	
	public int deleteDetail(String ediid);
	
	public int updateedi(EDIConfModel edimodel);
	
}