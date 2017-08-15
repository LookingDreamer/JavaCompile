package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMarketActivityPrize;

import java.util.List;
import java.util.Map;

public interface INSBMarketActivityPrizeDao extends BaseDao<INSBMarketActivityPrize> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public String queryActivityPrizeListById(String id);

    public void saveObject(INSBMarketActivityPrize iNSBMarketActivityPrize );

    public void updateObject(INSBMarketActivityPrize iNSBMarketActivityPrize );

    public void deleteObject(String id);//<!--addrefresh-->

}