package com.cninsure.system.dao;

import java.util.Date;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSBOrgagentlog;

public interface INSBOrgagentlogDao extends BaseDao<INSBOrgagentlog> {
	/**
	 * @param type
	 * @return
	 */
	public Date getMaxSyncdate(Integer type);

}