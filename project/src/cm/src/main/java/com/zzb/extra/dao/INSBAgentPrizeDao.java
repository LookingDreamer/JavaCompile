package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.model.AgentPrizeModel;

import java.util.List;
import java.util.Map;

public interface INSBAgentPrizeDao extends BaseDao<INSBAgentPrize> {

    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    /**
     * 查询有赏金的保单
     * @param agentId
     * @return
     */
    public List<Map<String, Object>> queryGotPrizesPolicy(String agentId);

    public long queryAgentPrizeListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryAgentPrizeList(Map<String, Object> map);

    public List<AgentPrizeModel> queryAgentPrizes(Map<String, Object> map);

    public int completeTask(String taskid,String activityprizeid);

    public void deletePrizeByAgentIdAndTaskId(Map<String,Object> map);

    public List<Map<Object, Object>> queryAgent(Map<String, Object> map);

    public List<INSBAgentPrize> queryPrizeByAgentIdAndTask(Map<String, Object> map);

    public long queryRecommentAmount(Map<String, Object> map);

    public int updateByTaskIdAndProviderCode(String taskid, String providercode,String agentid);

    public List<INSBAgentPrize> queryPaidOrder(Map<String, Object> map);
    //任务99关联的mini营销活动代码发布cm后台

    public List<INSBAgentPrize> queryNoPaymentOrder(Map<String, Object> map);

    public void deleteTempAgentPrize(Map<String,Object> map);

    public List<Map<String, Object>> queryRegisterPrize(Map<String, Object> map);

    public List<Map<String, Object>> queryAgentByTaskID(Map<String, Object> map);
}