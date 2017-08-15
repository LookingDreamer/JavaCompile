package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.model.AgentPrizeModel;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBAgentPrizeDaoImpl extends BaseDaoImpl<INSBAgentPrize> implements
        INSBAgentPrizeDao {

    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryAgentPrizeList", map);
    }

    /**
     * 查询有赏金的保单
     * @param agentId
     * @return
     */
    public List<Map<String, Object>> queryGotPrizesPolicy(String agentId) {
        return this.sqlSessionTemplate.selectList("queryGotPrizesPolicyList", agentId);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryAgentPrizeListCount", map);
    }

    @Override
    public long queryAgentPrizeListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryAgentPrizeListByNameCount", map);
    }

    @Override
    public List<Map<Object, Object>> queryAgentPrizeList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryAgentPrizeListByName", map);
    }

    @Override
    public List<AgentPrizeModel> queryAgentPrizes(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryAgentPrizes", map);
    }

    @Override
    public int completeTask(String taskid, String activityprizeid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", taskid);
        map.put("activityprizeid", activityprizeid);
        return this.sqlSessionTemplate.update(this.getSqlName("completeByTaskId"), map);
    }

    @Override
    public void deletePrizeByAgentIdAndTaskId(Map<String, Object> map) {
        this.sqlSessionTemplate.delete(this.getSqlName("deleteByTaskId"),map);
    }

    @Override
    public void deleteTempAgentPrize(Map<String, Object> map) {
        this.sqlSessionTemplate.delete(this.getSqlName("deleteTempAgentPrize"),map);
    }

    @Override
    public List<Map<Object, Object>> queryAgent(Map<String, Object> map) {
       return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgent"), map);
    }

    @Override
    public long queryRecommentAmount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectRecommendCounts"), map);
    }

    @Override
    public List<INSBAgentPrize> queryPrizeByAgentIdAndTask(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("INSBAgentPrize_selectByTaskId", map);
    }//<!--add-->//任务99关联的mini营销活动代码发布cm后台

    @Override
    public List<INSBAgentPrize> queryPaidOrder(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("INSBAgentPrize_queryPaidOrder", map);
    }

    @Override
    public List<INSBAgentPrize> queryNoPaymentOrder(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("INSBAgentPrize_queryNoPaymentOrder", map);
    }

    @Override
    public int updateByTaskIdAndProviderCode(String taskid, String providercode,String agentid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", taskid);
        map.put("providercode", providercode);
        map.put("agentid", agentid);
        return this.sqlSessionTemplate.update(this.getSqlName("updateByTaskIdAndPro"), map);
    }

    @Override
    public List<Map<String, Object>> queryRegisterPrize(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("INSBAgentPrize_queryRegisterPrize", map);
    }

    @Override
    public List<Map<String, Object>> queryAgentByTaskID(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("INSBAgentPrize_queryAgentByTaskID", map);
    }
}