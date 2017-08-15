package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBMarketActivityDao;
import com.zzb.extra.entity.INSBMarketActivity;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBMarketActivityDaoImpl extends BaseDaoImpl<INSBMarketActivity> implements
        INSBMarketActivityDao {
    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryActivityList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryActivityListCount", map);
    }

    @Override
    public List<Map<Object, Object>> queryEffectiveActivity(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryEffectiveActivity", map);
        //<!--add refresh-->
    }

    @Override
    public Map findById(String id) {
        return this.sqlSessionTemplate.selectOne("INSBMarketActivity_selectById", id);
    }

    @Override
    public void saveObject(INSBMarketActivity iNSBMarketActivity) {
        this.sqlSessionTemplate.insert("INSBMarketActivity_insert",iNSBMarketActivity);
    }

    @Override
    public void updateObject(INSBMarketActivity iNSBMarketActivity) {
        this.sqlSessionTemplate.update("INSBMarketActivity_updateById", iNSBMarketActivity);
    }

    @Override
    public void deleteObject(String id) {
        this.sqlSessionTemplate.delete("INSBMarketActivity_deleteById", id);
    }

    @Override
    public List<Map<Object, Object>> queryActivityPrizeListById(String id) {
        return this.sqlSessionTemplate.selectList("INSBMarketActivity_queryPrizeById", id);
    }

    @Override
    public long queryActivityPrizeListCountById(String id) {
        return this.sqlSessionTemplate.selectOne("INSBMarketActivity_queryPrizeCountById",id);
    }

    @Override
    public List<Map<Object, Object>> queryActivityCondListById(Map<Object, Object> map) {
        return this.sqlSessionTemplate.selectList("INSBMarketActivity_queryCondById",map);
    }

    @Override
    public long queryActivityCondCountById(Map<Object, Object> map) {
        return this.sqlSessionTemplate.selectOne("INSBMarketActivity_queryCondCountById",map);
    }
    //<!--add-->

	@Override
	public long selectMaxTmpcode() {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMaxTmpcode"), new HashMap());
	}

	@Override
	public List<INSBMarketActivity> selectAll() {
		
		return this.sqlSessionTemplate.selectList("INSBMarketActivity_selectAll", new HashMap());
	}
}