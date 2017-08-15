package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBElfconf;

public interface INSBElfconfService extends BaseService<INSBElfconf> {
	
	public Map<String, Object> initELFConfList(Map<String, Object> map);
	public List<INSBElfconf> queryElfAll();
	
	/**
	 * 根据精灵id获取能力列表
	 * @param map
	 * @return
	 */
	public Map<String, Object> abilityListByelfid(Map<String, Object> map,String elfid);
}