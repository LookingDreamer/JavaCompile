package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBPrvaccountkey;

public interface INSBPrvaccountkeyService extends BaseService<INSBPrvaccountkey> {

	
	public List<Map<String, Object>> queryConfigInfo(String deptId,String providerid,String usetype);
	
}