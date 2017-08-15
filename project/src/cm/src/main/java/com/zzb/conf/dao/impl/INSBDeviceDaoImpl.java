package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBDeviceDao;
import com.zzb.conf.entity.INSBDevice;

@Repository
public class INSBDeviceDaoImpl extends BaseDaoImpl<INSBDevice> implements
		INSBDeviceDao {

	@Override
	public List<Map<Object, Object>> selectDeviceListPaging(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"),map);
	}

}