package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.model.AgentTaskModel;

import java.util.List;
import java.util.Map;

public interface INSBConditionsDao extends BaseDao<INSBConditions> {

    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<INSBConditions> queryConditions(Map<String, Object> map);

    public int insertConditions(INSBConditions conditions);

    public int updateConditions(INSBConditions conditions);

    public int deleteConditionsById(String id);

    public int deleteConditionsBySourceId(String sourceId);

    public Boolean containsParam(String paramname);

    public String queryAgreementIdByTask(AgentTaskModel agentTaskModel);

    public Map<String, Object> queryParamsByTask(AgentTaskModel agentTaskModel);

    public Object queryParamsBySqlName(String sqlName, String fieldName, AgentTaskModel agentTaskModel);

    public Map<String, Object> queryInsured(String insKindCode, AgentTaskModel agentTaskModel);

    public Map<String, Object> queryLastInsertInfo(AgentTaskModel agentTaskModel);

    public String queryDeptCodeByTask(AgentTaskModel agentTaskModel);
}