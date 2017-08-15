package com.zzb.cm.service;

import com.zzb.cm.entity.*;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dai on 2016/7/28.
 */
public interface INSBCommonQuoteinfoService {

    //测定车辆使用性质 1:客车，2:货车，3:特种车，-1:其他
    public int detechUsingPropertyType(String usingProperty);

    //获取车辆种类
    public String getVehicleType(INSBCarinfohis carInfo, INSBCarmodelinfohis carModelInfo);

    //获取车辆种类
    public String getVehicleType(INSBCarinfo carInfo, INSBCarmodelinfo carModelInfo);

    //测定投保类型 1:单商，2:单交，3:混保，4:交强险+车船税，5:车船税，-1:其他
    public int getInsureConfigType(String taskId, String insComcode);

    //前端是否有提交备注
    public boolean hasUserComments(String taskId, String insComcode);

    //检查车信息
    public boolean validateVehicleInfo(INSBCarinfohis carInfo, INSBCarmodelinfohis carModelInfo, boolean validatePriceType, String taskId);

    //指定日期是否在 当天加上指定天数之前
    public boolean isRegistedMonthsBefore(Date firstRegistDate, int months);

    //查询指定编码的险项信息
    public Map<String, Map<String, Object>> findRiskItems(String taskId, String insComcode, List<String> riskCodes);

    //获取上年保单信息
    public LastYearPolicyInfoBean getLastYearPolicyInfoBean(String taskId);

    //获取上年投保信息
    public INSBLastyearinsureinfo getLastYearInsureInfo(String taskId);

    //获取上年投保信息
    public Map<String, Object> getLastYearInfo(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo);

    //获取上年投保公司
    public String getLastYearInsCompany(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo);

    //获取投保类型
    public String getFirstinsuretype(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo);

    //获取上年出险信息
    public LastClaimBackInfo getLastClaimBackInfo(String taskId);

    //获取指定驾驶员
    public List<INSBSpecifydriver> getSpecifydrivers(String taskId, String carInfoId);

    //获取代理人所属平台机构
    public String getAgentPlatform(String taskId);

    //指定任务中是否有该投保公司
    public boolean isInsuredCompany(String taskId, String insComcode);

    //获取车辆信息
    public INSBCarinfohis getCarInfo(String taskId, String insComcode);

    //获取车型信息
    public INSBCarmodelinfohis getCarModelInfo(String carInfoId, String insComcode);

    //判断是否新车
    public boolean isNewVehicle(INSBCarinfohis carinfohis, boolean detectFirstRegistDate);

    //获取被保人信息
    public INSBInsuredhis getInsuredinfo(String taskId, String insComcode);

    public INSBRulequeryclaims getRulequeryclaims(String taskId);

    public INSBRulequeryotherinfo getRulequeryotherinfo(String taskId);

    public Integer getSupplementMappingValue(String keyText, String mapType);

    public boolean isNumeric(String str);

    public Map<String, Integer> getPlateType(String taskid, String inscomcode, String useProps, Integer seat, Double unwrtweight);

    public Integer getJgVehicleType(String taskid, String inscomcode, String useProps, Integer seat, Double unwrtweight);
    
    public boolean isDoPTQuery(int updateBefore, int updateAfter);

    /**
     * 根据平台查询回写的上年投保日期计算本年的起保与终止日期
     * @param taskid
     * @return 如果没有上年投保日期，则empty Map，否则Map with key:
     * 如果有上年商业险投保日期：commEffectDate(商业起保日期), commExpireDate(商业终保日期)
     * 如果有上年交强险投保日期：trafEffectDate(交强起保日期), trafExpireDate(交强终保日期)
     */
    public Map<String, Date> getNextPolicyDate(String taskid);
}
