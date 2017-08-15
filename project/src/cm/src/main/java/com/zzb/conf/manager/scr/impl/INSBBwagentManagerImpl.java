package com.zzb.conf.manager.scr.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.manager.scr.INSBBwagentManager;
import com.zzb.conf.service.INSBBwagentService;

@Service
public class INSBBwagentManagerImpl implements INSBBwagentManager {
	@Resource
	private INSBBwagentService insbBwagentService;

	@Override
	public List<INSBBwagent> getBwagentData(String maxSyncdateStr,String agentcode,String orgCode) {
		return insbBwagentService.sycBwagentData(maxSyncdateStr,agentcode, orgCode);
	}
}