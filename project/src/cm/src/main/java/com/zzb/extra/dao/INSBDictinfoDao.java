package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBDictinfo;

public interface INSBDictinfoDao extends BaseDao<INSBDictinfo> {
	public  List<Map<Object, Object>> getList(Map map);
}