package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBProducediscountrl;

public interface INSBProducediscountrlDao extends BaseDao<INSBProducediscountrl> {
	public  List<Map<Object, Object>> getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBProducediscountrl iNSBProducediscountrl);
	public void updateObject(INSBProducediscountrl iNSBProducediscountrl);
	public void deleteObect(String id);
	
	public  List<Map<String, String>> getMap(String type);
}