package com.zzb.cm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.service.INSBCarowneinfoService;

@Service
@Transactional
public class INSBCarowneinfoServiceImpl extends BaseServiceImpl<INSBCarowneinfo> implements
		INSBCarowneinfoService {
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;

	@Override
	protected BaseDao<INSBCarowneinfo> getBaseDao() {
		return insbCarowneinfoDao;
	}

	@Override
	public List<Map<String, String>> queryMonitorInfo(String taskid, String inscomcode) {
		return insbCarowneinfoDao.selectMonitorInfo(taskid, inscomcode);
	}

}