package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.service.INSBWorkflowmaintrackdetailService;

@Service
@Transactional
public class INSBWorkflowmaintrackdetailServiceImpl extends BaseServiceImpl<INSBWorkflowmaintrackdetail> implements
		INSBWorkflowmaintrackdetailService {
	@Resource
	private INSBWorkflowmaintrackdetailDao insbWorkflowmaintrackdetailDao;

	@Override
	protected BaseDao<INSBWorkflowmaintrackdetail> getBaseDao() {
		return insbWorkflowmaintrackdetailDao;
	}

}