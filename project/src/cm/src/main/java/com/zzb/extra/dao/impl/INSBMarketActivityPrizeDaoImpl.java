package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBMarketActivityPrizeDao;
import com.zzb.extra.entity.INSBMarketActivityPrize;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.List;
import java.util.Map;

@Repository
public class INSBMarketActivityPrizeDaoImpl extends BaseDaoImpl<INSBMarketActivityPrize> implements
        INSBMarketActivityPrizeDao {
    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryActivityPrizeList", map);
    }

    @Override
    public String queryActivityPrizeListById(String id) {
        return null;
    }

    @Override
    public void saveObject(INSBMarketActivityPrize iNSBMarketActivityPrize) {
        this.sqlSessionTemplate.insert("INSBMarketActivityPrize_insert",iNSBMarketActivityPrize);
    }

    @Override
    public void updateObject(INSBMarketActivityPrize iNSBMarketActivityPrize) {
        this.sqlSessionTemplate.update("INSBMarketActivityPrize_updateById", iNSBMarketActivityPrize);
    }

    @Override
    public void deleteObject(String id) {
        this.sqlSessionTemplate.delete("INSBMarketActivityPrize_deleteById", id);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryActivityPrizeListCount", map);
    }
    //<!--addrefresh-->

}