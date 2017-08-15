package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBDeviceDao;
import com.zzb.conf.entity.INSBDevice;
import com.zzb.conf.service.INSBDeviceService;

@Service
@Transactional
public class INSBDeviceServiceImpl extends BaseServiceImpl<INSBDevice> implements
		INSBDeviceService {
	@Resource
	private INSBDeviceDao insbDeviceDao;

	@Override
	protected BaseDao<INSBDevice> getBaseDao() {
		return insbDeviceDao;
	}

	@Override
	public Map<String, Object> initDeviceList(Map<String,Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbDeviceDao.selectDeviceListPaging(data);
		map.put("total", insbDeviceDao.selectCount());
		map.put("rows", infoList);
		return map;
	}

}