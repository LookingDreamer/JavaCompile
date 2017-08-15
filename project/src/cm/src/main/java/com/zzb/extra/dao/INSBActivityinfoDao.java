package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBActivityinfo;

public interface INSBActivityinfoDao extends BaseDao<INSBActivityinfo> {
	public  List<Map<Object, Object>> getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBActivityinfo insbActivityinfo);
	
	public void updateObject(INSBActivityinfo insbActivityinfo);
	
	public void deleteObect(String id);

}