package com.zzb.chn.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.chn.entity.INSBCommissionratio;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CHNCommissionRatioService extends BaseService<INSBCommissionratio> {
    String queryPagingList(Map<String, Object> map);

    public String queryTaskCommission(AgentTaskModel agentTaskModel);

    public List<Map<String, String>> initChannelList();

    public String addCommissionRatio(INSBCommissionratio commissionRatio);

    public String copyCommissionRatio(String commissionRatioId, String operator);

    public String updateCommissionRatio(INSBCommissionratio commissionRatio);

    public String updateCommissionRatioStatus(INSBCommissionratio commissionRatio);

    public String delCommissionRatio(String id);

    public String testCommissionRule(Map<String, Object> params);

    public String queryCommissionRatioChannel();

    public Map<String, String> batchUpdateCommissionRatioStatus(String ratioIds,String status,String userCode);

    public String batchCopyCommissionRatio(String ratioIds,String channelIds,String userCode);

    public String updateCommissionRatioTerminalTime(INSBCommissionratio commissionRatio);

    public String batchUpdateCommissionRatioTerminalTime(String ratioIds,Date terminalTime,String userCode);

    public String batchDeleteCommissionRatio(String ratioIds,String userCode);
}