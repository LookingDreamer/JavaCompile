package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBPrizeinfo;

public interface INSBPrizeinfoDao extends BaseDao<INSBPrizeinfo> {

	public List<Map<Object, Object>> getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBPrizeinfo insbPrizeinfo);
	
	public void updateObject(INSBPrizeinfo insbPrizeinfo);
	
	public void deleteObect(String id);
}