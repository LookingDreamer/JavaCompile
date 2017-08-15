package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBLoopunderwriting;

import java.util.List;
import java.util.Map;

public interface INSBLoopunderwritingDao extends BaseDao<INSBLoopunderwriting> {
    public List<Map<String, Object>> searchList(Map<String, Object> paramMap);
    public Long searchCount(Map<String, Object> paramMap);
}