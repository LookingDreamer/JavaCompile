package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBRenewalquoteitem;

public interface INSBRenewalquoteitemDao extends BaseDao<INSBRenewalquoteitem> {

    public int deleteByTask(String taskid, String inscomcode);
}