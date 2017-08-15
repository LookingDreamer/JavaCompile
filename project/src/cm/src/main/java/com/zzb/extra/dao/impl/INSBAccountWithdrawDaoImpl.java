package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBAccountWithdrawDao;
import com.zzb.extra.entity.INSBAccountWithdraw;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.List;
import java.util.Map;

@Repository
public class INSBAccountWithdrawDaoImpl extends BaseDaoImpl<INSBAccountWithdraw> implements
        INSBAccountWithdrawDao {

    @Override
    public INSBAccountWithdraw queryById(String id) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectById"), id);
    }

    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryAccountWithdrawList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryAccountWithdrawListCount", map);
    }

    @Override
    public int putAccountWithdraw(INSBAccountWithdraw accountWithdraw) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), accountWithdraw);
    }

    @Override
    public int updateStatusAndNoti(INSBAccountWithdraw accountWithdraw) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateStatusAndNotiById"), accountWithdraw);
    }

    @Override
    public Double queryBlocked(String accountid) {
        Double blockedFund = this.sqlSessionTemplate.selectOne("queryBlocked", accountid);
        return blockedFund != null ? blockedFund : 0;
    }

    @Override
    public Double queryMonthWithdraw(String accountid) {
        Double monthWithdraw = this.sqlSessionTemplate.selectOne("queryMonthWithdraw", accountid);
        return monthWithdraw != null ? monthWithdraw : 0;
    }

    @Override
    public String queryAgentNameByAccountid(String accountid){
        return this.sqlSessionTemplate.selectOne("queryAgentNameByAccountid",accountid);
    }

}