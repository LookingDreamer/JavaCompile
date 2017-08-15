package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBRegion;

public interface INSBOrderdeliveryDao extends BaseDao<INSBOrderdelivery> {
	public INSBOrderdelivery getOrderdeliveryByTaskId(Map<String,Object> map);

	/**
	 * liuchao 
	 */
	public String getOrderdeliveryAddress(Map<String,String> params); 
	
	public List<INSBRegion> getRegionByParentId(String parentid); 
	 
	public INSBOrderdelivery getOrderdelivery(Map<String, String> params);
}