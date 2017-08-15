package com.zzb.cm.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBQuoteVerifyService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by Dai on 2016/4/14.
 */

@Service
public class INSBQuoteVerifyServiceImpl implements INSBQuoteVerifyService {

    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;


    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //平台机构的起保日期的超出天数
    private final Map<String, Map<String, Integer>> configPlatformOfoverEffectDate = new HashMap<>(6);
    //默认值
    private final Integer biz = 90;
    private final Integer trf = 90;

    //使用平台车价的平台机构
    private final List<String> configPlatformOfusePlatformCarPrice = new ArrayList<>(7);

    //不需要执行指定车价规则的平台机构
    private final List<String> configPlatformOfnoSpecifyPriceRule = new ArrayList<>(2);

    {
        //加载配置文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config/quote-verify-config");

        String configOfusePlatformCarPriceStr = resourceBundle.getString("com.zzb.cm.usePlatformCarPrice.platforms");
        if (StringUtils.isNotBlank(configOfusePlatformCarPriceStr)) {
            configPlatformOfusePlatformCarPrice.addAll(Arrays.asList(configOfusePlatformCarPriceStr.split(",")));
        }

        String configOfnoSpecifyPriceRuleStr = resourceBundle.getString("com.zzb.cm.noSpecifyPriceRule.platforms");
        if (StringUtils.isNotBlank(configOfnoSpecifyPriceRuleStr)) {
            configPlatformOfnoSpecifyPriceRule.addAll(Arrays.asList(configOfnoSpecifyPriceRuleStr.split(",")));
        }

        String configOfoverEffectDateStr = resourceBundle.getString("com.zzb.cm.overEffectDate.platforms");
        if (StringUtils.isNotBlank(configOfoverEffectDateStr)) {
            String[] configOfoverEffectDate = configOfoverEffectDateStr.split(",");

            for (String platCfg : configOfoverEffectDate) {
                if (StringUtils.isBlank(platCfg)) continue;

                String[] config = platCfg.split(":");
                if (config.length == 0) continue;

                String overEffectDate = null;
                if (config.length >= 2) {
                    overEffectDate = config[1];
                }

                Map<String, Integer> dayConfig = new HashMap<>(2);
                dayConfig.put("biz", biz);
                dayConfig.put("trf", trf);

                if (StringUtils.isNotBlank(overEffectDate)) {
                    String[] dates = overEffectDate.split("-");

                    if (dates.length == 1){
                        dayConfig.put("biz", Integer.valueOf(dates[0]));
                    }else if (dates.length >= 2){
                        dayConfig.put("biz", Integer.valueOf(dates[0]));
                        dayConfig.put("trf", Integer.valueOf(dates[1]));
                    }
                }

                configPlatformOfoverEffectDate.put(config[0], dayConfig);
            }
        }
    }


    @Override
    public int verifyCommercial(String processInstanceId, String insComcode) {
        if (StringUtils.isBlank(processInstanceId)) {
            LogUtil.error("主流程实例id为空");
            return 0;
        }

        int ic = commonQuoteinfoService.getInsureConfigType(processInstanceId, insComcode);
        if (ic != 1 && ic != 3) {
            LogUtil.error(processInstanceId + "("+insComcode+")--投保险种类别不是 单商或混保");
            return 0;
        }

        if (commonQuoteinfoService.hasUserComments(processInstanceId, insComcode)) {
            LogUtil.error(processInstanceId + "("+insComcode+")--用户备注不为空");
            return 0;
        }

        INSBCarinfohis carInfo = commonQuoteinfoService.getCarInfo(processInstanceId, insComcode);
        INSBCarmodelinfohis carModelInfo = null;

        if (carInfo != null) {
            if (!commonQuoteinfoService.isRegistedMonthsBefore(carInfo.getRegistdate(), -9)) {
                LogUtil.error(processInstanceId + "--车辆初登日期<9个月");
                return 0;
            }

            carModelInfo = commonQuoteinfoService.getCarModelInfo(carInfo.getId(), insComcode);
        }

        if (!commonQuoteinfoService.validateVehicleInfo(carInfo, carModelInfo, false, processInstanceId)) {
            return 0;
        }

        List<INSBSpecifydriver> specifydrivers = commonQuoteinfoService.getSpecifydrivers(processInstanceId, carInfo.getId());
        if (specifydrivers != null && specifydrivers.size() > 1) {
            LogUtil.error(processInstanceId + "--指定驾驶人有多个");
            return 0;
        }

        if ("1".equals(carInfo.getIsTransfercar())) {
            LogUtil.error(processInstanceId + "--投保车辆为过户车");
            return 0;
        }

        //车辆使用性质类型
        int usingPropertyType = commonQuoteinfoService.detechUsingPropertyType(carInfo.getCarproperty());

        switch (usingPropertyType) {
            case 1:
                Integer seat = carModelInfo.getSeat();
                Double displacement = carModelInfo.getDisplacement();

                if (seat == null || seat == 0 || displacement == null || displacement == 0.0) {
                    LogUtil.error(processInstanceId + "--车辆性质=客车，座位数=空，或排气量=空");
                    return 0;
                }
                break;

            case 2:
                Double unwrtweight = carModelInfo.getUnwrtweight();
                Double fullweight = carModelInfo.getFullweight();

                if (unwrtweight == null || unwrtweight == 0.0 || fullweight == null || fullweight == 0.0) {
                    LogUtil.error(processInstanceId + "--车辆性质=货车，核定载质量=空，或整备质量=空");
                    return 0;
                }
                break;

            case 3:
                Double fullWeight = carModelInfo.getFullweight();

                if (fullWeight == null || fullWeight == 0.0) {
                    LogUtil.error(processInstanceId + "--车辆性质=特种车，整备质量=空");
                    return 0;
                }
                break;

            default:
                LogUtil.error(processInstanceId + "--车辆使用性质不确定");
                return 0;
        }

        LastYearPolicyInfoBean lastYearPolicyInfoBean = commonQuoteinfoService.getLastYearPolicyInfoBean(processInstanceId);
        INSBLastyearinsureinfo lastyearinsureinfo = null;
        if (lastYearPolicyInfoBean == null) {
            lastyearinsureinfo = commonQuoteinfoService.getLastYearInsureInfo(processInstanceId);
        }

        String firstInsureType = commonQuoteinfoService.getFirstinsuretype(lastYearPolicyInfoBean, lastyearinsureinfo);
        String LastBizInsCom = commonQuoteinfoService.getLastYearInsCompany(lastYearPolicyInfoBean, lastyearinsureinfo);

        if ("0".equals(firstInsureType) && StringUtils.isBlank(LastBizInsCom)) {
            LogUtil.error(processInstanceId + "--投保类型=非首次投保，上年商业险投保公司=空");
            return 0;
        }

        String platformCode = commonQuoteinfoService.getAgentPlatform(processInstanceId);

        Map<String, Date> effectDay = getEffectDay(processInstanceId, insComcode);
        Date bizStartdate = effectDay.get("businessStartdate");
        Date trfStartdate = effectDay.get("trafficStartdate");

        Map<String, Integer> overEffectDay = getOverEffectDay(platformCode);
        int bizOverDay = overEffectDay.get("biz");
        int trfOverDay = overEffectDay.get("trf");

        Map<String, Object> lastYearInfo = commonQuoteinfoService.getLastYearInfo(lastYearPolicyInfoBean, lastyearinsureinfo);
        Object commercialClaimTimes = lastYearInfo.get("commercialClaimTimes");
        Object compulsoryClaimTimes = lastYearInfo.get("compulsoryClaimTimes");

        if (1 == ic) {
            if (tooFarAway(bizStartdate, bizOverDay)) {
                //拒绝承保
                LogUtil.error(processInstanceId + "("+insComcode+")--商业险起保日期>"+bizOverDay+"天");
                return 2;
            }

            if (StringUtils.isBlank(firstInsureType) ||
                    ("0".equals(firstInsureType) && (commercialClaimTimes == null || StringUtils.isBlank(commercialClaimTimes.toString())))) {
                LogUtil.error(processInstanceId + "--投保类型=空，或投保类型=非首次投保且上年商业险理赔次数=空");
                return 0;
            }

        } else if (3 == ic) {
            boolean bToo = tooFarAway(bizStartdate, bizOverDay);
            boolean tToo = tooFarAway(trfStartdate, trfOverDay);

            if (bToo && tToo) {
                //拒绝承保
                LogUtil.error(processInstanceId + "("+insComcode+")--商业险起保日期>"+bizOverDay+"天，交强险起保日期>"+trfOverDay+"天");
                return 2;

            } else if (bToo) {
                LogUtil.error(processInstanceId + "("+insComcode+")--商业险起保日期>"+bizOverDay+"天，交强险起保日期<"+trfOverDay+"天");
                return 3;
            } else if (tToo) {
                LogUtil.error(processInstanceId + "("+insComcode+")--商业险起保日期<"+bizOverDay+"天，交强险起保日期>"+trfOverDay+"天");
                return 4;
            }

            if (StringUtils.isBlank(firstInsureType) ||
                    ("0".equals(firstInsureType) && (commercialClaimTimes == null || StringUtils.isBlank(commercialClaimTimes.toString()))) ||
                    compulsoryClaimTimes == null || StringUtils.isBlank(compulsoryClaimTimes.toString())) {
                LogUtil.error(processInstanceId + "--投保类型=空，或投保类型=非首次投保且上年商业险理赔次数=空，或交强险理赔次数=空");
                return 0;
            }
        }

        LastClaimBackInfo lastClaimBackInfo = commonQuoteinfoService.getLastClaimBackInfo(processInstanceId);

        if (isUsingPlatformCarPrice(platformCode) && (lastClaimBackInfo == null ||
                isNotSatisfyPlatformCarPrice(lastClaimBackInfo.getPlatformCarPrice(), carInfo.getTaxprice()))) {
            LogUtil.error(processInstanceId + "--要求使用平台车价，但平台车价为空，或平台车价！=新车购置价");
            return 0;
        }

        Map<String, Map<String, Object>> riskItems = commonQuoteinfoService.findRiskItems(processInstanceId, insComcode,
                Arrays.asList("VehicleTax", "VehicleTaxOverdueFine", "VehicleCompulsoryIns", "VehicleDemageIns"));

        boolean ruleNotEqTaxOverdueFineEnable = false;

        //深圳
        if ("1244500000".equals(platformCode)) {
            ruleNotEqTaxOverdueFineEnable = true;
        }

        //辽宁
        if (("1221000000".equals(platformCode) || "1121000000".equals(platformCode)) && commonQuoteinfoService.isInsuredCompany(processInstanceId, "2005")) {
            LogUtil.error(processInstanceId + "("+insComcode+")--投保了人保");
            return 0;
        }

        //江苏
        if ("1232000000".equals(platformCode) || "1132000000".equals(platformCode)) {
            if (lastClaimBackInfo != null && ("是".equals(lastClaimBackInfo.getPureESale()) || "1".equals(lastClaimBackInfo.getPureESale()) ||
                    "y".equalsIgnoreCase(lastClaimBackInfo.getPureESale()) || "true".equalsIgnoreCase(lastClaimBackInfo.getPureESale())) &&
                    commonQuoteinfoService.isInsuredCompany(processInstanceId, "2005")) {
                LogUtil.error(processInstanceId + "("+insComcode+")--投保人保并且通过纯电销投保");
                return 0;
            }
        }

        //北京
        if ("1211000000".equals(platformCode) || "1111000000".equals(platformCode)) {
            if (3 == ic) {
                Map<String, Object> VehicleCompulsoryIns = riskItems.get("VehicleCompulsoryIns");
                if (VehicleCompulsoryIns == null || VehicleCompulsoryIns.isEmpty() ||
                        VehicleCompulsoryIns.get("amountPrice") == null || (Double) VehicleCompulsoryIns.get("amountPrice") == 0.0) {
                    LogUtil.error(processInstanceId + "(" + insComcode + ")--投保险种=混保，交强险保费=空");
                    return 0;
                }
            }
            ruleNotEqTaxOverdueFineEnable = true;
        }

        //福建
        if ("1235000000".equals(platformCode) || "1135000000".equals(platformCode)) {
            if (riskItems != null && !riskItems.isEmpty()) {
                Map<String, Object> vehicleDemageIns = riskItems.get("VehicleDemageIns");
                if (vehicleDemageIns != null && !vehicleDemageIns.isEmpty() &&
                        vehicleDemageIns.get("amount") != null && (Double)vehicleDemageIns.get("amount") >= 0.0) {
                    LogUtil.error(processInstanceId + "(" + insComcode + ")--投保了车辆损失险");
                    return 0;
                }
            }
            ruleNotEqTaxOverdueFineEnable = true;
        }

        if (ruleNotEqTaxOverdueFineEnable && ruleNotEqTaxOverdueFine(riskItems, lastClaimBackInfo)) {
            LogUtil.error(processInstanceId + "(" + insComcode + ")--代缴车船税时，（往年补缴车船税+滞纳金）！= 车船税滞纳金");
            return 0;
        }

        return 1;
    }

    @Override
    public boolean verifyTraffic(String processInstanceId, String insComcode) {
        if (StringUtils.isBlank(processInstanceId)) {
            LogUtil.error("主流程实例id为空");
            return false;
        }

        int ic = commonQuoteinfoService.getInsureConfigType(processInstanceId, insComcode);
        if (ic != 2) {
            LogUtil.error(processInstanceId + "("+insComcode+")--投保险种类别不是 单交强");
            return false;
        }

        if (commonQuoteinfoService.hasUserComments(processInstanceId, insComcode)) {
            LogUtil.error(processInstanceId + "("+insComcode+")--用户备注不为空");
            return false;
        }

        INSBCarinfohis carInfo = commonQuoteinfoService.getCarInfo(processInstanceId, insComcode);
        INSBCarmodelinfohis carModelInfo = null;

        if (carInfo != null) {
            carModelInfo = commonQuoteinfoService.getCarModelInfo(carInfo.getId(), insComcode);
        }

        if (!commonQuoteinfoService.validateVehicleInfo(carInfo, carModelInfo, false, processInstanceId)) {
            return false;
        }

        if ("1".equals(carInfo.getIsTransfercar())) {
            LogUtil.error(processInstanceId + "--投保车辆为过户车");
            return false;
        }

        //是否投保车船税
        boolean hasVesselTaxes = false;

        Map<String, Map<String, Object>> riskItems = commonQuoteinfoService.findRiskItems(processInstanceId, insComcode, Arrays.asList("VehicleTax"));
        if (riskItems != null && !riskItems.isEmpty()) {
            Map vehicleTax = riskItems.get("VehicleTax");
            if (vehicleTax != null && !vehicleTax.isEmpty()) {
                hasVesselTaxes = true;
            }
        }

        //车辆性质
        int usingPropertyType = commonQuoteinfoService.detechUsingPropertyType(carInfo.getCarproperty());

        switch (usingPropertyType) {
            case 1:
                Integer seat = carModelInfo.getSeat();
                boolean isNullDisplacement = false;

                if (hasVesselTaxes) {
                    Double displacement = carModelInfo.getDisplacement();
                    if (displacement == null || displacement == 0.0) {
                        isNullDisplacement = true;
                    }
                }

                if (seat == null || seat == 0 || isNullDisplacement) {
                    LogUtil.error(processInstanceId + "--车辆性质=客车，座位数=空" + (hasVesselTaxes ? "，或排气量=空" : ""));
                    return false;
                }
                break;

            case 2:
                Double unwrtweight = carModelInfo.getUnwrtweight();
                boolean isNullFullweight = false;

                if (hasVesselTaxes) {
                    Double fullweight = carModelInfo.getFullweight();
                    if (fullweight == null || fullweight == 0.0) {
                        isNullFullweight = true;
                    }
                }

                if (unwrtweight == null || unwrtweight == 0.0 || isNullFullweight) {
                    LogUtil.error(processInstanceId + "--车辆性质=货车，核定载质量=空" + (hasVesselTaxes ? "，或整备质量=空" : ""));
                    return false;
                }
                break;

            case 3:
                isNullFullweight = false;

                if (hasVesselTaxes) {
                    Double fullweight = carModelInfo.getFullweight();
                    if (fullweight == null || fullweight == 0.0) {
                        isNullFullweight = true;
                    }
                }

                if (isNullFullweight) {
                    LogUtil.error(processInstanceId + "--车辆性质=特种车，整备质量=空");
                    return false;
                }
                break;

            default:
                LogUtil.error(processInstanceId + "--车辆使用性质不确定");
                return false;
        }

        LastYearPolicyInfoBean lastYearPolicyInfoBean = commonQuoteinfoService.getLastYearPolicyInfoBean(processInstanceId);
        INSBLastyearinsureinfo lastyearinsureinfo = null;
        if (lastYearPolicyInfoBean == null) {
            lastyearinsureinfo = commonQuoteinfoService.getLastYearInsureInfo(processInstanceId);
        }

        Map<String, Object> lastYearInfo = commonQuoteinfoService.getLastYearInfo(lastYearPolicyInfoBean, lastyearinsureinfo);
        Object compulsoryClaimTimes = lastYearInfo.get("compulsoryClaimTimes");

        if (compulsoryClaimTimes == null || StringUtils.isBlank(compulsoryClaimTimes.toString())) {
            LogUtil.error(processInstanceId + "--交强险出险次数为空");
            return false;
        }

        String platformCode = commonQuoteinfoService.getAgentPlatform(processInstanceId);

        //辽宁、深圳
        if ("1221000000".equals(platformCode) || "1121000000".equals(platformCode) || "1244500000".equals(platformCode)) {
            boolean hasClaim = false;
            try {
                if (Integer.parseInt(compulsoryClaimTimes.toString()) > 0) {
                    hasClaim = true;
                }
            } catch (Exception e) {
                if (!"null".equalsIgnoreCase(compulsoryClaimTimes.toString()) && !compulsoryClaimTimes.toString().contains("没有理赔")) {
                    hasClaim = true;
                }
            }
            if (hasClaim) {
                if ((lastYearPolicyInfoBean == null || lastYearPolicyInfoBean.getLastYearClaimBean() == null ||
                        lastYearPolicyInfoBean.getLastYearClaimBean().getJqclaims() == null) &&
                        (lastyearinsureinfo == null || StringUtils.isBlank(lastyearinsureinfo.getJqclaims()))) {
                    LogUtil.error(processInstanceId + "--交强险出险次数>0，交强险理赔记录=空");
                    return false;
                }
            }
        }

        //福建
        if ("1235000000".equals(platformCode) || "1135000000".equals(platformCode)) {
            LastClaimBackInfo lastClaimBackInfo = commonQuoteinfoService.getLastClaimBackInfo(processInstanceId);
            if (lastClaimBackInfo != null && StringUtils.isNotBlank(lastClaimBackInfo.getDrunkDriving())) {
                LogUtil.error(processInstanceId + "--平台查询有查询到酒驾系数");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean verifyNewVehicle(String processInstanceId, String insComcode) {
        if (StringUtils.isBlank(processInstanceId)) {
            LogUtil.error("主流程实例id为空");
            return false;
        }

        if (commonQuoteinfoService.hasUserComments(processInstanceId, insComcode)) {
            LogUtil.error(processInstanceId + "("+insComcode+")--用户备注不为空");
            return false;
        }

        INSBCarinfohis carInfo = commonQuoteinfoService.getCarInfo(processInstanceId, insComcode);
        INSBCarmodelinfohis carModelInfo = null;

        if (carInfo != null) {
            carModelInfo = commonQuoteinfoService.getCarModelInfo(carInfo.getId(), insComcode);
        }

        String platformCode = commonQuoteinfoService.getAgentPlatform(processInstanceId);

        boolean validatePriceType = !isNoSpecifyPriceRule(platformCode);
        if (!commonQuoteinfoService.validateVehicleInfo(carInfo, carModelInfo, validatePriceType, processInstanceId)) {
            return false;
        }

        //有车牌
        boolean hasPlate = false;
        if(!StringUtils.isEmpty(carInfo.getCarlicenseno()) && !"新车未上牌".equals(carInfo.getCarlicenseno())) {
            hasPlate = true;
        }

        //初登日期为9个月以内
        boolean isRegisted9MonthsSince = false;
        if (carInfo.getRegistdate() != null && !commonQuoteinfoService.isRegistedMonthsBefore(carInfo.getRegistdate(), -9)) {
            isRegisted9MonthsSince = true;
        }

        if(hasPlate && isRegisted9MonthsSince) {
            LastClaimBackInfo lastClaimBackInfo = commonQuoteinfoService.getLastClaimBackInfo(processInstanceId);
            if (lastClaimBackInfo != null && lastClaimBackInfo.getBizPolicies() != null && lastClaimBackInfo.getBizPolicies().size() > 0 &&
                    StringUtils.isNotBlank(lastClaimBackInfo.getBizPolicies().get(0).getPolicyId())) {
                LogUtil.error(processInstanceId + "--上年商业险保单号不为空");
                return false;
            }
        }

        //北京、河南
        if ("1211000000".equals(platformCode) || "1111000000".equals(platformCode) ||
                "1241000000".equals(platformCode) || "1141000000".equals(platformCode)) {
            if (carInfo.getRegistdate() != null && commonQuoteinfoService.isRegistedMonthsBefore(carInfo.getRegistdate(), -1)) {
                LogUtil.error(processInstanceId + "--初登日期不在1个月以内");
                return false;
            }
        }

        //天津
        if ("1212000000".equals(platformCode) || "1112000000".equals(platformCode)) {
            if (!hasPlate || (carInfo.getRegistdate() != null && !isRegisted9MonthsSince)) {
                LogUtil.error(processInstanceId + "--初登日期不在9个月以内，或无车牌");
                return false;
            }
        }

        //其他机构平台
        if (carInfo.getRegistdate() != null && !isRegisted9MonthsSince) {
            LogUtil.error(processInstanceId + "--初登日期不在9个月以内");
            return false;
        }

        //非 湖南、深圳、河南、长沙
        if (!"1243100000".equals(platformCode) && !"1143000000".equals(platformCode) && !"1244500000".equals(platformCode) &&
                !"1241000000".equals(platformCode) && !"1141000000".equals(platformCode) &&
                !"1243000000".equals(platformCode) && !"1143100000".equals(platformCode)) {
            List<INSBSpecifydriver> specifydrivers = commonQuoteinfoService.getSpecifydrivers(processInstanceId, carInfo.getId());
            if (specifydrivers != null && specifydrivers.size() > 1) {
                LogUtil.error(processInstanceId + "--指定驾驶人有多个");
                return false;
            }
        }

        return true;
    }

    //不需要执行指定车价规则的平台机构
    private boolean isNoSpecifyPriceRule(String platformCode) {
        LogUtil.debug("不需要执行指定车价规则的平台机构：" + configPlatformOfnoSpecifyPriceRule);
        return (StringUtils.isNotBlank(platformCode) && configPlatformOfnoSpecifyPriceRule.contains(platformCode));
    }

    //使用平台车价的平台机构
    private boolean isUsingPlatformCarPrice(String platformCode) {
        LogUtil.debug("使用平台车价的平台机构：" + configPlatformOfusePlatformCarPrice);
        return (StringUtils.isNotBlank(platformCode) && configPlatformOfusePlatformCarPrice.contains(platformCode));
    }

    private boolean isNotSatisfyPlatformCarPrice(Double platformCarPrice, Double replacementValue) {
        if (platformCarPrice == null || replacementValue == null) return true;

        return platformCarPrice.doubleValue() != replacementValue.doubleValue();
    }

    public boolean isEqTaxOverdueFine(Map<String, Object> vehicleTaxOverdueFine, LastClaimBackInfo lastClaimBackInfo) {
        if ((vehicleTaxOverdueFine == null || vehicleTaxOverdueFine.isEmpty()) && lastClaimBackInfo == null) return true;

        double vehicleTaxOverdueFineOf = 0.0, lwArrearsTax = 0.0;

        if (lastClaimBackInfo != null) {
            if (StringUtils.isNotBlank(lastClaimBackInfo.getVehicleTaxOverdueFine())) {
                try {
                    vehicleTaxOverdueFineOf = Double.parseDouble(lastClaimBackInfo.getVehicleTaxOverdueFine());
                } catch (Throwable t) {}
            }

            if (StringUtils.isNotBlank(lastClaimBackInfo.getLwArrearsTax())) {
                try {
                    lwArrearsTax = Double.parseDouble(lastClaimBackInfo.getLwArrearsTax());
                } catch (Throwable t) {}
            }

            try {
                vehicleTaxOverdueFineOf = vehicleTaxOverdueFineOf + lwArrearsTax;
            } catch (Throwable t) {}
        }

        double vehicleTaxOverdueFinePrice = 0.0;

        if (vehicleTaxOverdueFine != null) {
            Object overdueFinePrice = vehicleTaxOverdueFine.get("amountPrice");
            vehicleTaxOverdueFinePrice = overdueFinePrice!=null ? (Double) overdueFinePrice : 0.0;
        }

        return vehicleTaxOverdueFinePrice == vehicleTaxOverdueFineOf;
    }

    private boolean ruleNotEqTaxOverdueFine(Map<String, Map<String, Object>> riskItems, LastClaimBackInfo lastClaimBackInfo) {
        if (riskItems != null && !riskItems.isEmpty()) {
            Map vehicleTax = riskItems.get("VehicleTax");
            Map<String, Object> vehicleTaxOverdueFine = riskItems.get("VehicleTaxOverdueFine");

            if (vehicleTax != null && !vehicleTax.isEmpty() && !isEqTaxOverdueFine(vehicleTaxOverdueFine, lastClaimBackInfo)) {
                return true;
            }
        }

        return false;
    }

    public boolean tooFarAway(Date effectDate, int farAway) {
        if (effectDate == null) return false;

        try {
            Date insEffectDate = dateFormat.parse(dateFormat.format(effectDate));
            Date now = dateFormat.parse(dateFormat.format(new Date()));

            Calendar cal = Calendar.getInstance();
            cal.setTime(insEffectDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(now);
            long time2 = cal.getTimeInMillis();

            long between_days = (time1-time2) / 86400000;

            return between_days > farAway;
        } catch (Exception e) {
            LogUtil.error("解析日期出错：" + effectDate + " : " + e.getMessage());
            return false;
        }
    }

    public Map<String, Integer> getOverEffectDay(String platformCode) {
        Map<String, Integer> result = configPlatformOfoverEffectDate.get(platformCode);
        if (result == null || result.isEmpty()) {
            result = getDefaultOverEffectDay();
        }

        return result;
    }

    private Map<String, Integer> getDefaultOverEffectDay() {
        Map<String, Integer> result = new HashMap<>(2);
        result.put("biz", biz);
        result.put("trf", trf);
        return result;
    }

    public Map<String, Date> getEffectDay(String taskId, String insComcode) {
        Map<String, Date> data = new HashMap<>(2);

        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskId);

        if (StringUtils.isNotBlank(insComcode)) {
            insbPolicyitem.setInscomcode(insComcode);
        }

        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(insbPolicyitem);
        if (policyitemList == null || policyitemList.isEmpty()) return data;

        for (INSBPolicyitem policyitem : policyitemList) {
            if("0".equals(policyitem.getRisktype())){
                if(policyitem.getStartdate() != null){
                    data.put("businessStartdate", policyitem.getStartdate());
                }
                if(policyitem.getEnddate() != null){
                    data.put("businessEnddate", policyitem.getEnddate());
                }
            } else if("1".equals(policyitem.getRisktype())){
                if(policyitem.getStartdate() != null){
                    data.put("trafficStartdate", policyitem.getStartdate());
                }
                if(policyitem.getEnddate() != null){
                    data.put("trafficEnddate", policyitem.getEnddate());
                }
            }
        }

        return data;
    }
}
