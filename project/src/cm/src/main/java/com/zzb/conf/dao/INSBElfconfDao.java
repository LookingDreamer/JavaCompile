package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBElfconf;

public interface INSBElfconfDao extends BaseDao<INSBElfconf> {
	public List<Map<Object, Object>> selectELFConfListPaging(Map<String, Object> data);
	
	
	public List<INSBElfconf> selectByPid(String providerid);
	
	public List<INSBElfconf> queryElfAll();
	
	/**
	 * 根据精灵id获取能力列表
	 * @param data
	 * @return
	 */
	public List<Map<Object, Object>> abilityListByelfidPaging(Map<String, Object> data);
	
	public int abilityListByelfidPagingCount(Map<String, Object> data);
}