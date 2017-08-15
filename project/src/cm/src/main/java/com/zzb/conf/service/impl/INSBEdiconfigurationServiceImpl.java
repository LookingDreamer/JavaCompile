package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBEdiconfigurationDao;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.conf.service.INSBEdiconfigurationService;
import com.zzb.model.EDIConfModel;

@Service
@Transactional
public class INSBEdiconfigurationServiceImpl extends BaseServiceImpl<INSBEdiconfiguration> implements
		INSBEdiconfigurationService {
	@Resource
	private INSBEdiconfigurationDao insbEdiconfigurationDao;

	@Override
	protected BaseDao<INSBEdiconfiguration> getBaseDao() {
		return insbEdiconfigurationDao;
	}
	
	@Override
	public Map<String, Object> initEDIList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbEdiconfigurationDao.selectEDIListPaging(data);
		map.put("total", insbEdiconfigurationDao.selectCount());
		map.put("rows", infoList);
		return map;
	}

	@Override
	public int addedi(EDIConfModel edimodel) {
		return insbEdiconfigurationDao.addedi(edimodel);
	}

	@Override
	public List<INSBEdidescription> queryediinfoByid(String id) {
		return insbEdiconfigurationDao.queryediinfoByid(id);
	}

	@Override
	public int deleteDetail(String ediid) {
		return insbEdiconfigurationDao.deleteDetail(ediid);
	}

	@Override
	public int updateedi(EDIConfModel edimodel) {
		return insbEdiconfigurationDao.updateedi(edimodel);
	}

}