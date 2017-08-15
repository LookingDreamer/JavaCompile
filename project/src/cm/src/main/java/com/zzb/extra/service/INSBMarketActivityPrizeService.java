package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMarketActivityPrize;

import java.util.List;
import java.util.Map;

public interface INSBMarketActivityPrizeService extends BaseService<INSBMarketActivityPrize> {

    public String queryActivityPrizeListById(String id);

    public void saveObject(INSBMarketActivityPrize insbMarketActivityPrize);

    public void updateObject(INSBMarketActivityPrize insbMarketActivityPrize);

    public void deleteObject(String id);

    public long queryPagingListCount(Map<String,Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);//<!--add refresh-->
}
