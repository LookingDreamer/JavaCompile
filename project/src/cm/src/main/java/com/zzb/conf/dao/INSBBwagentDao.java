package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBBwagent;

public interface INSBBwagentDao extends BaseDao<INSBBwagent> {
	/**
	 * @return
	 */
	public List<INSBBwagent> selectBwagentData(String maxSyncdateStr);
	
	public List<INSBBwagent> getBwagentDataByAgcode(String agcode);

	public List<INSBBwagent> getBwagentDataByOrg(String orgCode);
}