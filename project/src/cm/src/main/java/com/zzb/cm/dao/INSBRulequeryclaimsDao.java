package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBRulequeryclaims;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;

import java.util.List;

public interface INSBRulequeryclaimsDao extends BaseDao<INSBRulequeryclaims> {
    List<INSBRulequeryclaims> selectPolicy(String taskid);
}