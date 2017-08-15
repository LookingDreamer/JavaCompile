package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.model.EDIConfModel;

public interface INSBEdiconfigurationDao extends BaseDao<INSBEdiconfiguration> {
	
	public List<Map<Object, Object>> selectEDIListPaging(Map<String, Object> map);
	
	public int addedi(EDIConfModel edimodel);
	
	public List<INSBEdidescription> queryediinfoByid(String id);
	
	public int deleteDetail(String ediid);
	
	public int updateedi(EDIConfModel edimodel);
	
}