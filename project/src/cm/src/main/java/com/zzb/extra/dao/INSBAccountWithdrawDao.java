package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAccountWithdraw;

import java.util.List;
import java.util.Map;

public interface INSBAccountWithdrawDao extends BaseDao<INSBAccountWithdraw> {
    public INSBAccountWithdraw queryById(String id);

    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public int putAccountWithdraw(INSBAccountWithdraw accountWithdraw);

    public int updateStatusAndNoti(INSBAccountWithdraw accountWithdraw);

    public Double queryBlocked(String accountid);

    public Double queryMonthWithdraw(String accountid);

    public String queryAgentNameByAccountid(String accountid);

}