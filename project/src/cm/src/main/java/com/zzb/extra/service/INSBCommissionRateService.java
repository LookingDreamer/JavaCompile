package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;

import java.util.List;
import java.util.Map;

public interface INSBCommissionRateService extends BaseService<INSBCommissionRate> {

    String queryPagingList(Map<String, Object> map);

    public String queryTaskCommission(AgentTaskModel agentTaskModel);

    public List<Map<String, String>> initAgreementList();

    public String addCommissionRate(INSBCommissionRate commissionRate);

    public String copyCommissionRate(String commissionRateId, String operator);

    public String updateCommissionRate(INSBCommissionRate commissionRate);

    public String updateCommissionRateStatus(INSBCommissionRate commissionRate);

    public String delCommissionRate(String id);

    public String testCommissionRule(Map<String, Object> params);

    public String queryCommissionProvider(String productcode);
}