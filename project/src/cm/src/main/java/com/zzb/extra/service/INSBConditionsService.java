package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.ConditionsVo;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.model.AgentTaskModel;

import java.util.Map;

public interface INSBConditionsService extends BaseService<INSBConditions> {

    String queryPagingList(Map<String, Object> map);

    public Map<String, Object> queryParams(AgentTaskModel agentTaskModel);

    public Boolean execute(String source, String sourceId, Map<String, Object> params);

    public String saveConditions(INSBConditions conditionsVo);

    public Boolean copyConditions(String source, String sourceId, String fromId);

    public String deleteConditionsById(String id);

    public void deleteConditionsBySourceId(String sourceId);

    public String queryAgreementIdByTask(AgentTaskModel agentTaskModel);

    public String queryConditionsText(String sourceId);//<!--add-->

    public String queryDeptCodeByTask(AgentTaskModel agentTaskModel);
}