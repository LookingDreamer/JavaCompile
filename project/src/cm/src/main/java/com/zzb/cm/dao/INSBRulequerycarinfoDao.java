package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBRulequerycarinfo;

public interface INSBRulequerycarinfoDao extends BaseDao<INSBRulequerycarinfo> {

    INSBLastyearinsureinfo queryLastYearClainInfo(String taskid);
}