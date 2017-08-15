package com.zzb.cm.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBAutomaticVerifyService;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Dai on 2016/7/27.
 */
@Service
public class INSBAutomaticVerifyServiceImpl implements INSBAutomaticVerifyService {

    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBCarinfoDao carinfoDao;

    public boolean verifyTrafficTax(String processInstanceId, String insComcode) {
        if (StringUtil.isEmpty(processInstanceId)) {
            LogUtil.error("主流程实例id为空");
            return false;
        }

        int ic = commonQuoteinfoService.getInsureConfigType(processInstanceId, insComcode);
        if (ic != 4) {
            LogUtil.error(processInstanceId + "("+insComcode+")--投保险种类别不是 交强+车船税");
            return false;
        }

        if (commonQuoteinfoService.hasUserComments(processInstanceId, insComcode)) {
            LogUtil.error(processInstanceId + "("+insComcode+")--用户备注不为空");
            return false;
        }

        INSBCarinfohis carInfo = commonQuoteinfoService.getCarInfo(processInstanceId, insComcode);
        INSBCarmodelinfohis carModelInfo = null;

        if (carInfo != null) {
            if ("1".equals(carInfo.getIsTransfercar())) {
                LogUtil.error(processInstanceId + "--投保车辆为过户车");
                return false;
            }

            INSBCarinfo carinfo = new INSBCarinfo();
            carinfo.setTaskid(processInstanceId);
            carinfo = carinfoDao.selectOne(carinfo);
            if (carinfo != null) {
                carModelInfo = commonQuoteinfoService.getCarModelInfo(carinfo.getId(), insComcode);
            }
        }

        if (!commonQuoteinfoService.validateVehicleInfo(carInfo, carModelInfo, false, processInstanceId)) {
            return false;
        }

        //车辆性质
        int usingPropertyType = commonQuoteinfoService.detechUsingPropertyType(carInfo.getCarproperty());

        switch (usingPropertyType) {
            case 1:
                Integer seat = carModelInfo.getSeat();
                boolean isNullDisplacement = false;

                Double displacement = carModelInfo.getDisplacement();
                if (displacement == null || displacement == 0.0) {
                    isNullDisplacement = true;
                }

                if (seat == null || seat == 0 || isNullDisplacement) {
                    LogUtil.error(processInstanceId + "--车辆性质=客车，座位数=空，或排气量=空");
                    return false;
                }
                break;

            case 2:
                Double unwrtweight = carModelInfo.getUnwrtweight();
                boolean isNullFullweight = false;

                Double fullweight = carModelInfo.getFullweight();
                if (fullweight == null || fullweight == 0.0) {
                    isNullFullweight = true;
                }

                if (unwrtweight == null || unwrtweight == 0.0 || isNullFullweight) {
                    LogUtil.error(processInstanceId + "--车辆性质=货车，核定载质量=空，或整备质量=空");
                    return false;
                }
                break;

            case 3:
                String tzclass = commonQuoteinfoService.getVehicleType(carInfo, carModelInfo);
                isNullFullweight = false;

                fullweight = carModelInfo.getFullweight();
                if (fullweight == null || fullweight == 0.0) {
                    isNullFullweight = true;
                }

                if (StringUtil.isEmpty(tzclass) || isNullFullweight) {
                    LogUtil.error(processInstanceId + "--车辆性质=特种车，特种车类型=空，或整备质量=空");
                    return false;
                }
                break;

            default:
                LogUtil.error(processInstanceId + "--车辆使用性质不确定");
                return false;
        }

        Object compulsoryClaimTimes = null;
        boolean hasClaimRecord = false;

        INSBRulequeryotherinfo rulequeryotherinfo = commonQuoteinfoService.getRulequeryotherinfo(processInstanceId);
        if (rulequeryotherinfo != null && StringUtil.isNotEmpty(rulequeryotherinfo.getBwcompulsoryclaimtimes())) {
            if (!commonQuoteinfoService.isNumeric(rulequeryotherinfo.getBwcompulsoryclaimtimes())) {
                compulsoryClaimTimes = commonQuoteinfoService.getSupplementMappingValue(rulequeryotherinfo.getBwcompulsoryclaimtimes(), "compulsoryClaimTimes");
            } else {
                compulsoryClaimTimes = rulequeryotherinfo.getBwcompulsoryclaimtimes();
            }

            INSBRulequeryclaims rulequeryclaims = commonQuoteinfoService.getRulequeryclaims(processInstanceId);
            if (rulequeryclaims != null) {
                hasClaimRecord = true;
            }
        }

        if (compulsoryClaimTimes == null || StringUtil.isEmpty(compulsoryClaimTimes.toString())) {
            INSBLastyearinsureinfo lastyearinsureinfo = commonQuoteinfoService.getLastYearInsureInfo(processInstanceId);
            LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
            if (lastyearinsureinfo == null) {
                lastYearPolicyInfoBean = commonQuoteinfoService.getLastYearPolicyInfoBean(processInstanceId);
            }

            Map<String, Object> lastYearInfo = commonQuoteinfoService.getLastYearInfo(lastYearPolicyInfoBean, lastyearinsureinfo);
            compulsoryClaimTimes = lastYearInfo.get("compulsoryClaimTimes");

            if ((lastyearinsureinfo != null && StringUtil.isNotEmpty(lastyearinsureinfo.getJqclaims())) ||
                    (lastYearPolicyInfoBean != null && lastYearPolicyInfoBean.getLastYearClaimBean() != null && lastYearPolicyInfoBean.getLastYearClaimBean().getJqclaims() != null)) {
                hasClaimRecord = true;
            }
        }

        boolean isNewVehicle = commonQuoteinfoService.isNewVehicle(carInfo, true);

        if (!isNewVehicle && (compulsoryClaimTimes == null || StringUtil.isEmpty(compulsoryClaimTimes.toString()))) {
            LogUtil.error(processInstanceId + "--交强险出险次数为空");
            return false;
        }

        String platformCode = commonQuoteinfoService.getAgentPlatform(processInstanceId);

        //辽宁、深圳
        if ("1221000000".equals(platformCode) || "1121000000".equals(platformCode) || "1244500000".equals(platformCode)) {
            boolean hasClaim = false;
            if (!isNewVehicle) {
                try {
                    if (Integer.parseInt(compulsoryClaimTimes.toString()) > 0) {
                        hasClaim = true;
                    }
                } catch (Exception e) {
                    if (!"null".equalsIgnoreCase(compulsoryClaimTimes.toString()) && !compulsoryClaimTimes.toString().contains("没有理赔")) {
                        hasClaim = true;
                    }
                }
            }
            if (hasClaim && !hasClaimRecord) {
                LogUtil.error(processInstanceId + "--交强险出险次数>0，交强险理赔记录=空");
                return false;
            }
        }

        //福建、四川、重庆
        if ("1235000000".equals(platformCode) || "1135000000".equals(platformCode) || "1251000000".equals(platformCode) || "1250000000".equals(platformCode)) {
            LastClaimBackInfo lastClaimBackInfo = commonQuoteinfoService.getLastClaimBackInfo(processInstanceId);
            if (lastClaimBackInfo != null && StringUtil.isNotEmpty(lastClaimBackInfo.getDrunkDriving())) {
                LogUtil.error(processInstanceId + "--平台查询有查询到酒驾系数");
                return false;
            }
        }

        return true;
    }

    public boolean verifyNewVehicle(String processInstanceId, String insComcode) {
        return false;
    }

    public int verifyCommercial(String processInstanceId, String insComcode) {
        return 0;
    }
}
