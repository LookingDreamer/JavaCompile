package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMarketPrize;

import java.util.List;
import java.util.Map;

public interface INSBMarketPrizeService extends BaseService<INSBMarketPrize> {

    public List<INSBMarketPrize> getPrizeList(Map<String,Object> map);

    public void saveObject(INSBMarketPrize insbMarketPrize);

    public void updateObject(INSBMarketPrize insbMarketPrize);

    public void deleteObject(String id);

    public String queryPrizeList(Map<String,Object> map);
    //<!--add refreshrefreshrefresh-->

    public Map findById(String id);//<!--add refreshrefreshrefresh-->
}