package com.zzb.extra.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBProducediscount;

public interface INSBProducediscountService extends BaseService<INSBProducediscount> {
	public  String getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBProducediscount INSBProducediscount);
	public void updateObject(INSBProducediscount INSBProducediscount);
	public void deleteObect(String id);
}