package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBUserprize;

public interface INSBUserprizeService extends BaseService<INSBUserprize> {
	 public String getList(Map map);
	    public INSBUserprize findById(String id);
		
		public void saveObject(INSBUserprize insbUserprize);
		
		public void updateObject(INSBUserprize insbUserprize);
		
		public void deleteObect(String id);

}