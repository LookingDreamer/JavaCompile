package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.system.entity.INSCUser;

public interface INSBUnderwritingService {

	public String undwrtSuccess(String processInstanceId, String userid, String taskName, Map<String, Object> data,String inscomcode);

	public String undwrtSuccess(String processInstanceId, String userid, String taskCode,String inscomcode);

}
