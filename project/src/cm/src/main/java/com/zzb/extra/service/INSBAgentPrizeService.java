package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBAgentPrize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface INSBAgentPrizeService extends BaseService<INSBAgentPrize> {

    String queryPrizes(Map<String, Object> map);

    public String queryAgentPrizeList(Map<String, Object> map);

    public void deletePrizeByTaskId(Map<String, Object> map);

    public String queryAvailablePrizes(Map<String, Object> map);

    public List<Map<Object, Object>> queryAgentPrizeListNoTotal(Map<String, Object> map);

    public String queryGotPrizes(Map<String, Object> map); //<!--add//任务99关联的mini营销活动代码发布cm后台-->

    /**
     * 有赏金的保单
     * @param agentId
     * @return
     */
    public Map<String,Object> queryGotPrizesPolicy(String agentId);

    public int updateByTaskIdAndProviderCode(String taskid, String providercode,String agentid);

    public List<INSBAgentPrize> queryNoPaymentOrder(Map<String, Object> map);

    public String queryPrizeByAgentIdAndTask(Map<String, Object> map);

}