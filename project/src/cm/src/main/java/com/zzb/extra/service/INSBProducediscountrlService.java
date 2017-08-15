package com.zzb.extra.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBProducediscountrl;

public interface INSBProducediscountrlService extends BaseService<INSBProducediscountrl> {
	public  String getList(Map map);
	public Map findById(String id);
	public void saveObject(INSBProducediscountrl INSBProducediscountrl);
	public void updateObject(INSBProducediscountrl INSBProducediscountrl);
	public void deleteObect(String id);
}