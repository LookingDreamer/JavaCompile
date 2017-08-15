package com.cninsure.system.manager.scr.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCFdcomDao;
import com.cninsure.system.entity.INSBOrgagentlog;
import com.cninsure.system.entity.INSCFdcom;
import com.cninsure.system.manager.scr.INSCFdcomManager;
import com.cninsure.system.service.INSCDeptService;
import com.cninsure.system.service.INSCFdcomService;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentproviderDao;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBAgreementscopeDao;
import com.zzb.conf.dao.INSBCertificationDao;
import com.zzb.conf.dao.INSBItemprovidestatusDao;
import com.zzb.conf.dao.INSBPermissionallotDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.manager.scm.INSBAgentSyncService;
import com.zzb.conf.manager.scr.INSBBwagentManager;

@Service
public class INSCFdcomManagerImpl implements INSCFdcomManager {
	@Resource
	private INSCFdcomService inscFdcomService;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private OFUserDao ofUserDao;
	@Resource
	private INSBAgentproviderDao agentproviderDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBAgreementDao agreementDao;
	@Resource
	private INSBAgreementscopeDao agreementscopeDao;
	@Resource
	private INSBPermissionallotDao permissionallotDao;
	@Resource
	private INSBItemprovidestatusDao itemprovidestatusDao;
	@Resource
	private INSBCertificationDao certificationDao;
	@Resource
	private INSBFilelibraryService filelibraryService;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBAgentSyncService insbAgentSyncService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBBwagentManager insbBwagentManager;
	@Resource
	private INSBOrgagentlogDao insbOrgagentlogDao;
	@Resource
	private INSCFdcomDao inscFdcomDao;
	@Override
	public List<INSCFdcom> getOrganizationData(String maxSyncdateStr, String comcode) {
		return inscFdcomService.sycOrganizationData(maxSyncdateStr,comcode);
	}

	@Override
	public List<Map<String, String>> getOrgCode() {
		return inscFdcomService.sycOrgCode();
	}
	public void saveOrgagentlog(String operator, boolean result,
			Map<String, Object> map, Date syncdate) {
		INSBOrgagentlog log = new INSBOrgagentlog();
		log.setCreatetime(new Date());
		log.setOperator(operator);
		log.setType(Integer.valueOf(2));
		log.setSyncdate(syncdate);
		log.setIssuccess(result ? Integer.valueOf(1) : Integer.valueOf(2));
		log.setLogcontent((String) map.get("returnMsg"));
		insbOrgagentlogDao.insert(log);
	}

	@Override
	public List<Map<String, Object>> getProviderData(String maxSyncdateStr) {
		return inscFdcomService.sycProviderData(maxSyncdateStr);
	}
}