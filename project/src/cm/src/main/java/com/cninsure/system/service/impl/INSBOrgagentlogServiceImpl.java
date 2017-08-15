package com.cninsure.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.entity.INSBOrgagentlog;
import com.cninsure.system.service.INSBOrgagentlogService;

@Service
@Transactional
public class INSBOrgagentlogServiceImpl extends BaseServiceImpl<INSBOrgagentlog> implements
		INSBOrgagentlogService {
	@Resource
	private INSBOrgagentlogDao insbOrgagentlogDao;

	@Override
	protected BaseDao<INSBOrgagentlog> getBaseDao() {
		return insbOrgagentlogDao;
	}

}