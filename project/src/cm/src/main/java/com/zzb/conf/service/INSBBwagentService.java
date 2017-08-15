package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBBwagent;

public interface INSBBwagentService extends BaseService<INSBBwagent> {

	List<INSBBwagent> sycBwagentData(String maxSyncdateStr, String agentcode, String orgCode);
}