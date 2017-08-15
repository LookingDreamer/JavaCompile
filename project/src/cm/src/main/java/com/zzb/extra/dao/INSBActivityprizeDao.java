package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBActivityprize;

public interface INSBActivityprizeDao extends BaseDao<INSBActivityprize> {
	public  List<Map<Object, Object>> getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBActivityprize iNSBActivityprize);
	public void updateObject(INSBActivityprize iNSBActivityprize);
	public void deleteObect(String id);
	
	public  List<Map<String, String>> getMap(String type);
}