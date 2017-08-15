package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBBwagentDao;
import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.service.INSBBwagentService;

@Service
@Transactional
public class INSBBwagentServiceImpl extends BaseServiceImpl<INSBBwagent> implements
INSBBwagentService {
	@Resource
	private INSBBwagentDao insbBwagentDao;

	@Override
	protected BaseDao<INSBBwagent> getBaseDao() {
		return insbBwagentDao;
	}

	/* (non-Javadoc)
	 * @see com.zzb.conf.service.INSBBwagentService#sycBwagentData()
	 */
	@Override
	public List<INSBBwagent> sycBwagentData(String maxSyncdateStr,String agentcode,String orgCode) {
		if(!StringUtil.isEmpty(agentcode)){
			return insbBwagentDao.getBwagentDataByAgcode(agentcode);
		}else if(!StringUtil.isEmpty(orgCode)){
			return insbBwagentDao.getBwagentDataByOrg(orgCode);
		}else if(!StringUtil.isEmpty(maxSyncdateStr)){
			return insbBwagentDao.selectBwagentData(maxSyncdateStr);
		}
		return null;
	}


}
