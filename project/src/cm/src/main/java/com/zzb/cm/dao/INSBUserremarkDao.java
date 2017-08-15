package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBUserremark;

public interface INSBUserremarkDao extends BaseDao<INSBUserremark> {

	INSBUserremark selectByTaskId(String taskid);

}