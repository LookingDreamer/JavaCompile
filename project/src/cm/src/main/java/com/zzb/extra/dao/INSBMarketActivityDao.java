package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMarketActivity;

import java.util.List;
import java.util.Map;

public interface INSBMarketActivityDao extends BaseDao<INSBMarketActivity> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<Map<Object, Object>> queryEffectiveActivity(Map<String, Object> map);
    //<!--add refresh-->

    public Map findById(String id);

    public void saveObject(INSBMarketActivity iNSBMarketActivity);

    public void updateObject(INSBMarketActivity iNSBMarketActivity);

    public void deleteObject(String id);

    public List<Map<Object, Object>> queryActivityPrizeListById(String id);

    public long queryActivityPrizeListCountById(String id);

    public List<Map<Object, Object>> queryActivityCondListById(Map<Object, Object> map);

    public long queryActivityCondCountById(Map<Object, Object> map);//<!--add-->

    public long selectMaxTmpcode();
    
    
    public List<INSBMarketActivity> selectAll();
}