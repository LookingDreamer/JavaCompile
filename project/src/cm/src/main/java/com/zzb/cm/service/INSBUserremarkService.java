package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBUserremark;

public interface INSBUserremarkService extends BaseService<INSBUserremark> {

	INSBUserremark getRemarkByTaskId(String taskid);
	Map<String, Object> getRemarkByTaskId_Comcode(String taskid,String comcode);

}