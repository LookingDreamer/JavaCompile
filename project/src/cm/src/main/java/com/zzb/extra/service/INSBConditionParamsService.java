package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;

import java.util.List;
import java.util.Map;

public interface INSBConditionParamsService extends BaseService<INSBConditionParams> {
    String queryPagingList(Map<String, Object> map);

    public String saveConditionParam(INSBConditionParams params);

    public String deleteConditionParam(String id);

    public List<INSBConditionParams> queryConditionParamsByTag(String tag);//<!--add-->
}