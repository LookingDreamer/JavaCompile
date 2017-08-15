package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBSpecifydriverhis;

public interface INSBSpecifydriverhisDao extends BaseDao<INSBSpecifydriverhis> {

	public INSBSpecifydriverhis selectSpecifyDriverhis(String processInstanceId, String inscomcode, String personid);

}