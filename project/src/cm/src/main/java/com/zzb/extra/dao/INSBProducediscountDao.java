package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBProducediscount;

public interface INSBProducediscountDao extends BaseDao<INSBProducediscount> {
	public  List<Map<Object, Object>> getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBProducediscount iNSBProducediscount);
	public void updateObject(INSBProducediscount iNSBProducediscount);
	public void deleteObect(String id);
	
	public  List<Map<String, String>> getMap(String type);
}