package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBConditionParamsDao;
import com.zzb.extra.entity.INSBConditionParams;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.List;
import java.util.Map;

@Repository
public class INSBConditionParamsDaoImpl extends BaseDaoImpl<INSBConditionParams> implements
        INSBConditionParamsDao {

    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryConditionParamsList", map);
    }

    @Override
    public List<INSBConditionParams> queryConditionParamsByTag(String tag){
        return this.sqlSessionTemplate.selectList("queryConditionParamsByTag", tag);
    }

    @Override
    public INSBConditionParams queryById(String id) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("queryById"), id);
    }

    @Override
    public INSBConditionParams queryByName(String paramname) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("queryByName"), paramname);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryConditionParamsListCount", map);
    }

    @Override
    public int insertParam(INSBConditionParams conditions) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), conditions);
    }

    @Override
    public int updateParam(INSBConditionParams conditions) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateById"), conditions);
    }

    @Override
    public int deleteParamById(String id) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteById"), id);
    }

    @Override
    public Boolean containsParam(INSBConditionParams conditions) {
        int result = this.sqlSessionTemplate.selectOne(this.getSqlName("containsParam"), conditions);
        return result > 0;
    }
    //<!--add-->
}