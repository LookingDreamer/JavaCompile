package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCFdcom;
import com.zzb.conf.entity.INSBProvider;

public interface INSCFdcomService extends BaseService<INSCFdcom> {

	public List<INSCFdcom> sycOrganizationData(String maxSyncdateStr, String comcode);

	public List<Map<String, String>> sycOrgCode() ;
	/**
	 * 获取需要同步更新的供应商信息
	 * @param maxSyncdateStr
	 * @return
	 */
	public List<Map<String, Object>> sycProviderData(String maxSyncdateStr);
}