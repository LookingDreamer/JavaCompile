package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBPolicyitem;

public interface ApplicationBookService extends BaseService<INSBPolicyitem>{

	public String applicationBoolSource(String taskId,String risktype,String prvid);
}
