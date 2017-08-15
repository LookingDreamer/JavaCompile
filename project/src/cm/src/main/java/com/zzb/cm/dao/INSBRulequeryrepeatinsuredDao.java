package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;

import java.util.List;

public interface INSBRulequeryrepeatinsuredDao extends BaseDao<INSBRulequeryrepeatinsured> {
    List<INSBRulequeryrepeatinsured> selectPolicy(String taskid);

}