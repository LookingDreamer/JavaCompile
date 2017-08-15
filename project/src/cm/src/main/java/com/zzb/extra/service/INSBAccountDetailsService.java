package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.AccountDetailVo;
import com.zzb.extra.entity.INSBAccountDetails;

import java.util.List;
import java.util.Map;

public interface INSBAccountDetailsService extends BaseService<INSBAccountDetails> {

    String queryPagingList(Map<String, Object> map);

    public String queryBalance(String agentid);

    public Boolean intoAccountDetails(String accountId, String fundType, String fundSource, Double amount, String noti, String operator,String taskid);

    public String detector();

    public String refreshRedPackage();

    public String sendRedPackets(String taskid,String providercode,String acountid);

    public String giveRedPackets(String taskid,String agentid,String providercode);

    public String genCommissionAndRedPackets(String taskid,String providercode);
    //任务99关联的mini营销活动代码发布cm后台

    public String refreshRedPackets(String taskid,String providercode);

    public List<Map<String,Object>> queryBusinessList(String startDate,String endDate);

    public List<Map<String,Object>> queryWithdrawListByDate(String startDate,String endDate,String withdrawStatus);

    public List<Map<String,Object>> queryCashPrizeListByDate(String startDate,String endDate);

    public List<Map<String,Object>> queryPersonalWealthByDate(String startDate,String endDate);

    public List<Map<String,Object>> queryPersonalWealthByMonth(String startDate);

    public List<Map<String,Object>> querySumBusinessByMonth(String startDate);

    public List<Map<String,Object>> queryCommissionAndPrizeByTask(String taskid,String agentid);

}