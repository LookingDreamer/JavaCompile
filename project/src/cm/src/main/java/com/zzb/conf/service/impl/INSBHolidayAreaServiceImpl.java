package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.dao.INSBHolidayareaDao;
import com.zzb.conf.dao.INSBWorktimeDao;
import com.zzb.conf.dao.INSBWorktimeareaDao;
import com.zzb.conf.entity.INSBHolidayarea;
import com.zzb.conf.service.INSBHolidayAreaService;

@Service
@Transactional
public class INSBHolidayAreaServiceImpl extends BaseServiceImpl<INSBHolidayarea>
		implements INSBHolidayAreaService {

	@Resource
	private INSBWorktimeDao insbWorktimeDao;

	@Resource
	private INSBWorktimeareaDao insbWorktimeareaDao;

	@Resource
	private INSBHolidayareaDao insbHolidayareaDao;

	@Resource
	private INSCDeptService deptService;

	@Override
	protected BaseDao<INSBHolidayarea> getBaseDao() {
		// TODO Auto-generated method stub
		return insbHolidayareaDao;
	}

}