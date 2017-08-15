package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBPlatcarprice;

public interface INSBPlatcarpriceDao extends BaseDao<INSBPlatcarprice> {

	/**
	 * 通过map参数条件查询平台车价信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPlatCarPriceInfoByMap(Map<String, Object> map);
}