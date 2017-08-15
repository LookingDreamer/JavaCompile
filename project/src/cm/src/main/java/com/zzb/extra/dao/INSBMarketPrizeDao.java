package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMarketPrize;

import java.util.List;
import java.util.Map;

public interface INSBMarketPrizeDao extends BaseDao<INSBMarketPrize> {

    public List<INSBMarketPrize> getPrizeList(Map<String, Object> map);

    public void saveObject(INSBMarketPrize iNSBMarketPrize );

    public void updateObject(INSBMarketPrize iNSBMarketPrize );

    public void deleteObject(String id);

    public List<Map<Object, Object>> queryPrizeList(Map<String, Object> map);

    public long queryPrizeListCount(Map<String, Object> map);

    public Map findById(String id);//<!--add- refreshrefresh->


}