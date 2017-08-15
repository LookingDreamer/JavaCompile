package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMinichannelway;

import java.util.List;
import java.util.Map;

public interface INSBMinichannelwayDao extends BaseDao<INSBMinichannelway> {
    public List<Map<Object,Object>> selectChannelWayList(Map<String,Object> map);
    public long selectMaxWayCode(Map<String,Object> map);
}