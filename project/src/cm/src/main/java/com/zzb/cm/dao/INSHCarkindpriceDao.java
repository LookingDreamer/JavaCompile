package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSHCarkindprice;

public interface INSHCarkindpriceDao extends BaseDao<INSHCarkindprice> {

	public List<INSHCarkindprice> selectByInscomcode(Map<String, Object> map);
}