package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBLoopunderwritingDao;
import com.zzb.cm.entity.INSBLoopunderwriting;

import java.util.List;
import java.util.Map;

@Repository
public class INSBLoopunderwritingDaoImpl extends BaseDaoImpl<INSBLoopunderwriting> implements INSBLoopunderwritingDao {
    public List<Map<String, Object>> searchList(Map<String, Object> paramMap) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("queryList"), paramMap);
    }

    public Long searchCount(Map<String, Object> paramMap) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("queryListCount"), paramMap);
    }
}