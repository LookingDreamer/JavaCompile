package com.zzb.extra.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBAccountDetailsDao;
import com.zzb.extra.entity.INSBAccountDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class INSBAccountDetailsDaoImpl extends BaseDaoImpl<INSBAccountDetails> implements
        INSBAccountDetailsDao {
    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryAccountDetailsList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryAccountDetailsListCount", map);
    }

    @Override
    public int intoDetails(INSBAccountDetails detail) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), detail);
    }

    @Override
    public Double queryBalance(String accountid) {
        Double balance = this.sqlSessionTemplate.selectOne("queryBalance", accountid);
        return balance != null ? balance : 0;
    }

    @Override
    public Double queryTodayIncome(String accountid) {
        Double balance = this.sqlSessionTemplate.selectOne("queryTodayIncome", accountid);
        return balance != null ? balance : 0;
    }

    @Override
    public Boolean exist(INSBAccountDetails detail) {
        INSBAccountDetails insbAccountDetails = this.sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), detail);
        return insbAccountDetails != null;
    }

    public List<Map<String, Object>> queryBusinessList(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryBusinessList", map);
    }

    public List<Map<String, Object>> queryWithdrawListByDate(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryWithdrawListByDate", map);
    }

    public List<Map<String, Object>> queryCashPrizeListByDate(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryCashPrizeListByDate", map);
    }

    public List<Map<String, Object>> queryPersonalWealthByDate(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryPersonalWealthByDate", map);
    }

    public List<Map<String, Object>> queryPersonalWealthByMonth(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryPersonalWealthByMonth", map);
    }

    public List<Map<String, Object>> querySumBusinessByMonth(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("querySumBusinessByMonth", map);
    }

    public List<Map<String, Object>> queryBusinessListByTaskid(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryBusinessListByTaskid", map);
    }

    public List<Map<String, Object>> queryCommissionAndPrizeByTask(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryCommissionAndPrizeByTask", map);
    }
}