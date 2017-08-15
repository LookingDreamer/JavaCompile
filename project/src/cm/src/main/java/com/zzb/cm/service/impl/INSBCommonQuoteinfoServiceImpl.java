package com.zzb.cm.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.CloudQueryUtil;
import com.common.ModelUtil;
import com.common.VehicleTypeMapper;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBRulequeryrepeatinsuredService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastinsured.LastYearClaimBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.model.lastinsured.LastYearSupplierBean;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

/**
 * Created by Dai on 2016/7/28.
 */
@Service
public class INSBCommonQuoteinfoServiceImpl implements INSBCommonQuoteinfoService {
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBCarinfoDao carinfoDao;
    @Resource
    private INSBCarinfohisDao carinfohisDao;
    @Resource
    private INSBCarmodelinfoDao carmodelinfoDao;
    @Resource
    private INSBCarmodelinfohisDao carmodelinfohisDao;
    @Resource
    private INSBInsuredDao insuredDao;
    @Resource
    private INSBInsuredhisDao insuredhisDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private INSBCarconfigDao insbCarconfigDao;
    @Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
    @Resource
    private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Resource
    private INSBSpecifydriverDao insbSpecifydriverDao;
    @Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
    @Resource
    private INSBUsercommentDao insbUsercommentDao;
    @Resource
    private INSCDeptService deptService;
    @Resource
    private IRedisClient redisClient;
    @Resource
    private INSBRulequeryclaimsDao rulequeryclaimsDao;
    @Resource
    private INSBRulequeryotherinfoDao rulequeryotherinfoDao;
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private INSCCodeService inscCodeService;
    @Resource
    private INSBRulequeryrepeatinsuredService insbRulequeryrepeatinsuredService;

    //数字
    private static final String regNumeric = "^\\-?\\d+(\\.?\\d+)?$";

    //使用性质 客车
    private final Set<String> usingPropertiesOfPassengerCar = new HashSet<>(7);
    //使用性质 货车
    private final Set<String> usingPropertiesOfGoodsVan = new HashSet<>(2);
    //使用性质 特种车
    private final Set<String> usingPropertiesOfSpecialVehicle = new HashSet<>(2);

    private final Map<String, Integer> commercialClaimTimesMapper = new HashMap<>(16);

    private final Map<String, Integer> compulsoryClaimTimesMapper = new HashMap<>(6);

    private final Map<String, Integer> firstInsureTypeMapper = new HashMap<>(6);

    {
        usingPropertiesOfPassengerCar.add("1");
        usingPropertiesOfPassengerCar.add("2");
        usingPropertiesOfPassengerCar.add("3");
        usingPropertiesOfPassengerCar.add("4");
        usingPropertiesOfPassengerCar.add("5");
        usingPropertiesOfPassengerCar.add("10");
        usingPropertiesOfPassengerCar.add("11");

        usingPropertiesOfGoodsVan.add("6");
        usingPropertiesOfGoodsVan.add("12");

        usingPropertiesOfSpecialVehicle.add("15");
        usingPropertiesOfSpecialVehicle.add("16");

        commercialClaimTimesMapper.put("连续五年没有理赔", -4);
        commercialClaimTimesMapper.put("连续四年没有理赔", -3);
        commercialClaimTimesMapper.put("连续三年没有理赔", -2);
        commercialClaimTimesMapper.put("连续两年没有理赔", -1);
        commercialClaimTimesMapper.put("去年没有理赔", 0);
        commercialClaimTimesMapper.put("去年发生一次理赔", 1);
        commercialClaimTimesMapper.put("去年发生两次理赔", 2);
        commercialClaimTimesMapper.put("去年发生三次理赔", 3);
        commercialClaimTimesMapper.put("去年发生四次理赔", 4);
        commercialClaimTimesMapper.put("去年发生五次理赔", 5);
        commercialClaimTimesMapper.put("去年发生六次理赔", 6);
        commercialClaimTimesMapper.put("去年发生七次理赔", 7);
        commercialClaimTimesMapper.put("去年发生八次理赔", 8);
        commercialClaimTimesMapper.put("去年发生九次理赔", 9);
        commercialClaimTimesMapper.put("去年发生十次理赔", 10);
        commercialClaimTimesMapper.put("去年发生十次以上理赔", 11);

        compulsoryClaimTimesMapper.put("连续三年没有理赔", -2);
        compulsoryClaimTimesMapper.put("连续两年没有理赔", -1);
        compulsoryClaimTimesMapper.put("上年没有理赔", 0);
        compulsoryClaimTimesMapper.put("新保或上年发生一次有责任不涉及死亡理赔", 1);
        compulsoryClaimTimesMapper.put("上年有两次及以上理赔", 2);
        compulsoryClaimTimesMapper.put("上年有涉及死亡理赔", 3);

        firstInsureTypeMapper.put("非首次投保", 0);
        firstInsureTypeMapper.put("新车首次投保", 1);
        firstInsureTypeMapper.put("旧车首次投保", 2);
    }

    //测定车辆使用性质 1:客车，2:货车，3:特种车，-1:其他
    public int detechUsingPropertyType(String usingProperty) {
        if(usingPropertiesOfPassengerCar.contains(usingProperty)) {
            return 1;
        } else if (usingPropertiesOfGoodsVan.contains(usingProperty)) {
            return 2;
        } else if (usingPropertiesOfSpecialVehicle.contains(usingProperty)) {
            return 3;
        } else {
            return -1;
        }
    }

    private String getPassengerCapacityText(String useProperty, int passengerCapacity) {
        switch (useProperty) {
            case "1":
                if (passengerCapacity < 6) {
                    return "6座以下";
                } else if (passengerCapacity >= 6 && passengerCapacity < 10) {
                    return "6-10座";
                } else {
                    return "10座以上";
                }

            case "10":
            case "11":
            case "2":
            case "3":
            case "4":
                if (passengerCapacity < 6) {
                    return "6座以下";
                } else if (passengerCapacity >= 6 && passengerCapacity < 10) {
                    return "6-10座";
                } else if (passengerCapacity >= 10 && passengerCapacity < 20) {
                    return "10-20座";
                } else if (passengerCapacity >= 20 && passengerCapacity < 36) {
                    return "20-36座";
                } else {
                    return "36座以上";
                }

            default: return "";
        }
    }

    private String getTonnageText(String useProperty, float tonnage) {
        switch (useProperty) {
            case "6":
            case "12":
                if (tonnage < 2000) {
                    return "2吨以下";
                } else if (tonnage >= 2000 && tonnage < 5000) {
                    return "2-5吨";
                } else if (tonnage >= 5000 && tonnage < 10000) {
                    return "5-10吨";
                } else {
                    return "10吨以上";
                }

            default: return "";
        }
    }

    private String getTzClassnameText(String useProperty, String tzclassname) {
        if (StringUtil.isEmpty(tzclassname)) return "";

        switch (useProperty) {
            case "15":
            case "16":
                tzclassname = tzclassname.replaceAll("特种车型", "特种车");
                if (tzclassname.contains("特种车一")) {
                    return "特种车型一";
                } else if (tzclassname.contains("特种车二")) {
                    return "特种车型二";
                } else if (tzclassname.contains("特种车三")) {
                    return "特种车型三";
                } else if (tzclassname.contains("特种车四")) {
                    return "特种车型四";
                } else {
                    return "特种车型一";
                }

            default: return "";
        }
    }

    public String getVehicleType(INSBCarinfohis carInfo, INSBCarmodelinfohis carModelInfo) {
        String vtKey = null;

        try {
            String useProperty = carInfo.getCarproperty();
            String vtText = "";

            switch (useProperty) {
                case "1":
                case "10":
                case "11":
                case "2":
                case "3":
                case "4":
                    vtText = getPassengerCapacityText(useProperty, carModelInfo.getSeat()!=null ? carModelInfo.getSeat() : 0);
                    break;

                case "6":
                case "12":
                    vtText = getTonnageText(useProperty, carModelInfo.getUnwrtweight()!=null ? carModelInfo.getUnwrtweight().floatValue() : 0);
                    break;

                case "15":
                case "16":
                    vtText = getTzClassnameText(useProperty, carModelInfo.getSyvehicletypename());
                    break;
            }

            Map<String, String> vehicleTypeOptions = VehicleTypeMapper.Mapper.get(useProperty);

            for (Map.Entry entry : vehicleTypeOptions.entrySet()) {
                if (entry.getValue().equals(vtText)) {
                    vtKey = entry.getKey().toString();
                    break;
                }
            }

            LogUtil.info(carInfo.getTaskid() + "," + carInfo.getInscomcode() + "解析出车辆种类为 " + vtKey);

        } catch (Exception e) {
            LogUtil.error(carInfo.getTaskid() + "," + carInfo.getInscomcode() + "解析车辆种类时出错：" + e.getMessage());
            e.printStackTrace();
        }

        return vtKey;
    }

    public String getVehicleType(INSBCarinfo carInfo, INSBCarmodelinfo carModelInfo) {
        INSBCarinfohis carinfohis = null;
        if (carInfo != null) {
            carinfohis = new INSBCarinfohis();
            try {
                PropertyUtils.copyProperties(carinfohis, carInfo);
            } catch (Exception e) {
                LogUtil.error("taskid=" + carInfo.getTaskid() + ",复制车辆信息出错: " + e.getMessage());
                e.printStackTrace();
            }
        }

        INSBCarmodelinfohis carmodelinfohis = null;
        if (carModelInfo != null) {
            carmodelinfohis = new INSBCarmodelinfohis();

            try {
                PropertyUtils.copyProperties(carmodelinfohis, carModelInfo);
            } catch (Exception e) {
                LogUtil.error("carInfoId=" + carModelInfo.getCarinfoid() + ",复制车型信息出错: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return getVehicleType(carinfohis, carmodelinfohis);
    }

    //测定投保类型 1:单商，2:单交，3:混保，4:交强险+车船税，5:车船税，-1:其他
    public int getInsureConfigType(String taskId, String insComcode) {
        if (StringUtil.isEmpty(taskId) && StringUtil.isEmpty(insComcode)) {
            LogUtil.error("参数为空");
            return -1;
        }

        boolean isInsuredBusinessRisks = false, isInsuredTrafficFatalities = false, isInsuredTax = false;

        if (StringUtil.isEmpty(insComcode)) {
            INSBCarconfig carconfig = new INSBCarconfig();
            carconfig.setTaskid(taskId);
            List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);

            if (carconfigs != null && carconfigs.size() > 0) {
                for (INSBCarconfig insureConfig : carconfigs) {
                    if ("0".equals(insureConfig.getInskindtype()) || "1".equals(insureConfig.getInskindtype())) {
                        isInsuredBusinessRisks = true;
                    } else if ("2".equals(insureConfig.getInskindtype())) {
                        isInsuredTrafficFatalities = true;
                    } else if ("3".equals(insureConfig.getInskindtype())) {
                        isInsuredTax = true;
                    }
                }
            }
        } else {
            INSBCarkindprice carkindprice = new INSBCarkindprice();
            carkindprice.setTaskid(taskId);
            carkindprice.setInscomcode(insComcode);
            List<INSBCarkindprice> carkindprices = insbCarkindpriceDao.selectList(carkindprice);

            if (carkindprices != null && carkindprices.size() > 0) {
                for (INSBCarkindprice insureConfig : carkindprices) {
                    if ("0".equals(insureConfig.getInskindtype()) || "1".equals(insureConfig.getInskindtype())) {
                        isInsuredBusinessRisks = true;
                    } else if ("2".equals(insureConfig.getInskindtype())) {
                        isInsuredTrafficFatalities = true;
                    } else if ("3".equals(insureConfig.getInskindtype())) {
                        isInsuredTax = true;
                    }
                }
            }
        }

        if (isInsuredBusinessRisks && isInsuredTrafficFatalities) {
            //混保
            return 3;
        } else if (isInsuredBusinessRisks) {
            //单商
            return 1;
        } else if (isInsuredTrafficFatalities && isInsuredTax) {
            //交强险+车船税
            return 4;
        } else if (isInsuredTrafficFatalities) {
            //单交
            return 2;
        } else if (isInsuredTax) {
            //车船税
            return 5;
        }

        return -1;
    }
    
    /**
     * //1-单商,2-单交,3-混保,4:交强险+车船税. 投保类型变更：单交改混保，单交改单商，单商改单交，单商改混保, 修改保险配置发启平台查询
     * @param updateBefore
     * @param updateAfter
     * @return
     */
    public boolean isDoPTQuery(int updateBefore, int updateAfter){
    	if((updateBefore == 2 && updateAfter == 3) 
				|| (updateBefore == 2 && updateAfter == 1) 
				|| (updateBefore == 1 && updateAfter == 2) 
				|| (updateBefore == 1 && updateAfter == 3)){
    		return true;
    	} else {
    		return false;
    	}
    }

    public boolean hasUserComments(String taskId, String insComcode) {
        INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
        insbWorkflowmaintrack.setInstanceid(taskId);
        List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackDao.selectList(insbWorkflowmaintrack);

        if(insbWorkflowmaintracks == null || insbWorkflowmaintracks.size() == 0) return false;

        INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintracks.get(insbWorkflowmaintracks.size()-1);

        INSBUsercomment insbUsercomment = new INSBUsercomment();
        insbUsercomment.setTrackid(workflowmaintrack.getId());
        insbUsercomment.setTracktype(1);
        insbUsercomment.setCommenttype(1);

        List<INSBUsercomment> usercommentList = insbUsercommentDao.selectList(insbUsercomment);

        if (usercommentList == null || usercommentList.size() == 0) return false;

        for (INSBUsercomment usercomment : usercommentList) {
            if (StringUtil.isNotEmpty(usercomment.getCommentcontent())) return true;
        }

        return false;
    }

    public boolean validateVehicleInfo(INSBCarinfohis carInfo, INSBCarmodelinfohis carModelInfo, boolean validatePriceType, String taskId) {
        if (carInfo == null) {
            LogUtil.error(taskId + "--无车辆信息");
            return false;
        }

        if (carModelInfo == null) {
            LogUtil.error(taskId + "--无车型信息");
            return false;
        }

        if (StringUtil.isEmpty(carInfo.getStandardfullname())) {
            LogUtil.error(taskId + "--无车型名称");
            return false;
        }

        if (validatePriceType) {
            if (!"2".equals(carModelInfo.getCarprice())) {
                LogUtil.error(taskId + "--不是指定（自定义）车价");
                return false;
            }
        }

        return true;
    }

    public boolean isRegistedMonthsBefore(Date firstRegistDate, int months) {
        if (firstRegistDate == null) return false;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstRegistDate);
        long time = calendar.getTimeInMillis();

        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, months);
        long time2 = calendar.getTimeInMillis();

        return time <= time2;
    }

    public Map<String, Map<String, Object>> findRiskItems(String taskId, String insComcode, List<String> riskCodes) {
        Map<String, Map<String, Object>> datas = new HashMap<>();

        if (StringUtil.isEmpty(insComcode)) {
            INSBCarconfig carconfig = new INSBCarconfig();
            carconfig.setTaskid(taskId);
            List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);

            if (carconfigs != null && carconfigs.size() > 0) {
                for (INSBCarconfig insureConfig : carconfigs) {
                    if (riskCodes.contains(insureConfig.getInskindcode())) {
                        Map<String, Object> data = new HashMap<>();

                        data.put("inskindcode", insureConfig.getInskindcode());
                        data.put("amount", insureConfig.getAmount());
                        data.put("amountPrice", insureConfig.getAmountprice());
                        data.put("inskindtype", insureConfig.getInskindtype());

                        datas.put(insureConfig.getInskindcode(), data);
                        break;
                    }
                }
            }
        } else {
            INSBCarkindprice carkindprice = new INSBCarkindprice();
            carkindprice.setTaskid(taskId);
            carkindprice.setInscomcode(insComcode);
            List<INSBCarkindprice> carkindprices = insbCarkindpriceDao.selectList(carkindprice);

            if (carkindprices != null && carkindprices.size() > 0) {
                for (INSBCarkindprice insureConfig : carkindprices) {
                    if (riskCodes.contains(insureConfig.getInskindcode())) {
                        Map<String, Object> data = new HashMap<>();

                        data.put("inskindcode", insureConfig.getInskindcode());
                        data.put("amount", insureConfig.getAmount());
                        data.put("amountPrice", insureConfig.getAmountprice());
                        data.put("inskindtype", insureConfig.getInskindtype());

                        datas.put(insureConfig.getInskindcode(), data);
                        break;
                    }
                }
            }
        }

        return datas;
    }

    public LastYearPolicyInfoBean getLastYearPolicyInfoBean(String taskId) {
        return redisClient.get(Constants.TASK, taskId, LastYearPolicyInfoBean.class);
    }

    public INSBLastyearinsureinfo getLastYearInsureInfo(String taskId) {

        return insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
    }

    public Map<String, Object> getLastYearInfo(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo) {
        Map<String, Object> data = new HashMap<>(2);

        if (lastYearPolicyInfoBean != null) {
            LastYearClaimBean lastYearClaimBean = lastYearPolicyInfoBean.getLastYearClaimBean();
            if (lastYearClaimBean != null) {
                data.put("commercialClaimTimes", lastYearClaimBean.getBwcommercialclaimtimes());
                data.put("compulsoryClaimTimes", lastYearClaimBean.getBwcompulsoryclaimtimes());
            }
        } else if (lastyearinsureinfo != null) {
            data.put("commercialClaimTimes", lastyearinsureinfo.getBwcommercialclaimtimes());
            data.put("compulsoryClaimTimes", lastyearinsureinfo.getBwcompulsoryclaimtimes());
        }

        return data;
    }

    public String getLastYearInsCompany(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo) {
        String insCom = null;

        if (lastYearPolicyInfoBean != null) {
            LastYearSupplierBean lastYearSupplierBean = lastYearPolicyInfoBean.getLastYearSupplierBean();
            if (lastYearSupplierBean == null) return null;

            insCom = lastYearSupplierBean.getSupplierid();
            if (StringUtil.isEmpty(insCom)) {
                insCom = lastYearSupplierBean.getSuppliername();
            }
        } else if (lastyearinsureinfo != null) {
            insCom = lastyearinsureinfo.getSupplierid();
            if (StringUtil.isEmpty(insCom)) {
                insCom = lastyearinsureinfo.getSuppliername();
            }
        }

        return insCom;
    }

    public String getFirstinsuretype(LastYearPolicyInfoBean lastYearPolicyInfoBean, INSBLastyearinsureinfo lastyearinsureinfo) {
        if (lastYearPolicyInfoBean != null) {
            LastYearClaimBean lastYearClaimBean = lastYearPolicyInfoBean.getLastYearClaimBean();
            if (lastYearClaimBean == null) return null;

            return lastYearClaimBean.getFirstinsuretype();
        } else if (lastyearinsureinfo != null) {
            return INSBLastyearinsureinfo.convertFirstInsureTypeToCm(lastyearinsureinfo.getFirstinsuretype());
        }

        return null;
    }

    public LastClaimBackInfo getLastClaimBackInfo(String taskId) {
        return CloudQueryUtil.getLastClaimBackInfo(taskId);
    }

    public List<INSBSpecifydriver> getSpecifydrivers(String taskId, String carInfoId) {
        INSBSpecifydriver specifydriver = new INSBSpecifydriver();
        specifydriver.setTaskid(taskId);
        specifydriver.setCarinfoid(carInfoId);
        return insbSpecifydriverDao.selectList(specifydriver);
    }

    //获取该单代理人所属平台公司
    public String getAgentPlatform(String taskId) {
        INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
        quotetotalinfo.setTaskid(taskId);
        INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);

        if (insbQuotetotalinfo == null) {
            return null;
        }

        INSBAgent insbAgent = insbAgentDao.selectByJobnum(insbQuotetotalinfo.getAgentnum());
        if (insbAgent == null) {
            return null;
        }

        INSCDept dept = inscDeptDao.selectByComcode(insbAgent.getDeptid());
        if (dept == null) {
            return null;
        }

        if (StringUtil.isNotEmpty(dept.getParentcodes())) {
            String[] depts = dept.getParentcodes().split("\\+");
            if (depts.length >= 4) {
                return depts[3];
            }
        } else {
            INSCDept platform = deptService.getPlatformDept(dept.getDeptid());
            if (platform != null) {
                return platform.getComcode();
            }
        }

        return null;
    }

    public boolean isInsuredCompany(String taskId, String insComcode) {
        if (StringUtil.isEmpty(insComcode)) {
            return false;
        }

        INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
        insbQuoteinfo.setQuotetotalinfoid(taskId);
        List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);

        if (insbQuoteinfos == null || insbQuoteinfos.isEmpty()) {
            return false;
        }

        for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
            if (StringUtil.isNotEmpty(quoteinfo.getInscomcode()) && quoteinfo.getInscomcode().startsWith(insComcode)) {
                return true;
            }
        }

        return false;
    }

    public INSBCarinfohis getCarInfo(String taskId, String insComcode) {
        INSBCarinfohis carinfohis = new INSBCarinfohis();
        carinfohis.setTaskid(taskId);
        carinfohis.setInscomcode(insComcode);
        carinfohis = carinfohisDao.selectOne(carinfohis);

        if (carinfohis != null) {
            return carinfohis;
        } else {
            INSBCarinfo carinfo = new INSBCarinfo();
            carinfo.setTaskid(taskId);
            carinfo = carinfoDao.selectOne(carinfo);

            if (carinfo != null) {
                carinfohis = new INSBCarinfohis();

                try {
                    PropertyUtils.copyProperties(carinfohis, carinfo);
                    return carinfohis;
                } catch (Exception e) {
                    LogUtil.error("taskid=" + taskId + ",inscomcode=" + insComcode + ",复制车辆信息出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public INSBCarmodelinfohis getCarModelInfo(String carInfoId, String insComcode) {
        INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
        carmodelinfohis.setCarinfoid(carInfoId);
        carmodelinfohis.setInscomcode(insComcode);
        carmodelinfohis = carmodelinfohisDao.selectOne(carmodelinfohis);

        if (carmodelinfohis != null) {
            return carmodelinfohis;
        } else {
            INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
            carmodelinfo.setCarinfoid(carInfoId);
            carmodelinfo = carmodelinfoDao.selectOne(carmodelinfo);

            if (carmodelinfo != null) {
                carmodelinfohis = new INSBCarmodelinfohis();

                try {
                    PropertyUtils.copyProperties(carmodelinfohis, carmodelinfo);
                    return carmodelinfohis;
                } catch (Exception e) {
                    LogUtil.error("carInfoId=" + carInfoId + ",inscomcode=" + insComcode + ",复制车型信息出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public boolean isNewVehicle(INSBCarinfohis carinfohis, boolean detectFirstRegistDate) {
        if (carinfohis == null) return false;

        if (StringUtil.isEmpty(carinfohis.getCarlicenseno()) || carinfohis.getCarlicenseno().contains("未上牌")) {
            return true;
        }

        if (detectFirstRegistDate && carinfohis.getRegistdate() != null) {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();

            calendar.setTime(carinfohis.getRegistdate());
            long that = calendar.getTimeInMillis();

            return now < that || (now - that) / 86400000 < 270;
        }

        return false;
    }

    public INSBInsuredhis getInsuredinfo(String taskId, String insComcode) {
        INSBInsuredhis insuredhis = new INSBInsuredhis();
        insuredhis.setTaskid(taskId);
        insuredhis.setInscomcode(insComcode);
        insuredhis = insuredhisDao.selectOne(insuredhis);

        if (insuredhis != null) {
            return insuredhis;
        } else {
            INSBInsured insured = new INSBInsured();
            insured.setTaskid(taskId);
            insured = insuredDao.selectOne(insured);

            if (insured != null) {
                insuredhis = new INSBInsuredhis();

                try {
                    PropertyUtils.copyProperties(insuredhis, insured);
                    return insuredhis;
                } catch (Exception e) {
                    LogUtil.error("taskId=" + taskId + ",insComcode=" + insComcode + ",复制被保人信息出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public INSBRulequeryclaims getRulequeryclaims(String taskId) {
        if (StringUtil.isEmpty(taskId)) return null;

        INSBRulequeryclaims rulequeryclaims = new INSBRulequeryclaims();
        rulequeryclaims.setTaskid(taskId);
        List<INSBRulequeryclaims> rulequeryclaimses = rulequeryclaimsDao.selectList(rulequeryclaims);

        if (rulequeryclaimses == null || rulequeryclaimses.isEmpty()) return null;

        return rulequeryclaimses.get(0);
    }

    public INSBRulequeryotherinfo getRulequeryotherinfo(String taskId) {
        if (StringUtil.isEmpty(taskId)) return null;

        INSBRulequeryotherinfo rulequeryotherinfo = new INSBRulequeryotherinfo();
        rulequeryotherinfo.setTaskid(taskId);
        List<INSBRulequeryotherinfo> rulequeryotherinfoList = rulequeryotherinfoDao.selectList(rulequeryotherinfo);

        if (rulequeryotherinfoList == null || rulequeryotherinfoList.isEmpty()) return null;

        return rulequeryotherinfoList.get(0);
    }

    public Integer getSupplementMappingValue(String keyText, String mapType) {
        switch (mapType) {
            case "compulsoryClaimTimes": return compulsoryClaimTimesMapper.get(keyText);
            case "commercialClaimTimes": return commercialClaimTimesMapper.get(keyText);
            case "firstInsureType": return firstInsureTypeMapper.get(keyText);
            default: return null;
        }
    }

    public boolean isNumeric(String str) {
        return StringUtil.isNotEmpty(str) && str.matches(regNumeric);
    }

    @Override
    public Map<String, Integer> getPlateType(String taskid, String inscomcode, String useProps, Integer seat, Double unwrtweight) {
        LogUtil.info("====根据车辆性质判断车牌号类型和颜色：taskid=" + taskid + ",inscomcode=" + inscomcode + ",使用类型：" + useProps + ",座位数：" + seat + ",核定载重量：" + unwrtweight + "====");
        Map<String, Integer> result = new HashMap<>(2);

        if ("1".equals(useProps) || "11".equals(useProps) || "10".equals(useProps) || "2".equals(useProps) || "3".equals(useProps) || "4".equals(useProps)) {
            if (!StringUtil.isEmpty(seat)) {
                if (seat <= 9) {
                    result.put("plateColor", 1);
                    result.put("plateType", 1);//小号牌
                } else if (seat >= 10) {
                    result.put("plateColor", 0);
                    result.put("plateType", 0);//大号牌
                }
            } else {
                result.put("plateColor", 1);
                result.put("plateType", 1);
            }

        } else if ("12".equals(useProps) || "6".equals(useProps) || "15".equals(useProps) || "16".equals(useProps)) {
            INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
            String deptid = dataInsbQuoteinfo.getDeptcode();
            if (!StringUtil.isEmpty(deptid)) {
                INSCDept queryDept = deptService.queryById(deptid);
                String tempOrgCode = queryDept.getDeptinnercode().substring(0, 5);

                if ("02001".equals(tempOrgCode) || "02002".equals(tempOrgCode) || "02006".equals(tempOrgCode) || "02018".equals(tempOrgCode) || "02020".equals(tempOrgCode)
                        || "02024".equals(tempOrgCode) || "02027".equals(tempOrgCode) || "02032".equals(tempOrgCode) || "02033".equals(tempOrgCode)) {
                    if ("12".equals(useProps) || "6".equals(useProps)) {
                        if (unwrtweight < 2000) {
                            result.put("plateColor", 1);
                            result.put("plateType", 1);
                        } else if (unwrtweight >= 2000) {
                            result.put("plateColor", 0);
                            result.put("plateType", 0);
                        }
                    } else if ("15".equals(useProps) || "16".equals(useProps)) {
                        result.put("plateColor", 0);
                        result.put("plateType", 0);
                    }
                } else if ("02009".equals(tempOrgCode) || "02012".equals(tempOrgCode) || "02034".equals(tempOrgCode)) {
                    if (unwrtweight < 5000) {
                        result.put("plateColor", 1);
                        result.put("plateType", 1);
                    } else if (unwrtweight >= 5000) {
                        result.put("plateColor", 0);
                        result.put("plateType", 0);
                    }
                } else {
                    if (unwrtweight < 2000) {
                        result.put("plateColor", 1);
                        result.put("plateType", 1);
                    } else if (unwrtweight >= 2000) {
                        result.put("plateColor", 0);
                        result.put("plateType", 0);
                    }
                }
            } else {
                if (unwrtweight < 2000) {
                    result.put("plateColor", 1);
                    result.put("plateType", 1);
                } else if (unwrtweight >= 2000) {
                    result.put("plateColor", 0);
                    result.put("plateType", 0);
                }
            }
        }

        LogUtil.info("====根据车辆性质判断车牌号类型和颜色：taskid=" + taskid + ",inscomcode=" + inscomcode + ",结果：" + result + "====");
        return result;
    }

    @Override
    public Integer getJgVehicleType(String taskid, String inscomcode, String useProps, Integer seat, Double unwrtweight) {
        LogUtil.info("====根据车辆性质判断交管类型：taskid=" + taskid + ",inscomcode=" + inscomcode + ",使用类型：" + useProps + ",座位数：" + seat + ",核定载重量：" + unwrtweight + "====");

        INSCCode insccode = new INSCCode();
        insccode.setCodetype("VehicleType");

        if ("1".equals(useProps)) {
            if (!StringUtil.isEmpty(seat)) {
                if (seat < 6) {
                    insccode.setCodename("轿车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                } else if (seat >= 6) {
                    insccode.setCodename("小型客车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                }
            } else {
                insccode.setCodename("轿车");
                insccode = inscCodeService.queryOne(insccode);
                return Integer.parseInt(insccode.getCodevalue());
            }
        } else if ("15".equals(useProps) || "16".equals(useProps)) {
            insccode.setCodename("特种车");
            insccode = inscCodeService.queryOne(insccode);
            return Integer.parseInt(insccode.getCodevalue());
        } else if ("2".equals(useProps)) {
            insccode.setCodename("小型客车");
            insccode = inscCodeService.queryOne(insccode);
            return Integer.parseInt(insccode.getCodevalue());
        } else if ("3".equals(useProps)) {
            insccode.setCodename("大型客车");
            insccode = inscCodeService.queryOne(insccode);
            return Integer.parseInt(insccode.getCodevalue());
        } else if ("4".equals(useProps) || "5".equals(useProps) || "10".equals(useProps) || "11".equals(useProps)) {
            if (!StringUtil.isEmpty(seat)) {
                if (seat < 10) {
                    insccode.setCodename("小型客车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                } else if (seat >= 10 && seat < 20) {
                    insccode.setCodename("中型客车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                } else if (seat >= 20) {
                    insccode.setCodename("大型客车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                }
            } else {
                insccode.setCodename("小型客车");
                insccode = inscCodeService.queryOne(insccode);
                return Integer.parseInt(insccode.getCodevalue());
            }
        } else if ("6".equals(useProps) || "12".equals(useProps)) {
            if (!StringUtil.isEmpty(unwrtweight)) {
                if (unwrtweight < 2000) {
                    insccode.setCodename("轻型普通货车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                }
                if (unwrtweight >= 2000 && unwrtweight < 5000) {
                    insccode.setCodename("中型普通货车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                }
                if (unwrtweight >= 5000) {
                    insccode.setCodename("重型普通货车");
                    insccode = inscCodeService.queryOne(insccode);
                    return Integer.parseInt(insccode.getCodevalue());
                }
            } else {
                insccode.setCodename("轻型普通货车");
                insccode = inscCodeService.queryOne(insccode);
                return Integer.parseInt(insccode.getCodevalue());
            }
        }

        return null;
    }

    public Map<String, Date> getNextPolicyDate(String taskid) {
        Map<String, Date> data = new HashMap<>(4);

        INSBRulequeryrepeatinsured insbRulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
        insbRulequeryrepeatinsured.setTaskid(taskid);
        List<INSBRulequeryrepeatinsured> rulequeryrepeatinsuredList = insbRulequeryrepeatinsuredService.queryList(insbRulequeryrepeatinsured);

        if (rulequeryrepeatinsuredList == null || rulequeryrepeatinsuredList.isEmpty()) return data;

        List<INSBRulequeryrepeatinsured> syrulequeryrepeatinsuredList = new ArrayList<>(1);
        List<INSBRulequeryrepeatinsured> jqrulequeryrepeatinsuredList = new ArrayList<>(1);

        for (INSBRulequeryrepeatinsured rulequeryrepeatinsured : rulequeryrepeatinsuredList) {
            if ("0".equals(rulequeryrepeatinsured.getRisktype())) {
                syrulequeryrepeatinsuredList.add(rulequeryrepeatinsured);
            } else if ("1".equals(rulequeryrepeatinsured.getRisktype())) {
                jqrulequeryrepeatinsuredList.add(rulequeryrepeatinsured);
            }
        }

        Date syEffectDate = null, syExpireDate = null,
                jqEffectDate = null, jqExpireDate = null;

        if (!syrulequeryrepeatinsuredList.isEmpty()) {
            insbRulequeryrepeatinsuredService.sortPolicies(syrulequeryrepeatinsuredList);

            INSBRulequeryrepeatinsured last = syrulequeryrepeatinsuredList.get(0);
            Map<String, Date> dates = ModelUtil.calNextPolicyDate(last.getPolicystarttime(), last.getPolicyendtime());
            syEffectDate = dates.get("startDate");
            syExpireDate = dates.get("endDate");
        }
        if (!jqrulequeryrepeatinsuredList.isEmpty()) {
            insbRulequeryrepeatinsuredService.sortPolicies(jqrulequeryrepeatinsuredList);

            INSBRulequeryrepeatinsured last = jqrulequeryrepeatinsuredList.get(0);
            Map<String, Date> dates = ModelUtil.calNextPolicyDate(last.getPolicystarttime(), last.getPolicyendtime());
            jqEffectDate = dates.get("startDate");
            jqExpireDate = dates.get("endDate");
        }

        if (syEffectDate != null) {
            data.put("commEffectDate", syEffectDate);
        }
        if (syExpireDate != null) {
            data.put("commExpireDate", syExpireDate);
        }
        if (jqEffectDate != null) {
            data.put("trafEffectDate", jqEffectDate);
        }
        if (jqExpireDate != null) {
            data.put("trafExpireDate", jqExpireDate);
        }

        return data;
    }
}
