package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSHCarkindpriceDao;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSHCarkindprice;

@Repository
public class INSHCarkindpriceDaoImpl extends BaseDaoImpl<INSHCarkindprice> implements
		INSHCarkindpriceDao {

	@Override
	public List<INSHCarkindprice> selectByInscomcode(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByInscomcode"), params);
	}
}