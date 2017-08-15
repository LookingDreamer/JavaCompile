package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSHCarkindprice;

public interface INSHCarkindpriceService extends BaseService<INSHCarkindprice> {

	public Map<String, Object> getCarInsConfigByInscomcode(String inscomcode, String processInstanceId);

}