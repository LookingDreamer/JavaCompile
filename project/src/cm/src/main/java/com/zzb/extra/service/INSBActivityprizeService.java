package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBActivityprize;

public interface INSBActivityprizeService extends BaseService<INSBActivityprize> {
	  public String getList(Map map);
	    public Map findById(String id);
		public void saveObject(INSBActivityprize iNSBActivityprize);
		
		public void updateObject(INSBActivityprize iNSBActivityprize);
		
		public void deleteObect(String id);
		public  List<Map<String, String>> getMap(String type);
}