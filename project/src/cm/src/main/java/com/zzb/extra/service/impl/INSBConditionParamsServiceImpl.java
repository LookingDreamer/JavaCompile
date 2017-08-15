package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.dao.INSBConditionParamsDao;
import com.zzb.extra.dao.INSBConditionsDao;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBConditionParamsServiceImpl extends BaseServiceImpl<INSBConditionParams> implements
        INSBConditionParamsService {
    @Resource
    private INSBConditionParamsDao insbConditionParamsDao;

    @Resource
    private INSBConditionsDao insbConditionsDao;

    @Override
    protected BaseDao<INSBConditionParams> getBaseDao() {
        return insbConditionParamsDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        long total = insbConditionParamsDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbConditionParamsDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public List<INSBConditionParams> queryConditionParamsByTag(String tag) {
        return insbConditionParamsDao.queryConditionParamsByTag(tag);
    }

    @Override
    public String saveConditionParam(INSBConditionParams params) {
        int result = 0;

        if (insbConditionParamsDao.containsParam(params))
            return ParamUtils.resultMap(false, "参数已存在");

        if (params.getId() == null || params.getId().equals("")) {
            params.setId(UUIDUtils.random());
            params.setCreatetime(new Date());
            result = insbConditionParamsDao.insertParam(params);
        } else {
            params.setModifytime(new Date());
            result = insbConditionParamsDao.updateParam(params);
        }
        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public String deleteConditionParam(String id) {
        INSBConditionParams insbConditionParams = insbConditionParamsDao.queryById(id);
        if (insbConditionsDao.containsParam(insbConditionParams.getParamname()))
            return ParamUtils.resultMap(false, "参数已被使用，不可删除");

        if (insbConditionParamsDao.deleteParamById(id) > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }
    //<!--add-->
}