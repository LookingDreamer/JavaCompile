package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.entity.INSBCommissionRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface INSBCommissionRateDao extends BaseDao<INSBCommissionRate> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<INSBCommissionRate> queryCommissionByAgreement(String providerCode,String productcode,String channelSource);

    public List<INSBCommissionRate> queryCommissionByAgreementTest(String providerCode,String productcode,String channelSource,ArrayList statuslist);

    public INSBCommissionRate queryCommissionRateById(String id);

    public List<Map<String, String>> initAgreementList();

    public int insertCommissionRate(INSBCommissionRate commissionRate);

    public int updateCommissionRate(INSBCommissionRate commissionRate);

    public int updateCommissionRateStatus(INSBCommissionRate commissionRate);

    public int delCommissionRate(String id);

    public int updateTerminalTimeByNoti(INSBCommissionRate commissionRate);

    public List<Map<Object, Object>> queryCommissionProvider(String productcode);

    public int cleanCommissionRateStatus(String agreementid,String productcode);

    public List<Map<String, Object>> queryCommissionRateRule(Map<String, Object> map);

    public long queryCommissionRateRuleCount(Map<String, Object> map);

}