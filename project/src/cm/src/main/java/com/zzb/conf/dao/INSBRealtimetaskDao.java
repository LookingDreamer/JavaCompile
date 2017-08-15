package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBRealtimetask;

import java.util.Map;

public interface INSBRealtimetaskDao extends BaseDao<INSBRealtimetask> {

    public int deleteRealtimetask(Map<String, Object> map);
}