package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBHolidayareaDao;
import com.zzb.conf.dao.INSBWorktimeDao;
import com.zzb.conf.dao.INSBWorktimeareaDao;
import com.zzb.conf.entity.INSBWorktimearea;
import com.zzb.conf.service.INSBWorkTimeAreaService;

@Service
@Transactional
public class INSBWorkTimeAreaServiceImpl extends BaseServiceImpl<INSBWorktimearea>
		implements INSBWorkTimeAreaService {

	@Resource
	private INSBWorktimeDao insbWorktimeDao;

	@Resource
	private INSBWorktimeareaDao insbWorktimeareaDao;

	@Resource
	private INSBHolidayareaDao insbHolidayareaDao;

	@Override
	protected BaseDao<INSBWorktimearea> getBaseDao() {
		// TODO Auto-generated method stub
		return insbWorktimeareaDao;
	}

}