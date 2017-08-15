package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBPlatcarprice;

public interface INSBPlatcarpriceService extends BaseService<INSBPlatcarprice> {
	/**
	 * 通过taskid和inscomcode查询出平台车价信息List
	 * @param paramMap
	 * @return
	 */
public String getPlatCarPriceInfoJSONByMap(Map<String, Object> paramMap);
}