package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCFdcom;

public interface INSCFdcomDao extends BaseDao<INSCFdcom> {
	public List<INSCFdcom> selectOrganizationData(String maxSyncdateStr,String comcode);
	public List<Map<String, String>> selectOrgCode();
	/**
	 * 获取需要同步更新的供应商信息
	 * @param maxSyncdateStr
	 * @return
	 */
	public List<Map<String, Object>> selectProviderData(String maxSyncdateStr);

}