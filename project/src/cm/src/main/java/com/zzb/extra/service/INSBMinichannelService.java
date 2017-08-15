package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMinichannel;

import java.util.Map;

public interface INSBMinichannelService extends BaseService<INSBMinichannel> {
    public String queryChannelList(Map<String ,Object> map);
    public long selectMaxTempcode();
    public int updateWayNum(Map<String ,Object> map);
}