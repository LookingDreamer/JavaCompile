package com.zzb.extra.dao.impl;

import com.zzb.extra.model.AgentTaskModel;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBConditionsDao;
import com.zzb.extra.entity.INSBConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBConditionsDaoImpl extends BaseDaoImpl<INSBConditions> implements
        INSBConditionsDao {

    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryConditionsList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryConditionsListCount", map);
    }

    @Override
    public List<INSBConditions> queryConditions(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryConditions", map);
    }

    @Override
    public int insertConditions(INSBConditions conditions) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), conditions);
    }

    @Override
    public int updateConditions(INSBConditions conditions) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateById"), conditions);
    }

    @Override
    public int deleteConditionsById(String id) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteById"), id);
    }

    @Override
    public int deleteConditionsBySourceId(String sourceid) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteBySourceId"), sourceid);
    }

    @Override
    public Boolean containsParam(String paramname) {
        int result = this.sqlSessionTemplate.selectOne(this.getSqlName("containsParam"), paramname);
        return result > 0;
    }

    @Override
    public String queryAgreementIdByTask(AgentTaskModel agentTaskModel) {
        return this.sqlSessionTemplate.selectOne("queryAgreementIdByTask", agentTaskModel);
    }

    @Override
    public String queryDeptCodeByTask(AgentTaskModel agentTaskModel) {
        return this.sqlSessionTemplate.selectOne("queryDeptCodeByTask", agentTaskModel);
    }

    @Override
    public Map<String, Object> queryParamsByTask(AgentTaskModel agentTaskModel) {
        return this.sqlSessionTemplate.selectOne("queryParamsByTask", agentTaskModel);
    }

    @Override
    public Object queryParamsBySqlName(String sqlName, String fieldName, AgentTaskModel agentTaskModel) {
        Map<String, Object> map = this.sqlSessionTemplate.selectOne(sqlName, agentTaskModel);
        return map.get(fieldName);
    }

    @Override
    public Map<String, Object> queryInsured(String insKindCode, AgentTaskModel agentTaskModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskid", agentTaskModel.getTaskid());
        map.put("providercode", agentTaskModel.getProvidercode());
        map.put("inskindcode", insKindCode);
        Double coverage = this.sqlSessionTemplate.selectOne("queryCoverage", map);

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("insured" + insKindCode, coverage != null);
        resultMap.put("coverage" + insKindCode, coverage != null ? coverage : 0D);
        return resultMap;
    }

    @Override
    public Map<String, Object> queryLastInsertInfo(AgentTaskModel agentTaskModel) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> infoList1 = this.sqlSessionTemplate.selectOne("queryLastInsertInfo1", agentTaskModel);
        if(null == infoList1){
            infoList1 = new HashMap<String,Object>();
            infoList1.put("commercialClaimTimes",-1);
            infoList1.put("compulsoryClaimTimes",-1);
            infoList1.put("commercialClaimRate","");
            infoList1.put("firstInsureType","");
        }
        Map<String,Object> infoList2 = this.sqlSessionTemplate.selectOne("queryLastInsertInfo2", agentTaskModel);
        if(null == infoList2){
            infoList2 = new HashMap<String,Object>();
            infoList2.put("lastInsureCo","0");
        }
        map.putAll(infoList1);
        map.putAll(infoList2);
        return map;
    }
}