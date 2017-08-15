package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.baseData.INSCServicerModel;

public interface INSCServicerDao extends BaseDao<INSCServicerModel> {

	public List<INSCServicerModel> list();
}
