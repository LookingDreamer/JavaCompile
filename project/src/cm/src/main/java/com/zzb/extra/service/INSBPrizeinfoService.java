package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBPrizeinfo;

public interface INSBPrizeinfoService extends BaseService<INSBPrizeinfo> {
	
	public String getList(Map map);
	
	public void saveObject(INSBPrizeinfo insbPrizeinfo);
	
	public void updateObject(INSBPrizeinfo insbPrizeinfo);
	
	public void deleteObect(String id);
	public Map findById(String id);
}