package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBOperatingsystemDao;
import com.zzb.conf.entity.INSBOperatingsystem;
import com.zzb.conf.service.INSBOperatingsystemService;

@Service
@Transactional
public class INSBOperatingsystemServiceImpl extends BaseServiceImpl<INSBOperatingsystem> implements
		INSBOperatingsystemService {
	@Resource
	private INSBOperatingsystemDao insbOperatingsystemDao;

	@Override
	protected BaseDao<INSBOperatingsystem> getBaseDao() {
		return insbOperatingsystemDao;
	}

	@Override
	public String queryOperatingSystemlist(String paychannelid,String systemtype) {
		Map<String, String> para = new HashMap<String, String>();
		if(!StringUtil.isEmpty(paychannelid)){
			para.put("paychannelid", paychannelid);
		}
		if(!StringUtil.isEmpty(systemtype)){
			para.put("systemtype", systemtype);
		}
		return insbOperatingsystemDao.queryOperatingSystemlist(para);
	}

	@Override
	public String queryTypeId(String id, String systemtype) {
		Map<String, String> map = new HashMap<String, String>();
		if(id!=null&&systemtype!=null){
			map.put("id", id);
			map.put("systemtype", systemtype);
		}
		return insbOperatingsystemDao.queryTypeId(map);
	}


}