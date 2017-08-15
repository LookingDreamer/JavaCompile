package com.zzb.app.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.common.ConstUtil;
import com.zzb.cm.Interface.entity.car.model.ConfigInfo;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.*;

import com.zzb.conf.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpSender;
import com.common.WorkFlowUtil;
import com.common.redis.CMRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.controller.vo.SupplementInfoVO;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppOtherRequestService;

@Service
@Transactional
public class AppQuotationServiceImpl implements AppQuotationService {
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBCarinfohisDao insbCarinfohisDao;
    @Resource
    private INSBCarmodelinfoDao insbCarmodelinfoDao;
    @Resource
    private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
    @Resource
	private AppInsuredQuoteService appInsuredQuoteService;
    @Resource
	private AppOtherRequestService appOtherRequestService;
    @Resource
    private INSBAgreementService insbAgreementService;
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
    private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
    @Resource
    private INSBOrderDao insbOrderDao;
    @Resource
    private INSBProviderDao insbProviderDao;
    @Resource
    private INSBPersonService insbPersonService;//人员信息表
    @Resource
    private INSBCarowneinfoService insbCarowneinfoService;//车主信息表
    @Resource
    private INSBApplicantService insbApplicantService;//投保人原表
    @Resource
    private INSBApplicanthisService insbApplicanthisService;//投保人历史表
    @Resource
    private INSBInsuredService insbInsuredService;//被保人原表
    @Resource
    private INSBInsuredhisService insbInsuredhisService;//被保人历史表
    @Resource
    private INSBSupplementService supplementService;
    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBRulequeryotherinfoDao rulequeryotherinfoDao;
    @Resource
    private INSBAgentService insbAgentService;
    @Resource
    private INSBChannelService insbChannelService;
    @Resource
    private INSBInvoiceinfoService insbInvoiceinfoService;
    @Resource
    private INSBPrvaccountkeyService insbPrvaccountkeyService;
    
    private static String RULE_SERVER_IP ="";
    private static String RULE_PARAM_HOST = "";
    private static ResourceBundle ruleResourceBundle;
	static {
		// 读取相关的配置  
		ruleResourceBundle = ResourceBundle.getBundle("config/rule");
		RULE_SERVER_IP = ruleResourceBundle.getString("rule.handler.server.ip");
	}

    private ThreadLocal<String> threadFlag = new ThreadLocal<>();
    //商业险理赔次数
    private Map<String, String> commercialClaimTimesMap = null;
    //交强险理赔次数
    private Map<String, String> compulsoryClaimTimesMap = null;


    @PostConstruct
    private void initData() {
        if (commercialClaimTimesMap == null) {
            commercialClaimTimesMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(
                            CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "application.commercialClaimTimes")), Map.class);
        }
        if (compulsoryClaimTimesMap == null) {
            compulsoryClaimTimesMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(
                            CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "application.compulsoryClaimTimes")), Map.class);
        }
    }
    
    /**
     * @param taskId
     * @param inscomcode
     *            保险公司编码
     * @return
     */
    public List<INSBCarkindprice> getCarkindprice(String taskId, String inscomcode) {
        if (StringUtils.isNoneBlank(taskId, inscomcode)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskId", taskId);
            map.put("inscomcode", inscomcode);
            return insbCarkindpriceDao.selectByTaskidAndInscomcode(map);
        }
        return null;
    }

    /**
     * @param taskId
     * @param inscomcode
     * @return
     */
    public INSBCarinfo getCarinfo(String taskId, String inscomcode) {
        INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
        INSBCarinfohis carInfoHis = new INSBCarinfohis();
        carInfoHis.setTaskid(taskId);
        carInfoHis.setInscomcode(inscomcode);
        INSBCarinfohis carInfoHisBean = insbCarinfohisDao.selectOne(carInfoHis);
        String paidtaxes = carInfo.getPaidtaxes();
        if (carInfoHisBean == null) {
        } else {
            carInfo = new INSBCarinfo();
            try {
                PropertyUtils.copyProperties(carInfo, carInfoHisBean);
                if(StringUtil.isNotEmpty(paidtaxes))
                	carInfo.setPaidtaxes(paidtaxes);
            } catch (IllegalAccessException e) {
                LogUtil.error("获取" + taskId + "," + inscomcode + "车辆信息出错：" + e.getMessage());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                LogUtil.error("获取"+taskId+","+inscomcode+"车辆信息出错："+e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                LogUtil.error("获取"+taskId+","+inscomcode+"车辆信息出错："+e.getMessage());
                e.printStackTrace();
            }
        }
        return carInfo;
    }

    public String getCarinfoId(String taskId) {
        String carinfoid = null;
        if (StringUtil.isNotEmpty(taskId)) {
            INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
            if (carInfo != null) {
                carinfoid = carInfo.getId();
            }
        }
        return carinfoid;
    }

    public INSBCarmodelinfo getCarmodelinfo(String carinfoid, String inscomcode) {
        INSBCarmodelinfohis carModelInfoHis = new INSBCarmodelinfohis();
        carModelInfoHis.setCarinfoid(carinfoid);
        carModelInfoHis.setInscomcode(inscomcode);
        INSBCarmodelinfohis carInfoHisBean = insbCarmodelinfohisDao.selectOne(carModelInfoHis);

        INSBCarmodelinfo carModelInfo = null;

        if (carInfoHisBean == null) {
            carModelInfo = insbCarmodelinfoDao.selectByCarinfoId(carinfoid);
        } else {
            carModelInfo = new INSBCarmodelinfo();
            try {
                PropertyUtils.copyProperties(carModelInfo, carInfoHisBean);
            } catch (IllegalAccessException e) {
                LogUtil.error("获取"+carinfoid+","+inscomcode+"车型信息出错："+e.getMessage());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                LogUtil.error("获取" + carinfoid + "," + inscomcode + "车型信息出错：" + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                LogUtil.error("获取" + carinfoid + "," + inscomcode + "车型信息出错：" + e.getMessage());
                e.printStackTrace();
            }
        }
        return carModelInfo;
    }

    /**
     * @param subTaskId
     * @param methodName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public String getQuotationValidOrQuotationInfo(String subTaskId, String taskId, String inscomcode, String methodName) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>(88);

        INSBQuotetotalinfo qti = new INSBQuotetotalinfo();
        qti.setTaskid(taskId);
        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(qti);

        //投保地区
        String quoteTotalInfoId = null;

        if (quotetotalinfo != null) {
            quoteTotalInfoId = quotetotalinfo.getId();
        }

        if (StringUtils.isNotBlank(inscomcode)) {
        	inscomcode = inscomcode.trim();
            INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfo(quoteTotalInfoId, inscomcode);
            paramMap = packetParamdata(paramMap, taskId, inscomcode, quotetotalinfo, quoteinfo);

            if ("geniusRule".equalsIgnoreCase(methodName)) {
                String prvid = inscomcode.length()>4 ? inscomcode.substring(0, 4) : inscomcode;
                try {
                    String affiliationCode = ruleResourceBundle.getString("application.affiliationCode." + prvid);
                    ConfigInfo configInfo = getConfigInfo(inscomcode, quoteinfo.getDeptcode(), "edi");
                    if (configInfo != null && StringUtil.isNotEmpty(configInfo.getConfigMap().get(affiliationCode))) {
                        paramMap.put("application.affiliationCode", configInfo.getConfigMap().get(affiliationCode));
                    } else {
                        LogUtil.warn("taskid=" + taskId + ",prvid=" + prvid + "配置参数没有业务归属代码");
                    }
                } catch (Exception e) {
                    LogUtil.warn("taskid=" + taskId + ",prvid=" + prvid + "获取业务归属代码数据出错：" + e.getMessage());
                }
            }

            LogUtil.info("调用规则方法名 "+methodName+" 规则入参 taskid=" + taskId + ",inscomcode=" + inscomcode + ",map=" + paramMap);
            //HTTP请求接口
            try {
            	RULE_PARAM_HOST = ruleResourceBundle.getString("rule.handler.server."+methodName);
            	if(!RULE_PARAM_HOST.startsWith("http")){
            		RULE_PARAM_HOST = RULE_SERVER_IP + RULE_PARAM_HOST;
            	}
            	LogUtil.info("RULE_PARAM_HOST=="+RULE_PARAM_HOST);

    			String result = HttpSender.doPost(RULE_PARAM_HOST, JSONObject.fromObject(paramMap).toString());
    			LogUtil.info("规则出参 taskid=" + taskId + ",inscomcode=" + inscomcode + ",ruleResult=" + result);

    			return result;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        return null;
    }

    public Map<String, Object> packetParamdata(Map<String, Object> paramMap, String taskId, String inscomcode, INSBQuotetotalinfo quotetotalinfo, INSBQuoteinfo quoteinfo){
    	INSBCarmodelinfo carModelInfo = null;
        String carinfoid = getCarinfoId(taskId);
        if (StringUtil.isNotEmpty(carinfoid)) {
            carModelInfo = getCarmodelinfo(carinfoid, inscomcode);
        }
        //投保地区
        String insurancearea = null;
        String agentNum = null;
        if (quotetotalinfo != null) {
            insurancearea = quotetotalinfo.getInscitycode();
            agentNum = quotetotalinfo.getAgentnum();
        }
        String agreementtrule = null;
        if (StringUtils.isNotBlank(quoteinfo.getAgreementid())) {
            agreementtrule = insbAgreementService.queryById(quoteinfo.getAgreementid()).getAgreementtrule();
            if (StringUtils.isNotBlank(agreementtrule)) {
                paramMap.putAll(getRuleInfoMap(agreementtrule));
            }
        }
        paramMap.putAll(getInsureInfoMap(insurancearea, inscomcode, taskId));
        paramMap.putAll(getRiskInfoMap(getCarkindprice(taskId, inscomcode)));
        // 取得车辆信息
        INSBCarinfo carInfo = getCarinfo(taskId, inscomcode);
        if (carInfo != null) {
            paramMap.putAll(getCarInfoMap(carInfo, carModelInfo));
        }
        paramMap.putAll(getPolicyitem(taskId, inscomcode));

        // 投保信息补充表
        List<INSBSupplement> supplementList = supplementService.getSupplementsBytaskid(taskId, inscomcode);
        if (supplementList != null && !supplementList.isEmpty()) {
            for (INSBSupplement supplement : supplementList) {
            	Map<String, String> redisMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, supplement.getKeyid())), Map.class);//默认从redis里面获取补充项的取值Map
                if (StringUtil.isNotEmpty(supplement.getMetadataValueKey())) {
                    paramMap.put(supplement.getKeyid(), supplement.getMetadataValueKey());
                }
                if(null != redisMap&&!(redisMap.values().contains(supplement.getMetadataValueKey()))){
                	LogUtil.info(taskId+","+inscomcode+"值"+supplement.getMetadataValueKey()+"不存在值范围内："+redisMap);
                	paramMap.remove(supplement.getKeyid());
                }
            }
        }

        //无赔款不浮动原因
        INSBRulequeryotherinfo rulequeryotherinfo = new INSBRulequeryotherinfo();
        rulequeryotherinfo.setTaskid(taskId);
        List<INSBRulequeryotherinfo> rulequeryotherinfoList = rulequeryotherinfoDao.selectList(rulequeryotherinfo);
        if (rulequeryotherinfoList != null && !rulequeryotherinfoList.isEmpty()) {
            String loyaltyReasons = getnoRenewalDiscountReason(rulequeryotherinfoList.get(0).getLoyaltyreasons());
            LogUtil.info(taskId+","+inscomcode+"无赔款不浮动原因："+loyaltyReasons);
            if (StringUtil.isNotEmpty(loyaltyReasons)) {
                paramMap.put("ruleItem.noRenewalDiscountReason", loyaltyReasons);
            }
            //是否纯电销
            if(StringUtil.isNotEmpty(rulequeryotherinfoList.get(0).getPureesale())){
            	paramMap.put("application.saleChannelType", rulequeryotherinfoList.get(0).getPureesale());
            }
        }
        //发票类型
        INSBInvoiceinfo invoiceinfo = insbInvoiceinfoService.selectByTaskidAndComcode(taskId, inscomcode);
        if (invoiceinfo != null) {
            paramMap.put("ruleItem.invoiceType", invoiceinfo.getInvoicetype());//发票信息 0普通发票 1增值税发票
        }
        
        //车主
        INSBPerson person = getCarowneinfo(taskId, inscomcode);
        String ownerCard = "";
        if (person != null) {
            paramMap.put("car.owner.name", person.getName());
            paramMap.put("car.owner.certificate", getCertKinds(person.getIdcardtype()));
            paramMap.put("car.owner.age", getAge(person.getIdcardno(),person.getIdcardtype()));
            paramMap.put("car.owner.censusRegister",getCensusRegister(person.getIdcardno(),person.getIdcardtype()));
            ownerCard = person.getIdcardno();
        }
        
        //被保人
        person = getInsuredPersonInfo(taskId, inscomcode);
        String insuredCard = "";
        if (person != null) {
            paramMap.put("insured.name", person.getName());
            paramMap.put("insured.certificate", getCertKinds(person.getIdcardtype()));
            paramMap.put("insured.age", getAge(person.getIdcardno(),person.getIdcardtype()));
            paramMap.put("insured.censusRegister",getCensusRegister(person.getIdcardno(),person.getIdcardtype()));
            insuredCard = person.getIdcardno();
        }
        if(null!=ownerCard && ownerCard.equals(insuredCard)){
        	paramMap.put("ruleItem.ownerInsuredIsSame", true);//被保人车主
        }
        //投保人
        person = getApplicantInfo(taskId, inscomcode);
        String proposerCard = "";
        if (person != null) {
            paramMap.put("proposer.name", person.getName());
            paramMap.put("proposer.certificate", getCertKinds(person.getIdcardtype()));
            paramMap.put("proposer.censusRegister", getCensusRegister(person.getIdcardno(),person.getIdcardtype()));
            proposerCard = person.getIdcardno();
        }
        if(null!=ownerCard && ownerCard.equals(proposerCard)){
        	paramMap.put("ruleItem.ownerApplicantIsSame", true);//投保人车主
        }
        if(null!=insuredCard && insuredCard.equals(proposerCard)){
        	paramMap.put("ruleItem.insuredApplicantIsSame", true);//被保人投保人
        }
       /* 规则信息.证件号码相同 ruleItem.cardNumberIsSame Boolean
                                   规则信息.属于渠道业务 ruleItem.isChannelTask Boolean
                                   规则信息.渠道名称 ruleItem.channelName String*/
        paramMap.put("ruleItem.cardNumberIsSame", false);
        paramMap.put("ruleItem.isChannelTask", false);
        paramMap.put("ruleItem.channelName", "");
        
        INSBAgent agent = insbAgentService.getAgent(agentNum);
        if(null != agent){
        	if("3".equals(String.valueOf(agent.getAgentkind()))){//渠道类型的用户
            	paramMap.put("ruleItem.isChannelTask", true);
            }
        	if(null!=agent.getChannelid()){
        		INSBChannel channel = insbChannelService.queryById(agent.getChannelid());
        		if(null != channel){
                	paramMap.put("ruleItem.channelName", channel.getChannelname());
                }
        	}
        }
        //代理人工号
        if (agentNum != null) {
            paramMap.put("ruleItem.agentID", agentNum);
        }

        paramMap.put("api.log.enquiry.id", taskId + "_" + inscomcode);// 任务ID
        return paramMap;
    }
    
    private String getnoRenewalDiscountReason(String loyaltyReasons) {
        if (StringUtil.isEmpty(loyaltyReasons)) return null;
        if (loyaltyReasons.contains("没有上年度保单")) return "1";
        if (loyaltyReasons.contains("脱保6个月以上")) return "2";
        if (loyaltyReasons.contains("脱保3-6个月")) return "3";
        if (loyaltyReasons.contains("上张保单做过批改过户")) return "4";
        if (loyaltyReasons.contains("过户车")) return "5";
        if (loyaltyReasons.contains("上张保单为短期单")) return "6";
        return null;
    }

    public Integer getCertKinds(Integer idCardType) {
        if (idCardType == null) return 99;

        switch (idCardType) {
            case 0: return 1;
            case 6: return 21;
            case 8: return 22;
            default:return 99;
        }
    }

    public int getAge(String certificateNum,Integer idCardType) {
        if (StringUtil.isEmpty(certificateNum) && StringUtil.isEmpty(idCardType)) return 0;
        if(idCardType!=0) return 0;
        int len = certificateNum.length();
        if (len != 15 && len != 18) return 0;

        //转为18位
        if (len == 15) {
            String birthday = certificateNum.substring(6, 12);

            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (Exception e) {
                LogUtil.error("解析" + certificateNum + "年月日出错：" + e.getMessage());
                return 0;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthdate);

            String year = String.valueOf(calendar.get(Calendar.YEAR));
            certificateNum = certificateNum.substring(0, 6) + year + certificateNum.substring(8);
        }

        String birthday = certificateNum.substring(6, 14);

        Date birthdate = null;
        try {
            birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
        } catch (Exception e) {
            LogUtil.error("解析" + certificateNum + "年月日出错：" + e.getMessage());
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthdate);
        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }

        return age;
    }

    public INSBPerson getApplicantInfo(String taskId, String inscomcode) {
        INSBApplicanthis applicanthis = new INSBApplicanthis();
        applicanthis.setTaskid(taskId);
        applicanthis.setInscomcode(inscomcode);
        List<INSBApplicanthis> applicanthisList = insbApplicanthisService.queryList(applicanthis);

        INSBPerson insbPerson = null;

        if (applicanthisList != null && applicanthisList.size() > 0 && StringUtil.isNotEmpty(applicanthisList.get(0).getPersonid())) {
            insbPerson = insbPersonService.queryById(applicanthisList.get(0).getPersonid());
        } else {
            INSBApplicant applicant = new INSBApplicant();
            applicant.setTaskid(taskId);
            List<INSBApplicant> applicantList = insbApplicantService.queryList(applicant);

            if (applicantList != null && applicantList.size() > 0 && StringUtil.isNotEmpty(applicantList.get(0).getPersonid())) {
                insbPerson = insbPersonService.queryById(applicantList.get(0).getPersonid());
            }
        }

        return insbPerson;
    }

    public INSBPerson getInsuredPersonInfo(String taskId, String inscomcode) {
        INSBInsuredhis insuredhis = new INSBInsuredhis();
        insuredhis.setTaskid(taskId);
        insuredhis.setInscomcode(inscomcode);
        List<INSBInsuredhis> insuredhisList = insbInsuredhisService.queryList(insuredhis);

        INSBPerson insbPerson = null;

        if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
            insbPerson = insbPersonService.queryById(insuredhisList.get(0).getPersonid());
        } else {
            INSBInsured insbInsured = new INSBInsured();
            insbInsured.setTaskid(taskId);
            List<INSBInsured> insuredList = insbInsuredService.queryList(insbInsured);

            if (insuredList != null && insuredList.size() > 0 && StringUtil.isNotEmpty(insuredList.get(0).getPersonid())) {
                insbPerson = insbPersonService.queryById(insuredList.get(0).getPersonid());
            }
        }

        return insbPerson;
    }

    public INSBPerson getCarowneinfo(String taskId, String inscomcode) {
        INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
        carowneinfo.setTaskid(taskId);
        List<INSBCarowneinfo> carowneinfoList = insbCarowneinfoService.queryList(carowneinfo);

        INSBPerson insbPerson = null;

        if (carowneinfoList != null && carowneinfoList.size() > 0 && StringUtil.isNotEmpty(carowneinfoList.get(0).getPersonid())) {
            insbPerson = insbPersonService.queryById(carowneinfoList.get(0).getPersonid());
        }

        return insbPerson;
    }

    /**
     * 获得保单信息
     *
     * @param taskId
     * @param inscomcode
     * @return
     */
    private Map<String, Object> getPolicyitem(String taskId, String inscomcode) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskid", taskId);
        params.put("inscomcode", inscomcode);
        List<INSBPolicyitem> list = insbPolicyitemDao.getListByMap(params);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String businessstartdatestr = "";
        String startdatestr = "";

        if (list != null && !list.isEmpty()) {
            for (INSBPolicyitem item : list) {
                if (item != null && item.getRisktype().trim().equals("0")) {
                    Date businessstartdate = item.getStartdate();
                    if (businessstartdate != null) {
                        businessstartdatestr = df.format(businessstartdate);
                        map.put("application.commercialEffectiveDate", df.format(businessstartdate));// 投保信息.起保日期
                    }
                    Date businessenddate = item.getEnddate();
                    if (businessenddate != null)
                        map.put("application.commercialExpiryDate", df.format(businessenddate));// 投保信息.商业险终止日期
                } else if (item != null && item.getRisktype().trim().equals("1")) {
                    Date startdate = item.getStartdate();
                    if (startdate != null) {
                        startdatestr = df.format(startdate);
                        map.put("application.compulsoryEffectiveDate", df.format(startdate));// 投保信息.交强险起保日期
                    }
                    Date enddate = item.getEnddate();
                    if (enddate != null)
                        map.put("application.compulsoryExpiryDate", df.format(enddate));// 投保信息.交强险终止日期
                }
            }
        }
        // 商业险交强险是否一致
        if (!startdatestr.equals("") && !businessstartdatestr.equals("")) {
            if (businessstartdatestr.equals(startdatestr)) {
                map.put("ruleItem.isSameEffectiveDate", true);
            } else {
                map.put("ruleItem.isSameEffectiveDate", false);
            }
        } else {
            map.put("ruleItem.isSameEffectiveDate", false);
        }
        return map;
    }

    /**
     * 获得投保信息
     *
     * @param insurancearea
     * @param inscomcode
     * @return
     */
    public Map<String, Object> getInsureInfoMap(String insurancearea, String inscomcode, String taskid) {
        INSBLastyearinsureinfo lastyear = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);

        Map<String, Object> map = new HashMap<String, Object>(13);
        if (StringUtils.isBlank(insurancearea)) {
            insurancearea = "0";
        }
        map.put("application.insureArea", Long.valueOf(insurancearea.trim()));// 投保信息.地区编号

        if (StringUtils.isNotBlank(inscomcode)) {
            map.put("application.commercialInsureCo", inscomcode.trim());// 投保信息.保险公司编号
        }
        if (lastyear != null) {
            String commercialClaimTimes = null;
            if(!StringUtil.isEmpty(lastyear.getBwcommercialclaimtimes())){
                commercialClaimTimes = commercialClaimTimesMap.get(lastyear.getBwcommercialclaimtimes());
            }
            map.put("application.commercialClaimTimes", StringUtil.isEmpty(commercialClaimTimes) ? 0 : Integer.parseInt(commercialClaimTimes));// 投保信息.上年商业理赔次数
            map.put("application.commercialClaimRate", lastyear.getSyclaimrate() == null ? 0.0 : lastyear.getSyclaimrate());// 投保信息.上年商业赔付率
            map.put("application.lastCommercialClaimSum",lastyear.getSylastclaimsum() == null ? 0.0 : lastyear.getSylastclaimsum());// 投保信息.商业险理赔金额
            map.put("application.firstInsureType", Integer.valueOf(StringUtil.isEmpty(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(lastyear.getFirstinsuretype())) ? 
            		"0" : INSBLastyearinsureinfo.convertFirstInsureTypeToCm(lastyear.getFirstinsuretype())));// 投保信息.投保类型
            /**
             * 连续三年没有理赔:-2,连续两年没有理赔:-1,上年没有理赔:0,
             * 新保或上年发生一次有责任不涉及死亡理赔:1,上年有两次及以上理赔:2,
             * 上年有涉及死亡理赔:3
             */
            String compulsoryclaimtimes = null;
            if(!StringUtil.isEmpty(lastyear.getBwcompulsoryclaimtimes())){
                compulsoryclaimtimes = compulsoryClaimTimesMap.get(lastyear.getBwcompulsoryclaimtimes());
            }
			map.put("application.compulsoryClaimTimes", StringUtil.isEmpty(compulsoryclaimtimes) ? 0 : Integer.parseInt(compulsoryclaimtimes));// 投保信息.上年交强理赔次数
            map.put("application.compulsoryClaimRate", lastyear.getJqclaimrate() == null ? 0.0 : lastyear.getJqclaimrate());// 投保信息.上年交强赔付率
            map.put("application.lastCompulsoryClaimSum", lastyear.getJqlastclaimsum() == null ? 0.0 : lastyear.getJqlastclaimsum());// 投保信息.交强险理赔金额

            String preinscode = lastyear.getSupplierid();
            String preinsname = lastyear.getSuppliername();
            if (StringUtils.isNotBlank(preinscode)) {
                map.put("application.lastInsureCo", Long.valueOf(preinscode.trim()));// 投保信息.上年承保公司
            }else if(StringUtils.isNotBlank(preinsname)){
                map.put("application.lastInsureCo", this.findLastSupplierId(preinsname));// 投保信息.上年承保公司
            }

            if (!StringUtil.isEmpty(lastyear.getSypolicyno())) {
                map.put("application.lastCommercialPoliceyNum", lastyear.getSypolicyno().trim());// 投保信息.上年商业险保单号
            }
            // map.put("application.lastCommercialPremium",Double.valueOf(lastYearPolicyBean.getSyprem()));//
            // 投保信息.上年商业险保费
            if (!StringUtil.isEmpty(lastyear.getJqpolicyno())) {
                map.put("application.lastComulsoryPoliceyNum", lastyear.getJqpolicyno().trim());// 投保信息.上年交强险保单号
            }
        }
        return map;
    }

    private String findLastSupplierId(String prvname) {
        try {
            if(!StringUtil.isEmpty(prvname)){
                INSBProvider insbp = new INSBProvider();
                insbp.setPrvname(prvname);
                List<INSBProvider> list = insbProviderDao.selectList(insbp);
                if(list!=null&&!list.isEmpty()){
                    return list.get(0).getId();
                }
                return null;
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
    /**
     * 车辆使用性质
     *
     * @param carproperty
     * @return
     */
    private String getCarproperty(String carproperty) {
        String value = "1";
        if (StringUtil.isNotEmpty(carproperty)) {
            switch (carproperty) {
                case "1":
                    value = "1";
                    break;// 家庭自用汽车
                case "10":
                    value = "220";
                    break;// 企业非营业客车
                case "11":
                    value = "230";
                    break;// 机关非营业客车
                case "12":
                    value = "240";
                    break;// 非营业货车
                case "15":
                    value = "301";
                    break;// 营业特种车
                case "16":
                    value = "302";
                    break;// 非营业特种车
                case "2":
                    value = "3";
                    break;// 出租租赁营业客车
                case "3":
                    value = "4";
                    break;// 城市公交营业客车
                case "4":
                    value = "5";
                    break;// 公路客运营业客车
                case "6":
                    value = "7";
                    break;// 营业货车
                default:
                    value = "1";
                    break;
            }
        }
        return value;
    }

    //http://pm.baoxian.in/zentao/task-view-164.html
    private static final List<String> ruleCalculationUseProps = new ArrayList<>(4);
    private static final List<String> ruleCalculationVehicleTypes = new ArrayList<>(10);
    static {
        ruleCalculationUseProps.add("6");
        ruleCalculationUseProps.add("12");
        ruleCalculationUseProps.add("15");
        ruleCalculationUseProps.add("16");

        ruleCalculationVehicleTypes.add("18");
        ruleCalculationVehicleTypes.add("19");
        ruleCalculationVehicleTypes.add("20");
        ruleCalculationVehicleTypes.add("21");
        ruleCalculationVehicleTypes.add("22");
        ruleCalculationVehicleTypes.add("40");
        ruleCalculationVehicleTypes.add("41");
        ruleCalculationVehicleTypes.add("42");
        ruleCalculationVehicleTypes.add("43");
        ruleCalculationVehicleTypes.add("44");
    }

    /**
     * 获得车辆信息
     *
     * @param carInfo
     * @param carModelInfo
     * @return
     */
    public Map<String, Object> getCarInfoMap(INSBCarinfo carInfo, INSBCarmodelinfo carModelInfo) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<String, Object>(30);

        map.put("car.specific.useProps", Long.valueOf(getCarproperty(carInfo.getCarproperty())));// 车辆信息.车辆使用性质

        if (ruleCalculationUseProps.contains(carInfo.getCarproperty())) {
            String vt = commonQuoteinfoService.getVehicleType(carInfo, carModelInfo);
            if (ruleCalculationVehicleTypes.contains(vt)) {
                map.put("car.model.styleId", vt);// 车辆信息.车辆种类
            }
        }

        Date registdate = carInfo.getRegistdate();
        if (registdate != null) {
            map.put("car.specific.regDate", df.format(registdate));// 车辆信息.车辆初登日期
        }

        if("新车未上牌".equals(carInfo.getCarlicenseno())){
            map.put("car.specific.isNewCar", true);//是否 新车未上牌
        }else{
            map.put("car.specific.isNewCar", false);//是否 新车未上牌
        }

        String isTransfercar = carInfo.getIsTransfercar();
        if (isTransfercar != null && "1".equals(isTransfercar)) {
            map.put("car.specific.isTransferCar", Boolean.TRUE);// 车辆信息.是过户车
        } else {
            map.put("car.specific.isTransferCar", Boolean.FALSE);// 车辆信息.是过户车
        }

        String carLicenseNo = carInfo.getCarlicenseno();
        if (StringUtils.isNotBlank(carLicenseNo)) {
            if (!"新车未上牌".equals(carLicenseNo)) {
                map.put("car.specific.license", carLicenseNo.trim());// 车辆信息.车辆号牌
            } else {
                map.put("application.firstInsureType", Integer.valueOf("1"));// 投保信息.投保类型
            }
        }

        String property = carInfo.getProperty();
        if (StringUtils.isBlank(property)) {
            property = "0";
        }
        map.put("car.specific.userType", Integer.valueOf(Integer.parseInt(property.trim()) + 1));// 车辆信息.车辆用户类型
        
        if ("1".equals(carInfo.getPaidtaxes())) {// 是否完税，已完税传给规则1，
        	 map.put("car.specific.alreadyTaxed", Boolean.TRUE);// 是否完税
        }else{
        	map.put("car.specific.alreadyTaxed", Boolean.FALSE);// 未完税
        }
        Integer carVehicularApplications = carInfo.getCarVehicularApplications();
        if (carVehicularApplications == null) {
            carVehicularApplications = Integer.valueOf(0);
        }
        map.put("car.specific.vehicularApplications", carVehicularApplications);// 车辆信息.车辆用途

        String specifydriver = carInfo.getSpecifydriver();
        Boolean isSpecifyDriver = Boolean.FALSE;
        if (specifydriver != null && specifydriver.trim().equals("1")) {//
            isSpecifyDriver = Boolean.TRUE;
        }
        map.put("application.isSpecifyDriver", isSpecifyDriver);// 投保信息.指定驾驶人

        String mileage = carInfo.getMileage();
        if (StringUtils.isNotBlank(mileage)) {
            switch (mileage) {
                case "0":
                    mileage = "20000";
                    break;
                case "1":
                    mileage = "40000";
                    break;
                case "2":
                    mileage = "60000";
                    break;
            }
            map.put("car.specific.avgMileage", Long.valueOf(mileage));// 投保信息.平均行驶里程
        }

        String drivingarea = carInfo.getDrivingarea();
        if (StringUtils.isNotBlank(drivingarea)) {
            map.put("car.specific.drivingArea", Integer.parseInt(drivingarea));// 投保信息.约定行驶区域
        }

        Double taxprice = Double.valueOf(0.0);
        Double displacement = Double.valueOf(0);
        Integer seat = Integer.valueOf(0);
        Double unwrtweight = Double.valueOf(0.0);
        Double fullweight = Double.valueOf(0);
        Integer carVehicleOrigin = Integer.valueOf(1);
        Double analogyprice = Double.valueOf(0.0);
        Double analogytaxprice = Double.valueOf(0.0);
        Double price = Double.valueOf(0.0);
        String familyname = "全部";
        String carprice = "0";

        if (carModelInfo != null) {
            if (carModelInfo.getTaxprice() != null) {
                taxprice = carModelInfo.getTaxprice();
            }
            if (carModelInfo.getDisplacement() != null) {
                displacement = carModelInfo.getDisplacement();
            }
            if (carModelInfo.getSeat() != null) {
                seat = carModelInfo.getSeat();
            }
            if (carModelInfo.getUnwrtweight() != null) {
                unwrtweight = Double.valueOf(carModelInfo.getUnwrtweight().doubleValue() / 1000.0);
            }
            if (carModelInfo.getFullweight() != null) {
                fullweight = carModelInfo.getFullweight();
            }
            if (carModelInfo.getCarVehicleOrigin() != null) {
                carVehicleOrigin = carModelInfo.getCarVehicleOrigin() + 1;
            }
            if (carModelInfo.getAnalogyprice() != null) {
                analogyprice = carModelInfo.getAnalogyprice();
            }
            if (carModelInfo.getAnalogytaxprice() != null) {
                analogytaxprice = carModelInfo.getAnalogytaxprice();
            }
            if (carModelInfo.getPrice() != null) {
                price = carModelInfo.getPrice();
            }
            if (StringUtils.isNotBlank(carModelInfo.getFamilyname())) {
                familyname = carModelInfo.getFamilyname().trim();
            }
            if (StringUtils.isNotBlank(carModelInfo.getCarprice())) {
                carprice = carModelInfo.getCarprice();
            }
            if ("2".equals(carprice)) {
                map.put("car.specific.floatPrice", carModelInfo.getPolicycarprice());// 车辆信息.车辆指定价
            }

            String brandName = carModelInfo.getBrandname();
            if (StringUtils.isNotBlank(brandName)) {
                map.put("car.model.brandName", brandName.trim());// 车辆信息.车型品牌名称
            }

            String standardfullname = carModelInfo.getStandardfullname();
            if (StringUtils.isNotBlank(standardfullname)) {
                map.put("car.model.modelName", standardfullname.trim());// 车辆信息.车型名称
            }
        }

        map.put("car.specific.taxPrice", taxprice);// 车辆信息.新车购置价

        // map.put("car.specific.floatPrice", Double.valueOf(0.0));// 车辆信息.车辆浮动价

        // Integer glassType = carModelInfo.getGlassType();
        // if (glassType == null) {
        // glassType = Integer.valueOf(1);
        // }
        // map.put("car.specific.glassType", glassType);// 车辆信息.玻璃类型

        map.put("car.model.displacement", displacement);// 车辆信息.排气量

        map.put("car.model.seats", seat);// 车辆信息.座位数

        map.put("car.model.modelLoad", unwrtweight);// 车辆信息.载重量

        map.put("car.model.familyName", familyname);// 车辆信息.车型系列名称

        map.put("car.model.fullLoad", fullweight);// 车辆信息.车身自重

        // map.put("car.model.isChangeKind", Boolean.FALSE);// 车辆信息.营业转非营业

        map.put("car.model.vehicleOrigin", carVehicleOrigin);// 车辆信息.车辆产地类型

        map.put("car.specific.analogyPrice", analogyprice);// 车辆信息.不含税类比价

        map.put("car.specific.analogyTaxPrice", analogytaxprice);// 车辆信息.含税类比价

        map.put("car.specific.price", price);// 车辆信息.不含税价

        // map.put("car.specific.specialVehicleType", "");// 车辆信息.特种车类型

        map.put("ruleItem.carPriceType", "不含税价");// 车辆信息.车辆价格类型

        map.put("application.priceProvideType", Integer.valueOf(carprice));// 投保信息.车价提供方式
        // 最低:0,最高:1,指定:2

        return map;
    }

    /**
     * 获得规则信息
     *
     * @return
     */
    public Map<String, Object> getRuleInfoMap(String agreementtrule) {
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("ruleItem.ruleID", agreementtrule);// 规则信息.规则集编号
        map.put("ruleItem.repairDiscount", Double.valueOf(0));// 规则信息.专修保障费率
        // map.put("ruleItem.specialCarDiscount", Double.valueOf(1));//
        // 规则信息.特殊车型系数

        return map;
    }

    public static void main(String[] args) {
        String json = "{\"UNIT\":\"次\",\"VALUE\":\"5\",\"KEY\":\"5\"}";
        JSONObject j = JSONObject.fromObject(json);
        System.out.println(j.get("VALUE"));
        // String str = "备注了吧";
        // JSONObject j = JSONObject.fromObject(str);
        // System.out.println(j);
    }

    // 风险类型 1 √
    // 车辆归属区域 2 √
    // 交通违规次数 3 √
    // 交通违规系数 4 √
    // 使用车队优惠 5 √
    // 驾驶员年龄 6 √
    // 驾驶员驾龄 7 √
    // 驾驶员性别 8 √
    // 车辆浮动价 9 √
    // 营业转非营业 10 √
    // 车辆产地类型 11 √
    public Map<String, Object> getSupplementaryinfo(
            List<INSBSupplementaryinfo> taryinfolist) {
        Map<String, Object> map = new HashMap<String, Object>();
        String value = null;
        for (INSBSupplementaryinfo supple : taryinfolist) {
            if (supple.getInputtype().equals("1")) {
                value = supple.getOptional();
            } else {
                JSONObject info = JSONObject.fromObject(supple.getOptional());
                value = (String) info.get("VALUE");
            }

            if (supple.getItemtype().equals("1")) {
                map.put("application.kindOfRisk", value);// 投保信息.风险类别
            } else if (supple.getItemtype().equals("2")) {
                if (StringUtils.isBlank(value)) {
                    value = "0";
                }
                map.put("application.registerArea", value);// 投保信息.车辆归属区域
            } else if (supple.getItemtype().equals("3")) {
                map.put("application.trafficOffence", value);// 投保信息.交通违规次数
            } else if (supple.getItemtype().equals("4")) {
                map.put("application.trafficOffenceDiscount", value);// 投保信息.交通违规系数
            } else if (supple.getItemtype().equals("5")) {
                map.put("application.useMotorcadeMode", value);// 投保息.使用车队优惠
            } else if (supple.getItemtype().equals("6")) {
                map.put("driver.age", value);// 投保信息.驾驶员年龄
            } else if (supple.getItemtype().equals("7")) {
                map.put("driver.drivingAge", value);// 投保信息.驾驶员驾龄
            } else if (supple.getItemtype().equals("8")) {
                map.put("driver.gender", value);// 投保信息.驾驶员性别
            } else if (supple.getItemtype().equals("9")) {
                if (StringUtils.isBlank(value)) {
                    value = "0.0";
                }
                // map.put("car.specific.floatPrice", Double.valueOf(value));//
                // 车辆信息.车辆浮动价
            } else if (supple.getItemtype().equals("10")) {
                map.put("car.model.isChangeKind", value);// 车辆信息.营业转非营业
            } /*
			 * else if (supple.getItemtype().equals("11")) { if
			 * (StringUtils.isBlank(value)) { value = "1"; }
			 * map.put("car.model.vehicleOrigin", value);// 车辆信息.车辆产地类型 }
			 */

        }
        return map;
    }

    /**
     * 获得补充数据项
     *
     * @param replenishInfo
     * @return
     */
    public Map<String, Object> getSupplementIteminfo(
            SupplementInfoVO replenishInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (replenishInfo != null) {
            Long specialVehicleType = replenishInfo.getSpecialVehicleType();
            if (specialVehicleType == null) {
                specialVehicleType = Long.valueOf(1L);
            }
            map.put("car.specific.specialVehicleType", specialVehicleType);// 车辆信息.特种车类型

            Integer registerArea = replenishInfo.getRegisterArea();
            if (registerArea == null) {
                registerArea = Integer.valueOf(0);
            }
            map.put("application.registerArea", registerArea);// 投保信息.车辆归属区域

            Double customDiscount = replenishInfo.getCustomDiscount();
            if (customDiscount != null) {
                map.put("ruleItem.customDiscount", customDiscount);// 规则信息.自定义折扣

            }
            Boolean useMotorcadeMode = replenishInfo.getUseMotorcadeMode();
            if (useMotorcadeMode != null) {
                map.put("application.useMotorcadeMode", useMotorcadeMode);// 投保信息.车辆归属区域
            }

            // Integer vehicularApplications = replenishInfo
            // .getVehicularApplications();
            // if (vehicularApplications == null) {
            // vehicularApplications = Integer.valueOf(0);
            // }
            // map.put("car.specific.vehicularApplications",
            // vehicularApplications);// 车辆信息.车辆用途

            String fullLoadCalculationType = replenishInfo
                    .getFullLoadCalculationType();
            if (StringUtils.isBlank(fullLoadCalculationType)) {
                fullLoadCalculationType = "默认";
            }
            map.put("ruleItem.fullLoadCalculationType",
                    fullLoadCalculationType.trim());// 规则信息.自重计算标准

        }
        return map;
    }

    /**
     * 获得保障项目
     *
     * @param carkindpriceList
     * @return
     */
    public Map<String, Object> getRiskInfoMap(List<INSBCarkindprice> carkindpriceList) {
        Map<String, Object> map = new HashMap<String, Object>(33);
        if (carkindpriceList != null && carkindpriceList.size() > 0 && !carkindpriceList.isEmpty()) {
            boolean commercial = Boolean.FALSE;
            boolean traffic = Boolean.FALSE;
            for (INSBCarkindprice insbCarkindprice : carkindpriceList) {
                String inskindcode = insbCarkindprice.getInskindcode();// 险别编码
                if (StringUtils.isNotBlank(inskindcode)) {
                    Double amount = insbCarkindprice.getAmount();// 保额
                    amount = (amount == null) ? Double.valueOf(0.0) : amount;
                    String notdeductible = insbCarkindprice.getNotdeductible();// 不计免赔
                    Boolean isInsured = Boolean.FALSE;
                    if (notdeductible != null && notdeductible.trim().equals("1"))
                        isInsured = Boolean.TRUE;

                    if (inskindcode.trim().equals("ThirdPartyIns")) {// 第三者责任险
                        map.put("insureItem.thirdPartyIns.isInsured", Boolean.TRUE);// 保障项目.投保第三者责任险
                        map.put("insureItem.thirdPartyIns.coverage", amount);// 保障项目.第三者责任险保额
                        map.put("insureItem.ncfThirdPartyIns.isInsured", isInsured);// 保障项目.投保三者不计免赔
                    } else if (inskindcode.trim().equals("VehicleDemageIns")) {// 车辆损失险
                        map.put("insureItem.vehicleDemageIns.isInsured", Boolean.TRUE);// 保障项目.投保车辆损失险
                        map.put("insureItem.vehicleDemageIns.coverage", amount);// 保障项目.车辆损失险保额
                        map.put("insureItem.ncfVehicleDemageIns.isInsured", isInsured);// 保障项目.投保车损不计免赔
                    } else if (inskindcode.trim().equals("TheftIns")) {// 全车盗抢险
                        map.put("insureItem.theftIns.isInsured", Boolean.TRUE);// 保障项目.投保盗抢险

                        if (insbCarkindprice.getSelecteditem() != null && insbCarkindprice.getSelecteditem().contains("按折旧价投保")) {
                            amount = 0.0;
                        }
                        map.put("insureItem.theftIns.coverage", amount);// 保障项目.盗抢险保额
                        map.put("insureItem.ncfTheftIns.isInsured", isInsured);// 保障项目.投保盗抢不计免赔
                    } else if (inskindcode.trim().equals("CombustionIns")) {// 自燃损失险
                        map.put("insureItem.combustionIns.isInsured", Boolean.TRUE);// 保障项目.投保自燃损失险

                        if (insbCarkindprice.getSelecteditem() != null && insbCarkindprice.getSelecteditem().contains("按折旧价投保")) {
                            amount = 0.0;
                        }
                        map.put("insureItem.combustionIns.coverage", amount);// 保障项目.自燃损失险保额
                        map.put("insureItem.ncfCombustionIns.isInsured", isInsured);// 保障项目.投保自燃不计免赔
                    } else if (inskindcode.trim().equals("DriverIns")) {// 司机责任险
                        map.put("insureItem.driverIns.isInsured", Boolean.TRUE);// 保障项目.投保驾驶员责任险
                        map.put("insureItem.driverIns.coverage", amount);// 保障项目.驾驶员责任险保额
                        map.put("insureItem.ncfDriverIns.isInsured", isInsured);// 保障项目.投保驾驶员不计免赔
                    } else if (inskindcode.trim().equals("GoodsOnVehicleIns")) {// 车上货物责任险
                        map.put("insureItem.goodsOnTruckIns.isInsured", Boolean.TRUE);// 保障项目.投保车上货物责任险
                        map.put("insureItem.goodsOnTruckIns.coverage", amount);// 保障项目.车上货物责任险保额
                        map.put("insureItem.ncfGoodsOnTruckIns.isInsured", isInsured);// 保障项目.投保车上货物不计免赔
                    } else if (inskindcode.trim().equals("PassengerIns")) {// 乘客责任险
                        map.put("insureItem.passengerIns.isInsured", Boolean.TRUE);// 保障项目.投保乘客责任险
                        map.put("insureItem.passengerIns.coverage", amount);// 保障项目.乘客责任险保额
                        map.put("insureItem.ncfPassengerIns.isInsured", isInsured);// 保障项目.投保乘客不计免赔
                    } else if (inskindcode.trim().equals("ScratchIns")) {// 车身划痕险
                        map.put("insureItem.scratchIns.isInsured", Boolean.TRUE);// 保障项目.投保车身划痕险
                        map.put("insureItem.scratchIns.coverage", amount);// 保障项目.划痕险保额
                        map.put("insureItem.ncfScratchIns.isInsured", isInsured);// 保障项目.投保划痕不计免赔
                    } else if (inskindcode.trim().equals("WadingIns")) {// 涉水损失险
                        map.put("insureItem.wadingIns.isInsured", Boolean.TRUE);// 保障项目.投保涉水损失险
                        map.put("insureItem.wadingIns.coverage", amount);// 保障项目.涉水损失险保额
                        map.put("insureItem.ncfWadingIns.isInsured", isInsured);// 保障项目.投保涉水不计免赔
                    } else if (inskindcode.trim().equals("CompensationForMentalDistressIns")) {// 精神损害抚慰金责任险
                        map.put("insureItem.compensationForMentalDistressIns.isInsured", Boolean.TRUE);// 保障项目.投保精神损害抚慰金责任险
                        map.put("insureItem.compensationForMentalDistressIns.coverage", amount);// 保障项目.精神损害抚慰金责任险保额
                        map.put("insureItem.ncfCompensationForMentalDistressIns.isInsured", isInsured);// 保障项目.投保精神损害抚慰金责任险不计免赔
                    } else if (inskindcode.trim().equals("GlassIns")) {// 玻璃单独破碎险
                        map.put("insureItem.glassIns.isInsured", Boolean.TRUE);// 保障项目.投保玻璃破损险
                        map.put("insureItem.ncfGlassIns.isInsured", isInsured);// 保障项目.投保玻璃不计免赔

                        Integer glassType = amount.intValue();
                        if (glassType == 0) {
                            glassType = 1;
                        }
                        map.put("car.specific.glassType", glassType);// 车辆信息.玻璃类型
                    } else if (inskindcode.trim().equals("MirrorLightIns")) {// 倒车镜车灯损坏险
                        map.put("insureItem.mirrorLightIns.isInsured", Boolean.TRUE);// 保障项目.投保倒车镜破损险
                    } else if (inskindcode.trim().equals("SpecifyingPlantCla")) {// 指定专修厂
                        map.put("insureItem.specifyingPlantCla.isInsured", Boolean.TRUE);// 保障项目.指定专修厂
                    } else if (inskindcode.trim().equals("VehicleDemageMissedThirdPartyCla")) {
                        map.put("insureItem.vehicleDemageMissedThirdPartyCla.isInsured", Boolean.TRUE);// 保障项目.投保无法找到第三方特约险
                    } else if (inskindcode.trim().equals("VehicleCompulsoryIns")) {// 交强险
                        map.put("insureItem.vehicleCompulsoryIns.isInsured", Boolean.TRUE);// 保障项目.投保交强险
                    } else if (inskindcode.trim().equals("VehicleTax")) {// 车船税
                        map.put("insureItem.vehicleTax.isInsured", Boolean.TRUE);// 保障项目.缴纳车船税
                    }
                }

                String inskindtype = insbCarkindprice.getInskindtype();// 险别类型
                if (StringUtil.isNotEmpty(inskindtype)) {
                    if ("0".equals(inskindtype)) {
                        commercial = Boolean.TRUE;
                    } else if ("2".equals(inskindtype)) {
                        traffic = Boolean.TRUE;
                    }
                }
            }

            Integer insureType = null;
            if (commercial && traffic) {
                insureType = Integer.valueOf(3);
            } else {
                if (commercial) {
                    insureType = Integer.valueOf(1);
                } else if (traffic) {
                    insureType = Integer.valueOf(2);
                }
            }
            map.put("application.insureType", insureType);// 投保信息.投保险种类别
            // 单商:1,单交:2,混双:3
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.zzb.app.service.AppQuotationService#getQuotationValidatedInfo(java.lang.String)
     */
    @Override
    public String getQuotationValidatedInfo(String subtaskid, String taskid, String inscomcode) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (!StringUtils.isNoneBlank(taskid, inscomcode)) {
            map.put("success", Boolean.FALSE);
            map.put("resultMsg", "参数错误：任务号或保险公司编码为空");
            LogUtil.error("任务号或保险公司编码为空：" + taskid + ", " + inscomcode);
        } else {
            String quotationInfo = null;
            try {
                quotationInfo = getQuotationValidOrQuotationInfo(subtaskid, taskid, inscomcode, "checkRule");
            } catch (Exception e) {
                map.put("success", Boolean.FALSE);
                map.put("resultMsg", "规则校验时发生了异常："+e.getMessage());
                LogUtil.error("规则校验时发生了异常" + taskid + "," + inscomcode + "：" + e.getMessage());
                e.printStackTrace();
            }

            if (StringUtils.isNotBlank(quotationInfo)) {
                JSONObject jobj = JSONObject.fromObject(quotationInfo);
                if (jobj != null && jobj.containsKey("ruleItem.isRuleChecked")) {
                    boolean isRuleChecked = jobj.getBoolean("ruleItem.isRuleChecked");
                    if (!isRuleChecked) {
                        if (jobj.containsKey("ruleItem.quotationMode")) {
                            map.put("quotationMode", jobj.get("ruleItem.quotationMode"));
                        } else {
                            map.put("quotationMode", 2);
                        }
                        if (jobj.containsKey("ruleItem.execRuleQuote")) {
                            map.put("execRuleQuote", jobj.get("ruleItem.execRuleQuote"));
                        } else {
                            map.put("execRuleQuote", false);
                        }
                        if (jobj.containsKey("ruleItem.ruleValue")) {
                            Object obj = jobj.get("ruleItem.ruleValue");
                            if (obj instanceof JSONArray) {
                                JSONArray jArray = (JSONArray) obj;
                                if (jArray != null && jArray.size() > 0 && !jArray.isEmpty()) {
                                    String[] errorMsg = new String[jArray.size()];
                                    for (int i = 0; i < jArray.size(); i++) {
                                        JSONObject errjObj = (JSONObject) jArray.get(i);
                                        String ruleMsg = errjObj.getString("ruleItem.ruleMsg");
                                        errorMsg[i] = (ruleMsg == null) ? "" : ruleMsg.trim();
                                    }
                                    map.put("success", Boolean.FALSE);
                                    map.put("resultMsg", errorMsg);
                                }
                            } else if (obj instanceof String) {
                                map.put("success", Boolean.FALSE);
                                map.put("resultMsg", "[" + obj + "]");
                            }
                        }
                        if(jobj.containsKey("ruleItem.execRuleQuoteCause")){
                        	map.put("execRuleQuoteCause", jobj.get("ruleItem.execRuleQuoteCause"));
                        }
                    } else {
                        if (jobj.containsKey("ruleItem.quotationMode")) {
                            map.put("quotationMode", jobj.get("ruleItem.quotationMode"));
                        }

                        if (jobj.containsKey("ruleItem.execRuleQuote")) {
                            map.put("execRuleQuote", jobj.get("ruleItem.execRuleQuote"));
                        } else {
                            map.put("execRuleQuote", false);
                        }
                        if(jobj.containsKey("ruleItem.execRuleQuoteCause")){
                        	map.put("execRuleQuoteCause", jobj.get("ruleItem.execRuleQuoteCause"));
                        }
                        if (jobj.containsKey("ruleItem.ruleValue")) {
                            Object obj = jobj.get("ruleItem.ruleValue");
                            if (obj instanceof JSONArray) {
                                JSONArray jArray = (JSONArray) obj;
                                if (jArray != null && jArray.size() > 0 && !jArray.isEmpty()) {
                                    String[] errorMsg = new String[jArray.size()];
                                    for (int i = 0; i < jArray.size(); i++) {
                                        JSONObject errjObj = (JSONObject) jArray.get(i);
                                        String ruleMsg = errjObj.getString("ruleItem.ruleMsg");
                                        errorMsg[i] = (ruleMsg == null) ? "" : ruleMsg.trim();
                                    }
                                    map.put("resultMsg", errorMsg);
                                }
                            } else if (obj instanceof String) {
                                map.put("resultMsg", "[" + obj + "]");
                            }
                        }
                        map.put("success", Boolean.TRUE);
                        //map.put("resultMsg", "规则校验成功");
                    }
                }
            }
        }

        String result = null;
        if (map != null && !map.isEmpty()) {
            result = JSONObject.fromObject(map).toString();
        }
        return result;
    }

    /**
     * 获得报价信息
     *
     * @param taskid
     * @param inscomcode
     * @return
     */
    public INSBQuoteinfo getQuoteinfo(String taskid, String inscomcode) {
        INSBQuoteinfo quoteinfo = null;
        INSBQuotetotalinfo qti = new INSBQuotetotalinfo();
        qti.setTaskid(taskid);
        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(qti);
        String quoteTotalInfoId = null;
        if (quotetotalinfo != null) {
            quoteTotalInfoId = quotetotalinfo.getId();
        }

        if (quoteTotalInfoId != null) {
            quoteinfo = insbQuoteinfoService.getQuoteinfo(quoteTotalInfoId, inscomcode);
        }
        return quoteinfo;
    }

    @Override
    public String getQuotationInfo(String subtaskid, String taskid, String inscomcode) throws ServiceException {
        boolean quotationInfoResult = false;
        boolean carkindpriceResult = false;
        boolean carInfoResult = false;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isNoneBlank(taskid, inscomcode)) {
            map.put("success", Boolean.FALSE);
            map.put("resultMsg", "参数错误：任务号或保险公司编码为空");
            LogUtil.error("taskid or inscomcode is null or empty!");
        } else {
            String quotationInfo = null;
            String errMsg = "";
            try {
                quotationInfo = getQuotationValidOrQuotationInfo(subtaskid,taskid, inscomcode, "calculator");
//				quotationInfo ="{application.commercialClaimRate=0.0, application.discountPremium=8590.16, application.premium=11658.79, car.specific.depPrice=328048.0, car.specific.floatPrice=353500.0, car.specific.isDanger=false, car.specific.isRob=false, car.specific.isSpecial=false, car.specific.maxPrice=505000.0, car.specific.maxPriceRate=1.0, car.specific.minPrice=353500.0, car.specific.minPriceRate=0.7, car.specific.taxPrice=505000.0, insureItem.commercialIns.discountPremium=7160.16, insureItem.commercialIns.premium=10228.79, insureItem.driverIns.coverage=10000.0, insureItem.driverIns.discountPremium=29.4, insureItem.driverIns.premium=42.0, insureItem.ncfDriverIns.discountPremium=4.41, insureItem.ncfDriverIns.premium=6.3, insureItem.ncfPassengerIns.discountPremium=8.51, insureItem.ncfPassengerIns.premium=12.15, insureItem.ncfTheftIns.discountPremium=241.84, insureItem.ncfTheftIns.premium=345.49, insureItem.ncfThirdPartyIns.discountPremium=137.97, insureItem.ncfThirdPartyIns.premium=197.1, insureItem.ncfVehicleDemageIns.discountPremium=593.78, insureItem.ncfVehicleDemageIns.premium=848.26, insureItem.passengerIns.coverage=10000.0, insureItem.passengerIns.discountPremium=56.7, insureItem.passengerIns.premium=81.0, insureItem.theftIns.coverage=328048.0, insureItem.theftIns.discountPremium=1209.21, insureItem.theftIns.premium=1727.44, insureItem.thirdPartyIns.coverage=200000.0, insureItem.thirdPartyIns.discountPremium=919.8, insureItem.thirdPartyIns.premium=1314.0, insureItem.vehicleCompulsoryIns.discountPremium=950.0, insureItem.vehicleCompulsoryIns.premium=950.0, insureItem.vehicleDemageIns.coverage=353500.0, insureItem.vehicleDemageIns.discountPremium=3958.54, insureItem.vehicleDemageIns.premium=5655.05, insureItem.vehicleTax.premium=480.0, ruleItem.channelDiscount=1.0, ruleItem.compulsoryDiscount=1.0, ruleItem.discount=0.7, ruleItem.floatPriceRate=0.3, ruleItem.isCustomCombustionInsCoverage=true, ruleItem.isCustomTheftInsCoverage=true, ruleItem.isRuleChecked=true, ruleItem.quotationMode=0, ruleItem.repairDiscount=0.0, ruleItem.ruleValue=null, ruleItem.runningRecord=[145975, 146066, 145976, 145985, 145984, 145979, 146045], ruleItem.taxDerateType=0, ruleItem.wadingInsCoverageIsMust=false}";
                INSBQuoteinfo quoteinfo = getQuoteinfo(taskid, inscomcode);
                if (StringUtils.isNotBlank(quotationInfo)) {
                    JSONObject jobj = JSONObject.fromObject(quotationInfo);
                    if (jobj != null&& jobj.containsKey("ruleItem.isRuleChecked")) {
                        boolean isRuleChecked = jobj.getBoolean("ruleItem.isRuleChecked");
                        if (isRuleChecked) {
                            quotationInfoResult = quoteinfoSave(quoteinfo, jobj);
                            carkindpriceResult = insbcarkindpriceSave(taskid, inscomcode, jobj);
                            carInfoResult = carinfoSave(taskid, inscomcode, jobj);
                            policySave(taskid, inscomcode, jobj);
//							orderSave(taskid, inscomcode, jobj);
                        } else {
                            if (jobj.containsKey("ruleItem.ruleValue")) {
                                Object obj = jobj.get("ruleItem.ruleValue");
                                if (obj instanceof JSONArray) {
                                    JSONArray jArray = (JSONArray) obj;
                                    if (jArray != null && jArray.size() > 0 && !jArray.isEmpty()) {
                                        String[] errorMsg = new String[jArray.size()];
                                        for (int i = 0; i < jArray.size(); i++) {
                                            JSONObject errjObj = (JSONObject) jArray.get(i);
                                            String ruleMsg = errjObj.getString("ruleItem.ruleMsg");
                                            errorMsg[i] = (ruleMsg == null) ? "" : ruleMsg.trim();
                                        }
                                        errMsg = JSONArray.fromObject(errorMsg).toString();
                                    }
                                } else if (obj instanceof String) {
                                    errMsg = "[" + obj + "]";
                                }
                            }
                        }
                    }
                }
                if (quotationInfoResult && carkindpriceResult && carInfoResult) {
                    map.put("success", Boolean.TRUE);
                    map.put("resultMsg", "规则报价成功");
                } else {
                    quoteinfo.setQuoteamount(Double.valueOf(0.00));
                    quoteinfo.setQuotediscountamount(Double.valueOf(0.00));
                    quoteinfo.setQuoteresult("false");
                    if (StringUtil.isEmpty(errMsg)) {
                        errMsg = "未知";
                    }
                    quoteinfo.setQuoteinfo("规则报价失败，原因：" + errMsg);
                    insbQuoteinfoService.updateById(quoteinfo);
                    map.put("success", Boolean.FALSE);
                    map.put("resultMsg", "规则报价失败，原因：" + errMsg);
                }
            } catch (Exception e) {
                LogUtil.error("规则报价失败"+taskid+","+inscomcode+"出错："+e.getMessage());
                e.printStackTrace();
                map.put("success", Boolean.FALSE);
                map.put("resultMsg", "规则报价失败，报价时发生了异常："+e.getMessage());
            }
        }
        return JSONObject.fromObject(map).toString();
    }

    public void updatePolicyDiscount(String subTaskId, String taskId, String inscomcode) {
        if (StringUtil.isEmpty(taskId) || StringUtil.isEmpty(subTaskId)) {
            LogUtil.error("任务号为空：%s, %s", taskId, subTaskId);
            return;
        }

        INSBQuoteinfo quoteinfo = null;

        if (StringUtil.isEmpty(inscomcode)) {
            quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subTaskId);
            if (quoteinfo != null) {
                inscomcode = quoteinfo.getInscomcode();
            }
        }
        if (StringUtil.isEmpty(inscomcode)) {
            LogUtil.error("%s, %s投保公司编码为空", taskId, subTaskId);
            return;
        }

        INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
        insbRulequerycarinfo.setTaskid(taskId);
        List<INSBRulequerycarinfo> list = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo);
        if (list == null || list.isEmpty() || list.stream().noneMatch(item->"guizequery".equals(item.getOperator()))) {
            LogUtil.warn("%s无平台查询信息，不请求规则折扣系数", taskId);
            return;
        }

        try {
            String ruleResult = getQuotationValidOrQuotationInfo(subTaskId, taskId, inscomcode, "geniusRule");
            if (StringUtil.isEmpty(ruleResult)) {
                LogUtil.error("%s,%s规则返回数据为空", taskId, inscomcode);
                return;
            }

            JSONObject jobj = JSONObject.fromObject(ruleResult);

            if (jobj == null) {
                LogUtil.error("%s,%s规则返回折扣系数为空", taskId, inscomcode);
                return;
            }

            Map<String, String> discount = new HashMap<>(ConstUtil.DiscountKeys.length);

            for (String key : ConstUtil.DiscountKeys) {
                if (jobj.containsKey(key) && !StringUtil.isEmpty(jobj.get(key))) {
                    discount.put(key, jobj.getString(key));
                }
            }

            if (discount.isEmpty()) {
                LogUtil.error("%s,%s规则返回折扣系数为空", taskId, inscomcode);
                return;
            }

            if (quoteinfo == null) {
                quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subTaskId);
            }
            if (quoteinfo != null) {
                String discountrate = JSONObject.fromObject(discount).toString();
                quoteinfo.setDiscountrate(discountrate);
                insbQuoteinfoService.updateById(quoteinfo);
                LogUtil.error("%s,%s保存折扣系数：%s", taskId, inscomcode, discountrate);
            } else {
                LogUtil.error("%s,%s,%s无投保数据", taskId, inscomcode, subTaskId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void orderSave(String taskid, String inscomcode, JSONObject jobj) {
        INSBOrder ov = new INSBOrder();
        ov.setTaskid(taskid);
        ov.setPrvid(inscomcode);
        INSBOrder order= insbOrderDao.selectOne(ov);
        //application.discountPremium	保费信息.合计折扣保费
        if (jobj.containsKey("application.discountPremium")) {
            order.setTotalpaymentamount(jobj.getDouble("application.discountPremium"));// 商业险合计折扣保费
            order.setTotalproductamount(jobj.getDouble("application.discountPremium"));
        }
        insbOrderDao.updateById(order);
        LogUtil.info("规则计算--taskid ="+taskid +",inscomcode"+inscomcode +"-----订单保存"+order);
    }

    private void policySave(String taskid, String inscomcode, JSONObject jobj) {
        INSBPolicyitem  polic = new INSBPolicyitem();
        polic.setTaskid(taskid);
        polic.setInscomcode(inscomcode);
        polic.setRisktype("0");//商业险
        INSBPolicyitem pc = insbPolicyitemDao.selectOne(polic);

        if (pc != null) {
            //insureItem.commercialIns.discountPremium	保费信息.商业险合计折扣保费
            if (jobj.containsKey("insureItem.commercialIns.discountPremium")) {
                pc.setDiscountCharge(jobj.getDouble("insureItem.commercialIns.discountPremium"));// 商业险合计折扣保费
            }
            //ruleItem.discount	规则信息.折扣系数
            if (jobj.containsKey("ruleItem.discount")) {
                pc.setDiscountRate(jobj.getDouble("ruleItem.discount"));//商业险折扣系数
            }
            if (jobj.containsKey("application.discountPremium")) {
                pc.setTotalepremium(jobj.getDouble("application.discountPremium"));
            }
            insbPolicyitemDao.updateById(pc);
            LogUtil.info("规则计算--taskid =" + taskid + ",inscomcode=" + inscomcode + "-----商业保单保存:" + pc.getDiscountCharge() + "," + pc.getDiscountRate());
        }

        polic.setRisktype("1");//交强
        INSBPolicyitem pc2 = insbPolicyitemDao.selectOne(polic);

        if (pc2 != null) {
            //insureItem.vehicleCompulsoryIns.discountPremium	保费信息.交强险折扣保费
            if (jobj.containsKey("insureItem.vehicleCompulsoryIns.discountPremium")) {
                pc2.setDiscountCharge(jobj.getDouble("insureItem.vehicleCompulsoryIns.discountPremium"));// 交强险折扣保费
            }
            //ruleItem.compulsoryDiscount	规则信息.交强险折扣系数
            if (jobj.containsKey("ruleItem.compulsoryDiscount")) {
                pc2.setDiscountRate(jobj.getDouble("ruleItem.compulsoryDiscount"));//商业险折扣系数
            }
            if (jobj.containsKey("application.discountPremium")) {
                pc2.setTotalepremium(jobj.getDouble("application.discountPremium"));
            }
            insbPolicyitemDao.updateById(pc2);
            LogUtil.info("规则计算--taskid =" + taskid + ",inscomcode=" + inscomcode + "-----交强保单保存:" + pc2.getDiscountCharge() + "," + pc2.getDiscountRate());
        }
    }

    private boolean quoteinfoSave(INSBQuoteinfo quoteinfo, JSONObject jobj) {
        boolean quotationInfoResult = false;
        if (quoteinfo != null) {
            quoteinfo.setModifytime(new Date());
            if (jobj.containsKey("application.premium")) {// 保费信息.合计标准保费
                quoteinfo.setQuoteamount(jobj.getDouble("application.premium"));
            }
            if (jobj.containsKey("application.discountPremium")) {// 保费信息.合计折扣保费
                quoteinfo.setQuotediscountamount(jobj.getDouble("application.discountPremium"));
            }
            quoteinfo.setQuoteresult("true");
            quoteinfo.setQuoteinfo("规则报价成功");
            quoteinfo.setTaskstatus("0");
            insbQuoteinfoService.updateById(quoteinfo);
            quotationInfoResult = true;
            LogUtil.info("规则计算--taskid =" + quoteinfo.getQuoteinfo() + ",inscomcode=" + quoteinfo.getInscomcode() +
                    "-----quoteinfo保存:" + quoteinfo.getQuoteamount() + "," + quoteinfo.getQuotediscountamount());
        }
        return quotationInfoResult;
    }

    private boolean insbcarkindpriceSave(String taskid, String inscomcode, JSONObject jobj) {
        boolean carkindpriceResult = false;
        List<INSBCarkindprice> carkindpriceList = getCarkindprice(taskid, inscomcode);
        if (carkindpriceList != null && carkindpriceList.size() > 0 && !carkindpriceList.isEmpty()) {
            carkindpriceResult = updateQuotatioDetailInfo(carkindpriceList, jobj);
        }
        LogUtil.info("规则计算--taskid ="+taskid +",inscomcode"+inscomcode +"-----carkind保存:"+carkindpriceList);
        return carkindpriceResult;
    }

    private boolean carinfoSave(String taskid, String inscomcode, JSONObject jobj) {
        boolean carInfoResult = false;
        INSBCarinfohis carInfoHis = new INSBCarinfohis();
        carInfoHis.setTaskid(taskid);
        carInfoHis.setInscomcode(inscomcode);
        INSBCarinfohis carInfoHisBean = insbCarinfohisDao.selectOne(carInfoHis);
        if (carInfoHisBean == null) {
            INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
            if (carInfo != null) {
                carInfoResult = updateCarInfo(carInfo, jobj);
            }
        } else {
            carInfoResult = updateCarInfoHis(carInfoHisBean, jobj);
        }
        LogUtil.info("规则计算--taskid ="+taskid +",inscomcode"+inscomcode +"-----carinfo保存:"+carInfoHisBean);
        return carInfoResult;
    }

    /**
     *
     * @param carkindpriceList
     * @param jobj
     *            各险别保费信息
     * @return
     */
    public boolean updateQuotatioDetailInfo(List<INSBCarkindprice> carkindpriceList, JSONObject jobj) {
        boolean result = false;
        List<INSBCarkindprice> modifiedList = new ArrayList<INSBCarkindprice>();
        for (INSBCarkindprice carkindprice : carkindpriceList) {
            if (carkindprice != null) {
                String inskindcode = carkindprice.getInskindcode();
                if ("TheftIns".equals(inskindcode)) {// 全车盗抢险
                    if (jobj.containsKey("insureItem.theftIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.theftIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.theftIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.theftIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfTheftIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfTheftIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfTheftIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfTheftIns.discountPremium"));// 不计免赔折后保费
                    }
                    if (jobj.containsKey("insureItem.theftIns.coverage")) {
                        carkindprice.setAmount(jobj.getDouble("insureItem.theftIns.coverage")); //保额回写
                    }
                } else if ("NcfTheftIns".equals(inskindcode)) {// 附加全车盗抢险不计免赔
                    if (jobj.containsKey("insureItem.ncfTheftIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfTheftIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfTheftIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfTheftIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("ThirdPartyIns".equals(inskindcode)) {// 第三者责任险
                    if (jobj.containsKey("insureItem.thirdPartyIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.thirdPartyIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.thirdPartyIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.thirdPartyIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfThirdPartyIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfThirdPartyIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfThirdPartyIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfThirdPartyIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfThirdPartyIns".equals(inskindcode)) {// 附加第三者责任险不计免赔
                    if (jobj.containsKey("insureItem.ncfThirdPartyIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfThirdPartyIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfThirdPartyIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfThirdPartyIns.discountPremium"));// 不计免赔折后保费
                    }
                }
                else if ("VehicleDemageIns".equals(inskindcode)) {// 车辆损失险
                    if (jobj.containsKey("insureItem.vehicleDemageIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.vehicleDemageIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.vehicleDemageIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.vehicleDemageIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfVehicleDemageIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfVehicleDemageIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfVehicleDemageIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfVehicleDemageIns.discountPremium"));// 不计免赔折后保费
                    }
                    if (jobj.containsKey("insureItem.vehicleDemageIns.coverage")) {
                        carkindprice.setAmount(jobj.getDouble("insureItem.vehicleDemageIns.coverage")); //保额回写
                    }
                } else if ("NcfVehicleDemageIns".equals(inskindcode)) {// 附加车辆损失险不计免赔
                    if (jobj.containsKey("insureItem.ncfVehicleDemageIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfVehicleDemageIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfVehicleDemageIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfVehicleDemageIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("PassengerIns".equals(inskindcode)) {// 乘客责任险
                    if (jobj.containsKey("insureItem.passengerIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.passengerIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.passengerIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.passengerIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfPassengerIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfPassengerIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfPassengerIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfPassengerIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfPassengerIns".equals(inskindcode)) {// 附加乘客责任险不计免赔
                    if (jobj.containsKey("insureItem.ncfPassengerIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfPassengerIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfPassengerIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfPassengerIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("SpecifyingPlantCla".equals(inskindcode)) {// 指定专修厂
                    if (jobj.containsKey("insureItem.specifyingPlantCla.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.specifyingPlantCla.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.specifyingPlantCla.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.specifyingPlantCla.discountPremium"));// 折后保费
                    }
                } else if ("WadingIns".equals(inskindcode)) {// 涉水损失险
                    if (jobj.containsKey("insureItem.wadingIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.wadingIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.wadingIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.wadingIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfWadingIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfWadingIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfWadingIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfWadingIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfWadingIns".equals(inskindcode)) {// 附加涉水损失险不计免赔
                    if (jobj.containsKey("insureItem.ncfWadingIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfWadingIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfWadingIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfWadingIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("GlassIns".equals(inskindcode)) {// 玻璃单独破碎险
                    if (jobj.containsKey("insureItem.glassIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.glassIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.glassIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.glassIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfGlassIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfGlassIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfGlassIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfGlassIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("CombustionIns".equals(inskindcode)) {// 自燃损失险
                    if (jobj.containsKey("insureItem.combustionIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.combustionIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.combustionIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.combustionIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfCombustionIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfCombustionIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfCombustionIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfCombustionIns.discountPremium"));// 不计免赔折后保费
                    }
                    if (jobj.containsKey("insureItem.combustionIns.coverage")) {
                        carkindprice.setAmount(jobj.getDouble("insureItem.combustionIns.coverage")); //保额回写
                    }
                } else if ("NcfCombustionIns".equals(inskindcode)) {// 附加自燃损失险不计免赔
                    if (jobj.containsKey("insureItem.ncfCombustionIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfCombustionIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfCombustionIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfCombustionIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("GoodsOnVehicleIns".equals(inskindcode)) {// 车上货物责任险
                    if (jobj.containsKey("insureItem.goodsOnTruckIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.goodsOnTruckIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.goodsOnTruckIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.goodsOnTruckIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfGoodsOnTruckIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfGoodsOnTruckIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfGoodsOnTruckIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfGoodsOnTruckIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfGoodsOnVehicleIns".equals(inskindcode)) {// 附加车上货物责任险不计免赔
                    if (jobj.containsKey("insureItem.ncfGoodsOnTruckIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfGoodsOnTruckIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfGoodsOnTruckIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfGoodsOnTruckIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("ScratchIns".equals(inskindcode)) {// 车身划痕险
                    if (jobj.containsKey("insureItem.scratchIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.scratchIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.scratchIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.scratchIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfScratchIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfScratchIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfScratchIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfScratchIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfScratchIns".equals(inskindcode)) {// 附加车身划痕险不计免赔
                    if (jobj.containsKey("insureItem.ncfScratchIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfScratchIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfScratchIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfScratchIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("DriverIns".equals(inskindcode)) {// 司机责任险
                    if (jobj.containsKey("insureItem.driverIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.driverIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.driverIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.driverIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfDriverIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfDriverIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfDriverIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfDriverIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfDriverIns".equals(inskindcode)) {// 附加司机责任险不计免赔
                    if (jobj.containsKey("insureItem.ncfDriverIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfDriverIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfDriverIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfDriverIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("CompensationForMentalDistressIns".equals(inskindcode)) {// 精神损害抚慰金责任险
                    if (jobj.containsKey("insureItem.compensationForMentalDistressIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.compensationForMentalDistressIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.compensationForMentalDistressIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.compensationForMentalDistressIns.discountPremium"));// 折后保费
                    }
                    if (jobj.containsKey("insureItem.ncfCompensationForMentalDistressIns.premium")) {
                        carkindprice.setBjmpOrgCharge(jobj.getDouble("insureItem.ncfCompensationForMentalDistressIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfCompensationForMentalDistressIns.discountPremium")) {
                        carkindprice.setBjmpDiscountCharge(jobj.getDouble("insureItem.ncfCompensationForMentalDistressIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("NcfCompensationForMentalDistressIns".equals(inskindcode)) {// 精神损害抚慰金责任险不计免赔
                    if (jobj.containsKey("insureItem.ncfCompensationForMentalDistressIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.ncfCompensationForMentalDistressIns.premium"));// 不计免赔原始保费
                    }
                    if (jobj.containsKey("insureItem.ncfCompensationForMentalDistressIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.ncfCompensationForMentalDistressIns.discountPremium"));// 不计免赔折后保费
                    }
                } else if ("VehicleDemageMissedThirdPartyCla".equals(inskindcode)) {// 机动车损失保险无法找到第三方特约险
                    if (jobj.containsKey("insureItem.vehicleDemageMissedThirdPartyCla.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.vehicleDemageMissedThirdPartyCla.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.vehicleDemageMissedThirdPartyCla.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.vehicleDemageMissedThirdPartyCla.discountPremium"));// 折后保费
                    }
                } else if ("VehicleCompulsoryIns".equals(inskindcode)) {// 交强险
                    if (jobj.containsKey("insureItem.vehicleCompulsoryIns.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.vehicleCompulsoryIns.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.vehicleCompulsoryIns.discountPremium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.vehicleCompulsoryIns.discountPremium"));// 折后保费
                    }
                } else if ("VehicleTax".equals(inskindcode)) {// 车船税
                    if (jobj.containsKey("insureItem.vehicleTax.premium")) {
                        carkindprice.setAmountprice(jobj.getDouble("insureItem.vehicleTax.premium"));// 保费
                    }
                    if (jobj.containsKey("insureItem.vehicleTax.premium")) {
                        carkindprice.setDiscountCharge(jobj.getDouble("insureItem.vehicleTax.premium"));// 折后保费
                    }
                }
                modifiedList.add(carkindprice);
            }
        }
        if (modifiedList != null && modifiedList.size() > 0 && !modifiedList.isEmpty()) {
            for (INSBCarkindprice entity : modifiedList) {
                if (entity != null) {
                    insbCarkindpriceDao.updateById(entity);
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 更新车辆信息
     *
     * @param carInfo
     * @param jobj
     * @return
     */
    public boolean updateCarInfo(INSBCarinfo carInfo, JSONObject jobj) {
        boolean result = false;
        if (jobj.containsKey("car.specific.taxPrice")) {
            carInfo.setTaxprice(jobj.getDouble("car.specific.taxPrice"));// 车辆信息.新车购置价
        }
        if (jobj.containsKey("car.specific.depPrice")) {
            carInfo.setDepprice(jobj.getDouble("car.specific.depPrice"));// 车辆信息.车辆折旧价
        }
        if (jobj.containsKey("car.specific.floatPrice")) {
            carInfo.setFloatprice(jobj.getDouble("car.specific.floatPrice"));// 车辆信息.车辆浮动价
        }
        if (jobj.containsKey("car.specific.isDanger")) {
            carInfo.setIsdanger(jobj.getBoolean("car.specific.isDanger") == true ? "1"
                    : "0");// 车辆信息.是高危限制车型
        }
        if (jobj.containsKey("car.specific.isRob")) {
            carInfo.setIsrob(jobj.getBoolean("car.specific.isRob") == true ? "1"
                    : "0");// 车辆信息.是易盗车型
        }
        if (jobj.containsKey("car.specific.isSpecial")) {
            carInfo.setIsspecial(jobj.getBoolean("car.specific.isSpecial") == true ? "1"
                    : "0");// 车辆信息.是管控车型
        }
        if (jobj.containsKey("car.specific.maxPrice")) {
            carInfo.setMaxprice(jobj.getDouble("car.specific.maxPrice"));// 车辆信息.车辆浮动价上限
        }
        if (jobj.containsKey("car.specific.maxPriceRate")) {
            carInfo.setMaxpricerate(jobj.getDouble("car.specific.maxPriceRate"));// 车辆信息.车辆浮动价上限比率
        }
        if (jobj.containsKey("car.specific.minPrice")) {
            carInfo.setMinprice(jobj.getDouble("car.specific.minPrice"));// 车辆信息.车辆浮动价下限
        }
        if (jobj.containsKey("car.specific.minPriceRate")) {
            carInfo.setMinpricerate(jobj.getDouble("car.specific.minPriceRate"));// 车辆信息.车辆浮动价下限比率
        }
        insbCarinfoDao.updateById(carInfo);
        result = true;
        return result;
    }

    /**
     * 更新车辆信息备份表
     *
     * @param carInfoHis
     * @param jobj
     * @return
     */
    public boolean updateCarInfoHis(INSBCarinfohis carInfoHis, JSONObject jobj) {
        boolean result = false;
        if (jobj.containsKey("car.specific.taxPrice")) {
            carInfoHis.setTaxprice(jobj.getDouble("car.specific.taxPrice"));// 车辆信息.新车购置价
        }
        if (jobj.containsKey("car.specific.depPrice")) {
            carInfoHis.setDepprice(jobj.getDouble("car.specific.depPrice"));// 车辆信息.车辆折旧价
        }
        if (jobj.containsKey("car.specific.floatPrice")) {
            carInfoHis.setFloatprice(jobj.getDouble("car.specific.floatPrice"));// 车辆信息.车辆浮动价
        }
        if (jobj.containsKey("car.specific.isDanger")) {
            carInfoHis.setIsdanger(jobj.getBoolean("car.specific.isDanger") == true ? "1" : "0");// 车辆信息.是高危限制车型
        }
        if (jobj.containsKey("car.specific.isRob")) {
            carInfoHis.setIsrob(jobj.getBoolean("car.specific.isRob") == true ? "1" : "0");// 车辆信息.是易盗车型
        }
        if (jobj.containsKey("car.specific.isSpecial")) {
            carInfoHis.setIsspecial(jobj.getBoolean("car.specific.isSpecial") == true ? "1" : "0");// 车辆信息.是管控车型
        }
        if (jobj.containsKey("car.specific.maxPrice")) {
            carInfoHis.setMaxprice(jobj.getDouble("car.specific.maxPrice"));// 车辆信息.车辆浮动价上限
        }
        if (jobj.containsKey("car.specific.maxPriceRate")) {
            carInfoHis.setMaxpricerate(jobj.getDouble("car.specific.maxPriceRate"));// 车辆信息.车辆浮动价上限比率
        }
        if (jobj.containsKey("car.specific.minPrice")) {
            carInfoHis.setMinprice(jobj.getDouble("car.specific.minPrice"));// 车辆信息.车辆浮动价下限
        }
        if (jobj.containsKey("car.specific.minPriceRate")) {
            carInfoHis.setMinpricerate(jobj.getDouble("car.specific.minPriceRate"));// 车辆信息.车辆浮动价下限比率
        }
        insbCarinfohisDao.updateById(carInfoHis);
        result = true;
        return result;
    }

    @Override
    public String getQuotationInfoForFlow(String subtaskId, String taskId, String inscomcode) throws ServiceException {
    	String ruleResult = getQuotationInfo(subtaskId, taskId, inscomcode);
        Map<String, Object> rMap = new HashMap<String, Object>();
        appInsuredQuoteService.getPriceParamWay(rMap, taskId, inscomcode, "3");
        WorkFlowUtil.completeSubTask(ruleResult, subtaskId, "admin",rMap);
        /*String result = WorkFlowUtil.completeSubTask(ruleResult, subtaskId, "admin",rMap);
         JSONObject r =JSONObject.fromObject(result);
        String mes=r.getString("message");
        if(!StringUtil.isEmpty(mes)&&"fail".equals(mes)){
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result=WorkFlowUtil.completeSubTask(ruleResult, subtaskId, "admin",rMap);
        }*/
        return "{\"ruleQuotationResult\":true}";
    }

    @Override
    public String getPriceInterval(String subtaskId, String taskId,
                                   String inscomcode, Double taxPrice, Double price,
                                   Double analogyTaxPrice, Double analogyPrice, String regDate,
                                   int seats) throws ServiceException {

        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> paramMap = new HashMap<String, Object>(88);
        String insurancearea = null;// 投保地区
        INSBQuotetotalinfo qti = new INSBQuotetotalinfo();
        qti.setTaskid(taskId);
        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(qti);
        String quoteTotalInfoId = null;
        if (quotetotalinfo != null) {
            insurancearea = quotetotalinfo.getInsurancearea();
            quoteTotalInfoId = quotetotalinfo.getId();
        }
        INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfo(quoteTotalInfoId, inscomcode);
        String agreementtrule = null;
        if (StringUtils.isNotBlank(quoteinfo.getAgreementid())) {
            agreementtrule = insbAgreementService.queryById(quoteinfo.getAgreementid()).getAgreementtrule();
            if (StringUtils.isNotBlank(agreementtrule)) {
                paramMap.putAll(getRuleInfoMap(agreementtrule));
            }
        }
        paramMap.put("car.specific.taxPrice", taxPrice);
        paramMap.put("car.specific.price", price);
        if (analogyTaxPrice != null)
            paramMap.put("car.specific.analogyTaxPrice", analogyTaxPrice);
        if (analogyPrice != null)
            paramMap.put("car.specific.analogyPrice", analogyPrice);
        paramMap.put("api.log.enquiry.id", taskId + "_" + inscomcode);// 任务ID
        paramMap.put("car.model.seats", seats);
        paramMap.put("car.specific.regDate", regDate);
        // String jstr = JsonUtil.getJsonStringDate(paramMap);
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("methodName", "priceInterval");
        // map.put("paramStr", jstr);
        // IClientRuleApi client = new ClientOfferApi();
        // try {
        // ruleResult = client.getRuleResult(map);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //Map<String, Object> ruleResult = null;
        LogUtil.info("规则入参 taskid=" + taskId + ",inscomcode=" + inscomcode + ",map=" + paramMap);
        //http接口
        try {
        	RULE_PARAM_HOST = ruleResourceBundle.getString("rule.handler.server.priceInterval");
        	//如果不是HTTP开头就拼接,是就不用拼接
        	if(!RULE_PARAM_HOST.startsWith("http")){
        		RULE_PARAM_HOST = RULE_SERVER_IP + RULE_PARAM_HOST;
        	}
        	LogUtil.info("RULE_PARAM_HOST=="+RULE_PARAM_HOST);
			String result = HttpSender.doPost(RULE_PARAM_HOST, JSONObject.fromObject(paramMap).toString());
			LogUtil.info("价格配置规则结果=="+result+"规则查询eqid="+taskId+"_"+inscomcode);
			return result;
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
        return null;
    }

    @Override
    public String getPriceIntervalByGZquery(String subtaskId, String taskId,
                                            String inscomcode, Double taxPrice, Double price,
                                            Double analogyTaxPrice, Double analogyPrice, String regDate,
                                            int seats,String agreementid) throws ServiceException {
        Map<String, Object> paramMap = new HashMap<String, Object>(88);
        String agreementtrule = null;
        if (StringUtils.isNotBlank(agreementid)) {
            agreementtrule = insbAgreementService.queryById(agreementid).getAgreementtrule();
            if (StringUtils.isNotBlank(agreementtrule)) {
                paramMap.putAll(getRuleInfoMap(agreementtrule));
            }
        }
        paramMap.put("car.specific.taxPrice", taxPrice);
        paramMap.put("car.specific.price", price);
        if (analogyTaxPrice != null)
            paramMap.put("car.specific.analogyTaxPrice", analogyTaxPrice);
        if (analogyPrice != null)
            paramMap.put("car.specific.analogyPrice", analogyPrice);
        paramMap.put("api.log.enquiry.id", taskId + "_" + inscomcode);// 任务ID
        paramMap.put("car.model.seats", seats);
        paramMap.put("car.specific.regDate", regDate);
        //Map<String, Object> ruleResult = null;
        LogUtil.info("规则平台查询入参 taskid=" + taskId + ",inscomcode=" + inscomcode + ",map=" + paramMap);
        try {
        	RULE_PARAM_HOST = ruleResourceBundle.getString("rule.handler.server.priceInterval");
        	//如果不是HTTP开头就拼接,是就不用拼接
        	if(!RULE_PARAM_HOST.startsWith("http")){
        		RULE_PARAM_HOST = RULE_SERVER_IP + RULE_PARAM_HOST;
        	}
        	LogUtil.info("RULE_PARAM_HOST=="+RULE_PARAM_HOST);
			String result = HttpSender.doPost(RULE_PARAM_HOST, JSONObject.fromObject(paramMap).toString());
			LogUtil.info("价格配置规则结果=="+result+"规则查询eqid="+taskId+"_"+inscomcode);
			return result;
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
        return null;
    }

    public String getThreadLocalVal() {
        return threadFlag.get();
    }
    public void setThreadLocalVal(String val) {
        threadFlag.set(val);
    }
    public void removeThreadLocalVal() {
        threadFlag.remove();
    }

    public ConfigInfo getConfigInfo(String inscomcode, String deptid, String processType) {
        try {
            ConfigInfo configInfo = new ConfigInfo();
            List<Map<String, Object>> resultInfo = insbPrvaccountkeyService.queryConfigInfo(deptid, inscomcode.substring(0, 4), "edi".equals(processType) ? "2" : "1");
            Map<String, String> dataMap = new HashMap<>();
            for(Map<String, Object> tempMap : resultInfo){
                String tempKey = String.valueOf(tempMap.get("paramname"));
                if(StringUtil.isNotEmpty(tempKey)){
                    dataMap.put(tempKey, String.valueOf(tempMap.get("paramvalue")));
                }
            }
            configInfo.setConfigMap(dataMap);
            return configInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取身份证前六位的户籍信息
    private String getCensusRegister(String certificateNum,Integer idCardType){
        if (StringUtil.isEmpty(certificateNum) || StringUtil.isEmpty(idCardType)) return "";
        if(0==idCardType && (certificateNum.length()==15 || certificateNum.length()==18)){ //类型为身份证且位数为15、18
            return certificateNum.substring(0,6);
        }
        return "";
    }
}
