package com.zzb.chn.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hwc on 2016/11/7.
 */
public interface CHNCommissionRateService  {
    String queryPagingList(Map<String, Object> map);

    public String queryTaskCommission(AgentTaskModel agentTaskModel);

    public List<Map<String, String>> initAgreementList();

    public String addCommissionRate(INSBCommissionRate commissionRate);

    public String copyCommissionRate(String commissionRateId, String operator);

    public String updateCommissionRate(INSBCommissionRate commissionRate);

    public String updateCommissionRateTerminalTime(INSBCommissionRate commissionRate);

    public String updateCommissionRateStatus(INSBCommissionRate commissionRate);

    public String delCommissionRate(String id);

    public String testCommissionRule(Map<String, Object> params);

    public String queryCommissionProvider(String productcode);

    public String queryTaskCommissionRate(AgentTaskModel agentTaskModel);

    public String completeTaskCommission(String taskid);

    public Map<String, String> batchUpdateCommissionRateStatus(String rateIds,String status,String userCode);

    public String batchCopyCommissionRate(String rateIds,String agreementIds,String userCode);

    public String batchUpdateCommissionRateTerminalTime(String rateIds,Date terminalTime,String userCode);

    public String queryCommissionRateRule(Map<String, Object> params);

    public String queryCommissionInfo(Map<String, Object> params);

    public String queryCommissionInfoForBC(Map<String, Object> params);

    public String batchDeleteCommissionRate(String rateIds,String userCode);
}
