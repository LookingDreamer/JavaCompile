package com.cninsure.system.manager.scr;

import java.util.List;
import java.util.Map;

import com.cninsure.system.entity.INSCFdcom;
import com.zzb.conf.entity.INSBProvider;

public interface INSCFdcomManager {

	public List<INSCFdcom> getOrganizationData(String maxSyncdateStr, String comcode);
	
	public List<Map<String,String>> getOrgCode();
	/**
	 * 获取需要更新的供应商信息
	 * @param maxSyncdateStr
	 * @return
	 */
	public List<Map<String, Object>> getProviderData(String maxSyncdateStr);
	
}