package com.zzb.conf.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBTasksetscopeDao;
import com.zzb.conf.entity.INSBTasksetscope;
import com.zzb.conf.service.INSBTasksetscopeService;

@Service
@Transactional
public class INSBTasksetscopeServiceImpl extends BaseServiceImpl<INSBTasksetscope> implements
		INSBTasksetscopeService {
	@Resource
	private INSBTasksetscopeDao insbTasksetscopeDao;

	@Override
	protected BaseDao<INSBTasksetscope> getBaseDao() {
		return insbTasksetscopeDao;
	}
	@Override
	public int selectScopListCountByDeptid(Map<String,Object> map) {
		return  insbTasksetscopeDao.selectScopListCountByDeptid(map);
	}
}