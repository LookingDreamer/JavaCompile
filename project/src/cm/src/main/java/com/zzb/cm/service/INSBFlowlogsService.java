package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBFlowlogs;

public interface INSBFlowlogsService extends BaseService<INSBFlowlogs> {

	public void logs(INSBFlowinfo insbFlowinfo);
	
}