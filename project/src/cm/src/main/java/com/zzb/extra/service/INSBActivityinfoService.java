package com.zzb.extra.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBActivityinfo;

public interface INSBActivityinfoService extends BaseService<INSBActivityinfo> {
	    public String getList(Map map);
	    public Map findById(String id);
		public void saveObject(INSBActivityinfo insbActivityinfo);
		
		public void updateObject(INSBActivityinfo insbActivityinfo);
		
		public void deleteObect(String id);
}