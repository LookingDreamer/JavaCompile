package com.zzb.extra.dao.impl;

import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.dao.INSBCommissionRateDao;
import com.zzb.extra.entity.INSBCommissionRate;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBCommissionRateDaoImpl extends BaseDaoImpl<INSBCommissionRate> implements
        INSBCommissionRateDao {
    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryCommissionRateList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryCommissionRateListCount", map);
    }

    @Override
    public List<INSBCommissionRate> queryCommissionByAgreement(String agreementid,String productcode,String channelSource) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("agreementid",agreementid);
        map.put("productcode",productcode);
        map.put("channelsource",channelSource);
        return this.sqlSessionTemplate.selectList("queryCommissionByAgreement", map);
    }

    @Override
    public List<INSBCommissionRate> queryCommissionByAgreementTest(String agreementid,String productcode,String channelSource,ArrayList statuslist) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("agreementid",agreementid);
        map.put("productcode",productcode);
        map.put("channelsource",channelSource);
        map.put("statuslist",statuslist);
        return this.sqlSessionTemplate.selectList("queryCommissionByAgreementTest", map);
    }

    @Override
    public INSBCommissionRate queryCommissionRateById(String id) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectById"), id);
    }

    @Override
    public int insertCommissionRate(INSBCommissionRate commissionRate) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), commissionRate);
    }

    @Override
    public int updateCommissionRate(INSBCommissionRate commissionRate) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateById"), commissionRate);
    }

    @Override
    public int updateCommissionRateStatus(INSBCommissionRate commissionRate) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateStatusById"), commissionRate);
    }

    @Override
    public int delCommissionRate(String id) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteById"), id);
    }

    @Override
    public List<Map<String, String>> initAgreementList() {
        return this.sqlSessionTemplate.selectList("initAgreementList");
    }

    @Override
    public int updateTerminalTimeByNoti(INSBCommissionRate commissionRate) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateTerminalTimeByNoti"), commissionRate);
    }

    @Override
    public List<Map<Object, Object>> queryCommissionProvider(String productcode) {
        return this.sqlSessionTemplate.selectList("queryCommissionProvider",productcode);
    }

    @Override
    public int cleanCommissionRateStatus(String agreementid,String productcode) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("agreementid",agreementid);
        map.put("productcode",productcode);
        return this.sqlSessionTemplate.update(this.getSqlName("cleanCommissionRateStatus"), map);
    }

    @Override
    public List<Map<String, Object>> queryCommissionRateRule(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("queryCommissionRateRule", map);
    }

    @Override
    public long queryCommissionRateRuleCount(Map<String, Object> map){
        return this.sqlSessionTemplate.selectOne("queryCommissionRateRuleCount", map);
    }
}