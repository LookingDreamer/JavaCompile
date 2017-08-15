package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAccountDetails;

import java.util.List;
import java.util.Map;

public interface INSBAccountDetailsDao extends BaseDao<INSBAccountDetails> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public int intoDetails(INSBAccountDetails detail);

    public Double queryBalance(String accountid);

    public Double queryTodayIncome(String accountid);

    public Boolean exist(INSBAccountDetails detail);

    public List<Map<String, Object>> queryBusinessList(Map<String, Object> map);

    public List<Map<String, Object>> queryWithdrawListByDate(Map<String, Object> map);

    public List<Map<String, Object>> queryCashPrizeListByDate(Map<String, Object> map);

    public List<Map<String, Object>> queryPersonalWealthByDate(Map<String, Object> map);

    public List<Map<String, Object>> queryPersonalWealthByMonth(Map<String, Object> map);

    public List<Map<String, Object>> querySumBusinessByMonth(Map<String, Object> map);

    public List<Map<String, Object>> queryBusinessListByTaskid(Map<String, Object> map);

    public List<Map<String, Object>> queryCommissionAndPrizeByTask(Map<String, Object> map);

}