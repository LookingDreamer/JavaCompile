package com.zzb.conf.service;
 
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSCDeptpermissionset;

public interface INSCDeptpermissionsetService extends BaseService<INSCDeptpermissionset> {

	public Map<String, String> queryByComcode(String comcode);
	public INSCDeptpermissionset getSetid(String deptid,String agentKind);
}