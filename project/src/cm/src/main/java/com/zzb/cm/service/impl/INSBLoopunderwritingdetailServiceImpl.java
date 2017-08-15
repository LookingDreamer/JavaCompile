package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBLoopunderwritingdetailDao;
import com.zzb.cm.entity.INSBLoopunderwritingdetail;
import com.zzb.cm.service.INSBLoopunderwritingdetailService;

@Service
@Transactional
public class INSBLoopunderwritingdetailServiceImpl extends BaseServiceImpl<INSBLoopunderwritingdetail> implements
		INSBLoopunderwritingdetailService {
	@Resource
	private INSBLoopunderwritingdetailDao insbLoopunderwritingdetailDao;

	@Override
	protected BaseDao<INSBLoopunderwritingdetail> getBaseDao() {
		return insbLoopunderwritingdetailDao;
	}

}