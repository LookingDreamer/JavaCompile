package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zzb.conf.dao.INSCServicerDao;
import com.zzb.conf.entity.baseData.INSCServicerModel;
import com.zzb.conf.service.INSCServicerService;

@Service
public class INSCServicerServiceImpl implements INSCServicerService{

	@Resource
	private INSCServicerDao dao;
	
	@Override
	public List<INSCServicerModel> list() {
		return dao.list();
	}

}
