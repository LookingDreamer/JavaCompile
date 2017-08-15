package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBPlatcarpriceDao;
import com.zzb.cm.entity.INSBPlatcarprice;

@Repository
public class INSBPlatcarpriceDaoImpl extends BaseDaoImpl<INSBPlatcarprice> implements
		INSBPlatcarpriceDao {

	/**
	 * 通过map参数条件查询平台车价信息
	 */
	@Override
	public List<Map<String, Object>> getPlatCarPriceInfoByMap(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPlatCarPriceInfoByMap"),map);
	}

}