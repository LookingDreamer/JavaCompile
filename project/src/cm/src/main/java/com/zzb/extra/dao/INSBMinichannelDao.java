package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMinichannel;

import java.util.List;
import java.util.Map;

public interface INSBMinichannelDao extends BaseDao<INSBMinichannel> {

    public long selectChannelCount(Map<String,Object> map);
    public long selectMaxTempcode();
    public List<Map<Object,Object>> selectChannelList(Map<String,Object> map);
    public int updateWayNum(Map<String,Object> map);


}