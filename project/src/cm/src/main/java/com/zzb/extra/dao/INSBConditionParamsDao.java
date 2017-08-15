package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBConditionParams;

import java.util.List;
import java.util.Map;

public interface INSBConditionParamsDao extends BaseDao<INSBConditionParams> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<INSBConditionParams> queryConditionParamsByTag(String tag);

    public INSBConditionParams queryById(String id);

    public INSBConditionParams queryByName(String paramname);

    public int insertParam(INSBConditionParams conditions);

    public int updateParam(INSBConditionParams conditions);

    public int deleteParamById(String id);

    public Boolean containsParam(INSBConditionParams conditions);//<!--add-->
}