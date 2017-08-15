package com.cninsure.system.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCFdcomDao;
import com.cninsure.system.entity.INSCFdcom;
import com.cninsure.system.service.INSCFdcomService;
import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.entity.INSBProvider;

@Service
@Transactional
public class INSCFdcomServiceImpl extends BaseServiceImpl<INSCFdcom> implements
		INSCFdcomService {
	@Resource
	private INSCFdcomDao inscFdcomDao;

	@Override
	protected BaseDao<INSCFdcom> getBaseDao() {
		return inscFdcomDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cninsure.system.service.impl.INSCDeptTestService#getOrganizationData
	 * ()
	 */
	@Override
	public List<INSCFdcom> sycOrganizationData(String maxSyncdateStr,String comcode) {
		return inscFdcomDao.selectOrganizationData(maxSyncdateStr,comcode);
	}

	@Override
	public List<Map<String, String>> sycOrgCode() {
		return inscFdcomDao.selectOrgCode();
	}

	@Override
	public List<Map<String, Object>> sycProviderData(String maxSyncdateStr) {
		return inscFdcomDao.selectProviderData(maxSyncdateStr);
	}
}
