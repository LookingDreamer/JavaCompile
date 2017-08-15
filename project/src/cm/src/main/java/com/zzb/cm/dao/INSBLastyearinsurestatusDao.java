package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBLastyearinsurestatus;

public interface INSBLastyearinsurestatusDao extends BaseDao<INSBLastyearinsurestatus> {
	/**
	 * 判断表中数据是否存在，用于判断规则平台查询是否发起了
	 * @param taskid
	 * @return
	 */
	INSBLastyearinsurestatus isHaveOneDate(String taskid);

}