package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMinichannelway;

import java.util.Map;

public interface INSBMinichannelwayService extends BaseService<INSBMinichannelway> {
    public String queryChannelWayList(Map<String ,Object> map);
    public long selectMaxWayode(Map<String,Object> map);
}