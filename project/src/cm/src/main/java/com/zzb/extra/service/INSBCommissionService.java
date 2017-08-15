package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.model.AgentTaskModel;

import java.util.List;
import java.util.Map;

public interface INSBCommissionService extends BaseService<INSBCommission> {

    String queryPagingList(Map<String, Object> map);

    public List<INSBCommission> queryCommissions(AgentTaskModel agentTaskModel);

    public Boolean isLocked(AgentTaskModel agentTaskModel);

    public Boolean lockTaskCommission(AgentTaskModel agentTaskModel);
    //任务99关联的mini营销活动代码发布cm后台
    public Boolean isCompleted(AgentTaskModel agentTaskModel);

    public String saveCommission(INSBCommission commission);

    public void deleteCommissionByTask(AgentTaskModel agentTaskModel);

    public Boolean existRate(Map<String, Object> map);

    public Map<String, Object> queryPagingListReturnMap(Map<String, Object> map);
}