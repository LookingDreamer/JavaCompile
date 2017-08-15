package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBUserprize;

public interface INSBUserprizeDao extends BaseDao<INSBUserprize> {

	public List<Map<Object, Object>> getList(Map map);

	public INSBUserprize findById(String id);
		
	
	public void saveObject(INSBUserprize insbUserprize);
	
	public void updateObject(INSBUserprize insbUserprize);
	
	public void deleteObect(String id);

}