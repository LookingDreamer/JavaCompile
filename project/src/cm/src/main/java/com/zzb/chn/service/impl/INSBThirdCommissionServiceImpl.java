package com.zzb.chn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.ModelUtil;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.bean.third.BusinessCusBean;
import com.zzb.chn.bean.third.CarInfoBean;
import com.zzb.chn.bean.third.InsureInfoBean;
import com.zzb.chn.bean.third.PersonBean;
import com.zzb.chn.bean.third.PolicyInfoBean;
import com.zzb.chn.bean.third.PolicySettleBean;
import com.zzb.chn.bean.third.RiskKindBean;
import com.zzb.chn.bean.third.ThirdCommissionBean;
import com.zzb.chn.bean.third.VvTaxBean;
import com.zzb.chn.dao.INSBYoubaoliancommissionDao;
import com.zzb.chn.dao.INSBYoubaolianprvmapDao;
import com.zzb.chn.entity.INSBYoubaoliancommission;
import com.zzb.chn.entity.INSBYoubaolianprvmap;
import com.zzb.chn.service.INSBThirdCommissionService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.extra.dao.INSBConditionsDao;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.model.WorkFlow4TaskModel;

@Service
@Transactional
public class INSBThirdCommissionServiceImpl implements INSBThirdCommissionService {
	private static Map<Integer, Integer> idCardTypeMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> carpropertyMap = new HashMap<Integer, Integer>();
	private static Map<String, Integer> prvIdMap = new HashMap<String, Integer>();
	private static Map<String, Integer> riskCodeMap = new HashMap<String, Integer>();
	private static Map<Integer, Integer> carVehicleOriginMap = new HashMap<Integer, Integer>();
	private static Map<String, Integer> firstinsuretypeMap = new HashMap<String, Integer>();
	
	private static ResourceBundle config = ResourceBundle.getBundle("config/config");
	
	static {
		idCardTypeMap.put(0, 1);
		idCardTypeMap.put(1, 10);
		idCardTypeMap.put(2, 12);
		idCardTypeMap.put(3, 17);
		idCardTypeMap.put(4, 2);
		idCardTypeMap.put(5, 16);
		idCardTypeMap.put(6, 25);
		idCardTypeMap.put(7, 99);
		idCardTypeMap.put(8, 8);
		idCardTypeMap.put(9, 21);
		idCardTypeMap.put(10, 24);
		idCardTypeMap.put(11, 13);
	}
	
	static {
		propertyMap.put(0, 1);
		propertyMap.put(1, 4);
		propertyMap.put(2, 8);
	}
	
	static {
		carpropertyMap.put(1, 1);
		carpropertyMap.put(2, 10);
		carpropertyMap.put(3, 11);
		carpropertyMap.put(4, 12);
		carpropertyMap.put(5, 3);
		carpropertyMap.put(6, 3);
		carpropertyMap.put(10, 2);
		carpropertyMap.put(11, 2);
		carpropertyMap.put(12, 2);
		carpropertyMap.put(15, 3);
		carpropertyMap.put(16, 2);
	}
	
	static {
		prvIdMap.put("2005", 9801);
		prvIdMap.put("2074", 9802);
		prvIdMap.put("2007", 9803);
		prvIdMap.put("2095", 9804);
		prvIdMap.put("2027", 9805);
		prvIdMap.put("2022", 9806);
		prvIdMap.put("2023", 9807);
		prvIdMap.put("2026", 9808);
		prvIdMap.put("2016", 9809);
		prvIdMap.put("2043", 9810);
		prvIdMap.put("2024", 9811);
		prvIdMap.put("2011", 9812);
		prvIdMap.put("2019", 9813);
		prvIdMap.put("2073", 9814);
		prvIdMap.put("2046", 9815);
		prvIdMap.put("2021", 9816);
		prvIdMap.put("2045", 9817);
		prvIdMap.put("2002", 9818);
		prvIdMap.put("2040", 9819);
		prvIdMap.put("2064", 9820);
		prvIdMap.put("2076", 9821);
		prvIdMap.put("2077", 9822);
		prvIdMap.put("2096", 9823);
		prvIdMap.put("2066", 9824);
		prvIdMap.put("2028", 9825);
		prvIdMap.put("2034", 9826);
		prvIdMap.put("2075", 9827);
		prvIdMap.put("2044", 9828);
		prvIdMap.put("2050", 9829);
		prvIdMap.put("2058", 9830);
		prvIdMap.put("2106", 9831);
		prvIdMap.put("2060", 9832);
		prvIdMap.put("2061", 9833);
		prvIdMap.put("4003", 9834);
		prvIdMap.put("2059", 9835);
		prvIdMap.put("2049", 9836);
		//prvIdMap.put("", 9837);
		prvIdMap.put("2039", 9838);
		prvIdMap.put("2065", 9839);
		prvIdMap.put("2051", 9840);
		prvIdMap.put("2062", 9841);
		prvIdMap.put("2101", 9842);
		prvIdMap.put("2029", 9843);
		prvIdMap.put("2086", 9844);
		//prvIdMap.put("", 9845);
		prvIdMap.put("2087", 9846);
		//prvIdMap.put("", 9847);
		prvIdMap.put("2068", 9848);
		prvIdMap.put("2090", 9849);
		prvIdMap.put("2103", 9850);
		prvIdMap.put("2057", 9851);
		prvIdMap.put("2056", 9852);
		//prvIdMap.put("", 9853);
		prvIdMap.put("3001", 9854);
		prvIdMap.put("2067", 9855);
		prvIdMap.put("2094", 9856);
		prvIdMap.put("2047", 9857);
		prvIdMap.put("2093", 9858);
		prvIdMap.put("2098", 9859);
		prvIdMap.put("2084", 9860);
		prvIdMap.put("2020", 9861);
		//prvIdMap.put("", 9862);
		prvIdMap.put("2082", 9863);
		prvIdMap.put("2042", 9864);
		prvIdMap.put("2088", 9865);
		prvIdMap.put("2100", 9866);
		prvIdMap.put("2071", 9867);
		prvIdMap.put("2079", 9868);
		prvIdMap.put("2041", 9869);
		prvIdMap.put("2016", 9870);
		prvIdMap.put("2054", 9871);
		prvIdMap.put("2080", 9872);
		prvIdMap.put("2044", 9873);
		prvIdMap.put("2058", 9874);
		prvIdMap.put("2085", 9875);
		prvIdMap.put("2055", 9876);
	}
	
	static {
		riskCodeMap.put("VehicleDemageIns", 10001);
		riskCodeMap.put("ThirdPartyIns", 10002);
		riskCodeMap.put("TheftIns", 10003);
		riskCodeMap.put("DriverIns", 10004);
		riskCodeMap.put("PassengerIns", 10005);
		riskCodeMap.put("VehicleCompulsoryIns", 10501);
		riskCodeMap.put("GlassIns", 20201);
		riskCodeMap.put("ScratchIns", 20202);
		riskCodeMap.put("CombustionIns", 20203);
		riskCodeMap.put("WadingIns", 20204);
		riskCodeMap.put("SpecifyingPlantCla", 20205);
		riskCodeMap.put("NewEquipmentIns", 20206);
		riskCodeMap.put("GoodsOnVehicleIns", 20207);
		riskCodeMap.put("CompensationForMentalDistressIns", 20208);
		riskCodeMap.put("CompensationDuringRepairIns", 20209);
		riskCodeMap.put("VehicleDemageMissedThirdPartyCla", 20210);
		riskCodeMap.put("SpecialVehicleExtensionIns", 20251);
		//riskCodeMap.put("", 20252);
		//riskCodeMap.put("", 20253);
	}
	
	static {
		carVehicleOriginMap.put(0, 0);
		carVehicleOriginMap.put(1, 2);
		carVehicleOriginMap.put(2, 1);
	}
	
	static {
		firstinsuretypeMap.put("非首次投保", 2);
		firstinsuretypeMap.put("新车首次投保", 1);
		firstinsuretypeMap.put("旧车首次投保", 32);
	}
	
	@Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
    private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
    private INSCCodeDao inscCodeDao;
	@Resource
    private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
    private INSBPersonDao insbPersonDao;
	@Resource
    private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
    private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBConditionsDao insbConditionsDao;
	@Resource
	private INSBYoubaoliancommissionDao insbYoubaoliancommissionDao;
	@Resource
	private INSBYoubaolianprvmapDao insbYoubaolianprvmapDao;

	public static void main(String[] args) throws Exception {
		String key = "l5Sl6GkLGNMe";
		String data = "{\"provinceCode\":null}";
        
        String url = "http://to.youbaolian.com" + "/api/basedata";
        Map<String, String> params = new HashMap<String, String>();
        params.put("version", "1.0.0");
        params.put("partnerId", "7502");
        params.put("etype", "1");
        params.put("data", data);
        params.put("reqhash", StringUtil.md5Hex(data + key));
        params.put("datatype", "JSON");
        params.put("action", "cityquery");
        
        String result = HttpClientUtil.doPost(url, params);
        
        System.out.println(result); 
    }
	
	@Override
	public void thirdCommission(QuoteBean quoteBean, INSBQuotetotalinfo insbQuotetotalinfo) {
		
	}

	@Override
	public void thirdCommission(WorkFlow4TaskModel dataModel, INSBQuotetotalinfo insbQuotetotalinfo) throws Exception {
		String taskId = insbQuotetotalinfo.getTaskid();
		String prvId = dataModel.getProviderId();
		String city = insbQuotetotalinfo.getInscitycode();
		//String taskCode = dataModel.getTaskCode();
		//if (!CHNChannelService.INSURE_SUCCESS_TASKCODE.equals(taskCode)) {
		//	return;
		//}
		
		//INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		//quoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		//quoteinfo.setInscomcode(prvId);
        //quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		
        //INSCDept inscDept = inscDeptService.queryById(quoteinfo.getDeptcode());
        //String[] parentcodes = inscDept.getParentcodes().split("[+]");
        //LogUtil.info(taskId + "thirdCommission-parentcodes[3]:" + parentcodes[3]);
        //if ( !CHNChannelService.HEBEI_DEPTCODE.equals(parentcodes[3]) ) { //1213000000：河北平台
        //	return;
        //}
        
        ThirdCommissionBean thirdCommissionBean = new ThirdCommissionBean();
        thirdCommissionBean.setOutCheckedId(taskId); 
        thirdCommissionBean.setAreaCode(city);
        thirdCommissionBean.setCheckState(12); 
        thirdCommissionBean.setPrvId(prvIdMap.get(prvId.substring(0, 4)));
        thirdCommissionBean.setPayType(1); 
        
        BusinessCusBean businessCusBean = new BusinessCusBean();
        businessCusBean.setOutId(insbQuotetotalinfo.getAgentnum());
        businessCusBean.setName(insbQuotetotalinfo.getAgentname()); 
        businessCusBean.setPolicyLevel(0); 
        thirdCommissionBean.setBusinessCus(businessCusBean); 
        
        AgentTaskModel agentTaskModel = new AgentTaskModel();
        agentTaskModel.setTaskid(taskId); 
        Map<String, Object> lastInserMap = insbConditionsDao.queryLastInsertInfo(agentTaskModel);
        String firstInsureType = (String)lastInserMap.get("firstInsureType");
        Object compulsoryClaimTimesObj = lastInserMap.get("compulsoryClaimTimes");
        Long compulsoryClaimTimes = 0L;
        if (compulsoryClaimTimesObj != null) {
        	compulsoryClaimTimes = Long.parseLong(compulsoryClaimTimesObj.toString()); 
        }
        
        INSBPolicyitem pitem = new INSBPolicyitem();
        pitem.setTaskid(taskId);
        pitem.setInscomcode(prvId);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(pitem);

        INSBCarkindprice carprice = new INSBCarkindprice();
        carprice.setTaskid(taskId);
        carprice.setInscomcode(prvId);
        List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carprice); //险种列表
        
        List<InsureInfoBean> insureInfos = new ArrayList<InsureInfoBean>();
        if (policyitemList != null && policyitemList.size() > 0) {
            double efcAmount = 0.0000d; //交强险保额
            double efcPremium = 0.0000d; //交强险保费
            double taxAmount = 0.0000d; //车船税金额
            double taxLateFee = 0.0000d; //车船税滞纳金
            VvTaxBean vvTaBean = null; //车船税
            
            for (INSBPolicyitem policyitem : policyitemList) {
                if ("1".equals(policyitem.getRisktype())) { //交强险
                    InsureInfoBean efcInsureInfoBean = new InsureInfoBean();
                    if (policyitem.getStartdate() != null) {
                    	efcInsureInfoBean.setStartDate(ModelUtil.conbertToString(policyitem.getStartdate()));
                    }
                    
                    for (INSBCarkindprice carkindprice : carkindpriceList) {
                        if ("2".equals(carkindprice.getInskindtype())) {
                            if (carkindprice.getAmount() != null) {
                                efcAmount += douFormat(carkindprice.getAmount());
                            }
                            if (carkindprice.getDiscountCharge() != null) {
                                efcPremium += douFormat(carkindprice.getDiscountCharge());
                            }
                        } else if ("3".equals(carkindprice.getInskindtype())) {
                            if (null == vvTaBean) vvTaBean = new VvTaxBean();
                            if ("VehicleTax".equals(carkindprice.getInskindcode())) { //车船税金额
                                if (carkindprice.getDiscountCharge() != null) {
                                    taxAmount += douFormat(carkindprice.getDiscountCharge());
                                }
                            } else { //车船税滞纳金
                                if (carkindprice.getDiscountCharge() != null) {
                                    taxLateFee += douFormat(carkindprice.getDiscountCharge());
                                }
                            }
                        }
                    }
                    
                    if (vvTaBean != null) { //车船税
                    	vvTaBean.setVvTaxAmount(taxAmount);
                    	vvTaBean.setLaterFee(taxLateFee);
                    	thirdCommissionBean.setVvTax(vvTaBean);
                    }
                    
                    //efcInsureInfoBean.setPremium(efcPremium);
                    efcInsureInfoBean.setPremium(policyitem.getTotalepremium());
                    efcInsureInfoBean.setDiscountRate(policyitem.getDiscountRate());
                    efcInsureInfoBean.setInsuranceSlipNo(policyitem.getProposalformno()); 
                    efcInsureInfoBean.setInsuranceType(11);
                    
                    efcInsureInfoBean.setBusinessType(firstinsuretypeMap.get(firstInsureType)); 
                    //efcInsureInfoBean.setPrevDamagedCI(prevDamagedCI);
                    efcInsureInfoBean.setPrevUsedCount(compulsoryClaimTimes.intValue());
                    if (efcInsureInfoBean.getPrevUsedCount() == -1) {
                    	efcInsureInfoBean.setPrevUsedCount(0);
                    }
                    
                    insureInfos.add(efcInsureInfoBean); 
                    
                } else { //商业险
                    getBizInsureInfo(insureInfos, policyitem, carkindpriceList, null, lastInserMap);
                }

            }
        }
        thirdCommissionBean.setInsureInfo(insureInfos);
        
        INSBYoubaolianprvmap prvmapCon = new INSBYoubaolianprvmap();
        prvmapCon.setPrvId(prvId);
        List<INSBYoubaolianprvmap> prvmaps = insbYoubaolianprvmapDao.selectList(prvmapCon);
        if (!prvmaps.isEmpty()) {
        	INSBYoubaolianprvmap prvmap = prvmaps.get(0);
        	PolicyInfoBean policyInfoBean = new PolicyInfoBean();
        	policyInfoBean.setSupplierId(prvmap.getSupplierId());
        	policyInfoBean.setSupplierName(prvmap.getSupplierName()); 
        	policyInfoBean.setPrvAccountName(prvmap.getPrvAccountName());
        	policyInfoBean.setSumBIIntegral(0.0);
        	policyInfoBean.setSumCIIntegral(0.0);
        	thirdCommissionBean.setPolicyInfo(policyInfoBean);
        	
        	thirdCommissionBean.setOutPurchaseOrgId(prvmap.getOutPurchaseOrgId());
        	thirdCommissionBean.setOutPurchaseOrgName(prvmap.getOutPurchaseOrgName());
        }
        
        this.getApplicantPerson(thirdCommissionBean, taskId);
        this.getInsuredPerson(thirdCommissionBean, taskId);
        this.getCarInfo(thirdCommissionBean, taskId, prvId); 
        
        String url = config.getString("youbaolian.url") + "/api/ins";
        String key = config.getString("youbaolian.key");
        String partnerId = config.getString("youbaolian.partnerId");
		String data = JsonUtils.serialize(thirdCommissionBean);
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("version", "1.0.0");
        params.put("partnerId", partnerId);
        params.put("etype", "1");
        params.put("data", data);
        params.put("reqhash", StringUtil.md5Hex(data + key));
        params.put("datatype", "JSON");
        params.put("action", "checkins");
        
        LogUtil.info(key + taskId + "thirdCommission-send:" + params + url);
        String result = HttpClientUtil.doPost(url, params);
        LogUtil.info(taskId + "thirdCommission-result:" + result);
        
        ThirdCommissionBean retBean = JsonUtils.deserialize(result, ThirdCommissionBean.class); 
        INSBYoubaoliancommission insbYoubaoliancommission = new INSBYoubaoliancommission();
        
        insbYoubaoliancommission.setTaskId(taskId); 
        insbYoubaoliancommission.setCheckedId(retBean.getCheckedId());
        insbYoubaoliancommission.setCheckedState(retBean.getCheckedState());
        insbYoubaoliancommission.setCode(retBean.getCode());
        insbYoubaoliancommission.setCreatetime(new Date());
        insbYoubaoliancommission.setMsg(retBean.getMsg());
        insbYoubaoliancommission.setRet(retBean.getRet());
        
        PolicySettleBean policySettleBean = retBean.getPolicySettle();
        if (policySettleBean != null) {
	        insbYoubaoliancommission.setBiSettlePoint(policySettleBean.getBiSettlePoint());
	        insbYoubaoliancommission.setCiSettlePoint(policySettleBean.getCiSettlePoint());
	        insbYoubaoliancommission.setPlatformAccount(policySettleBean.getPlatformAccount());
	        insbYoubaoliancommission.setPlatformSettleFee(policySettleBean.getPlatformSettleFee());
	        insbYoubaoliancommission.setSalesmanSettleFee(policySettleBean.getSalesmanSettleFee());
	        insbYoubaoliancommission.setSupplierAccount(policySettleBean.getSupplierAccount());
	        insbYoubaoliancommission.setSupplierSettleFee(policySettleBean.getSupplierSettleFee());
	        insbYoubaoliancommission.setVvTaxSettlePoint(policySettleBean.getVvTaxSettlePoint());
        }
        
        insbYoubaoliancommissionDao.insert(insbYoubaoliancommission);
	}
	
	//查询商业险信息
    private void getBizInsureInfo(List<InsureInfoBean> insureInfos, INSBPolicyitem policyitem,
                                  List<INSBCarkindprice> carkindpriceList,
                                  List<INSBSpecialkindconfig> specialkindconfigList, Map<String, Object> lastInserMap) {
    	Object commercialClaimTimesObj = lastInserMap.get("commercialClaimTimes");
        Long commercialClaimTimes = 0L;
        if (commercialClaimTimesObj != null) {
        	commercialClaimTimes = Long.parseLong(commercialClaimTimesObj.toString()); 
        }
    	String firstInsureType = (String)lastInserMap.get("firstInsureType");
    	
        //获取商业险特殊险别
        List<Map<String, Object>> codeList = inscCodeDao.selectByType("riskkindconfig");
        List<String> riskKindConfigList = new ArrayList<String>();
        for (Map<String, Object> map : codeList) {
            riskKindConfigList.add((String) map.get("codename"));
        }
        
        double biznfcPremium = 0.0000d; //商业险不计免赔保费
        double bizPremium = 0.0000d; //商业总险保费
        InsureInfoBean bizInsureInfoBean = null; //商业险

        List<RiskKindBean> riskKinds = null;
        for (INSBCarkindprice carkindprice : carkindpriceList) {
            // 0 表示商业险 1 表示不计免赔
            if ("0".equals(carkindprice.getInskindtype()) || "NcfBasicClause".equals(carkindprice.getInskindcode()) ||
                    "Ncfaddtionalclause".equals(carkindprice.getInskindcode()) || "NcfClause".equals(carkindprice.getInskindcode())||
                    "NcfDriverPassengerIns".equals(carkindprice.getInskindcode())) {
                if (bizInsureInfoBean == null) { //商业险
                    bizInsureInfoBean = new InsureInfoBean();
                    riskKinds = new ArrayList<RiskKindBean>();
                }
                RiskKindBean riskKindBean = new RiskKindBean();
                if (carkindprice.getInskindcode().equals("Ncfaddtionalclause")) {
                    riskKindBean.setRiskCode(riskCodeMap.get("NcfAddtionalClause"));
                } else {
                    riskKindBean.setRiskCode(riskCodeMap.get(carkindprice.getInskindcode()));
                }
                
                boolean isSpecialRisk = false; //是否特殊险别
                for (String riskKindConfig : riskKindConfigList) {
                    if (riskKindConfig.equals(riskKindBean.getRiskCode())) {
                        isSpecialRisk = true;
                        break;
                    }
                }

                if (isSpecialRisk) {
                    //特殊险别的情况暂不考虑
                } else { //非特殊险别
                    if (carkindprice.getAmount() != null) {
                        riskKindBean.setAmount(carkindprice.getAmount());
                    }
                    if (carkindprice.getDiscountCharge() != null) {
                        if(carkindprice.getInskindcode().equals("NcfBasicClause")||carkindprice.getInskindcode().equals("Ncfaddtionalclause")
                                ||carkindprice.getInskindcode().equals("NcfClause")||carkindprice.getInskindcode().equals("NcfDriverPassengerIns")){

                            biznfcPremium += douFormat(carkindprice.getDiscountCharge());
                        }else {
                            bizPremium += douFormat(carkindprice.getDiscountCharge());
                        }
                        riskKindBean.setPremium(carkindprice.getDiscountCharge());

                    }
                    riskKindBean.setNotDeductible(false);
                    
                    //不计免赔
                    for (INSBCarkindprice ncfKind : carkindpriceList) {
                        if ("1".equals(ncfKind.getInskindtype())
                                && carkindprice.getInskindcode().equals(ncfKind.getPreriskkind())) {
                        	
                            riskKindBean.setNotDeductible(true);
                            if (ncfKind.getDiscountCharge() != null && douFormat(ncfKind.getDiscountCharge()) > 0) {
                                riskKindBean.setNcfPreminm(ncfKind.getDiscountCharge());
                                biznfcPremium += douFormat(ncfKind.getDiscountCharge());
                            } else {
                                riskKindBean.setNcfPreminm(0.0000);
                            }
                            break;
                        }
                    }

                    riskKinds.add(riskKindBean);
                }
            }
        }

        //商业险
        if (bizInsureInfoBean != null) {
            if (policyitem.getStartdate() != null) {
                bizInsureInfoBean.setStartDate(ModelUtil.conbertToString(policyitem.getStartdate()));
            }
            
            //if (null != policyitem.getDiscountCharge())
            //    bizInsureInfoBean.setPremium(bizPremium + biznfcPremium);
            bizInsureInfoBean.setPremium(policyitem.getTotalepremium());
            bizInsureInfoBean.setDiscountRate(policyitem.getDiscountRate());
            bizInsureInfoBean.setInsuranceSlipNo(policyitem.getProposalformno());
            bizInsureInfoBean.setInsuranceType(10);
            bizInsureInfoBean.setRiskKinds(riskKinds);
            
            bizInsureInfoBean.setBusinessType(firstinsuretypeMap.get(firstInsureType));
            //bizInsureInfoBean.setPrevDamagedCI(prevDamagedCI);
            bizInsureInfoBean.setPrevUsedCount(commercialClaimTimes.intValue());
            if (bizInsureInfoBean.getPrevUsedCount() == -1) {
            	bizInsureInfoBean.setPrevUsedCount(0);
            }
            
            insureInfos.add(bizInsureInfoBean);
        }
    }
    
    //获取车辆和车主
    private void getCarInfo(ThirdCommissionBean thirdCommissionBean, String taskId, String comcode) {
        INSBCarinfohis his = new INSBCarinfohis();
        his.setTaskid(taskId);
        his.setInscomcode(comcode);
        INSBCarinfohis carinfohis = insbCarinfohisDao.selectOne(his);
        CarInfoBean carInfoBean = new CarInfoBean();
        PersonBean carOwnerBean = new PersonBean();
        
        if (null != carinfohis) {
        	carInfoBean.setAutoModelName(carinfohis.getStandardfullname()); 
        	carInfoBean.setUsageAttribute(carpropertyMap.get(carinfohis.getCarproperty()));
        	carInfoBean.setOwnershipAttribute(propertyMap.get(carinfohis.getProperty())); 
            carInfoBean.setLicenceNo(carinfohis.getCarlicenseno());
            carInfoBean.setFrameNo(carinfohis.getVincode());
            carInfoBean.setEngineNo(carinfohis.getEngineno());
            carInfoBean.setRegisterDate(ModelUtil.conbertToString(carinfohis.getRegistdate())); 
            carInfoBean.setTransferCar(Integer.valueOf(carinfohis.getIsTransfercar())); 
            if ("1".equals(carInfoBean.getTransferCar())) {
            	carInfoBean.setTransferDate(ModelUtil.conbertToString(carinfohis.getTransferdate())); 
            }
            
            INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
            insbCarmodelinfohis.setCarinfoid(carinfohis.getId());
            INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
            
            if (carmodelinfohis == null) {
            	Map<String, Object> mapCond = new HashMap<String, Object>();
            	mapCond.put("taskid", taskId);
            	mapCond.put("inscomcode", comcode);
            	carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(mapCond);
            }
            
            if (null != carmodelinfohis) {
            	carInfoBean.setSeats(carmodelinfohis.getSeat());
            	if (carmodelinfohis.getLoads() != null) {
            		carInfoBean.setVehicleTonnages(carmodelinfohis.getLoads().toString());
            	}
            	carInfoBean.setExhaustCapability(carmodelinfohis.getDisplacement());
            	carInfoBean.setCarPrice(carmodelinfohis.getPrice());
            	carInfoBean.setNegotiatedPrice(carmodelinfohis.getPrice());
            	carInfoBean.setImportedCar(carVehicleOriginMap.get(carmodelinfohis.getCarVehicleOrigin()));
            	carInfoBean.setVehicleClassCode(getVehicleClassCode(carmodelinfohis.getSyvehicletypename()));
            	if (StringUtil.isNotEmpty(carmodelinfohis.getStandardfullname())) {
            		carInfoBean.setAutoModelName(carmodelinfohis.getStandardfullname());
            	}
            }
            thirdCommissionBean.setCarInfo(carInfoBean); 

            INSBPerson insbPerson = insbPersonDao.selectById(carinfohis.getOwner());
            List<PersonBean> persons = thirdCommissionBean.getPersons();
            if (null != insbPerson) {
            	if (persons == null) persons =  new ArrayList<PersonBean>();
            	carOwnerBean.setName(insbPerson.getName());
            	carOwnerBean.setIdcardNo(insbPerson.getIdcardno());
                carOwnerBean.setIdcardType(idCardTypeMap.get(insbPerson.getIdcardtype()));
                carOwnerBean.setMobile(insbPerson.getCellphone());
                carOwnerBean.setRole("车主");
                
                persons.add(carOwnerBean);
                thirdCommissionBean.setPersons(persons);                   
            }
        } 

    }

    //被保人
    private void getInsuredPerson(ThirdCommissionBean thirdCommissionBean, String taskId) {
        INSBPerson insbInsured = insbPersonDao.selectInsuredPersonByTaskId(taskId);
        List<PersonBean> persons = thirdCommissionBean.getPersons();
        
        PersonBean personBean = new PersonBean();
        if (insbInsured != null) {
        	if (persons == null) persons =  new ArrayList<PersonBean>();
        	personBean.setName(insbInsured.getName());
        	personBean.setIdcardNo(insbInsured.getIdcardno());
            personBean.setIdcardType(idCardTypeMap.get(insbInsured.getIdcardtype()));
            personBean.setMobile(insbInsured.getCellphone());
            personBean.setRole("被保人");
            
            persons.add(personBean);
            thirdCommissionBean.setPersons(persons); 
        } 
    }
    
    //投保人信息
    private void getApplicantPerson(ThirdCommissionBean thirdCommissionBean, String taskId) {
        INSBPerson applicant = insbPersonDao.selectApplicantPersonByTaskId(taskId);
        List<PersonBean> persons = thirdCommissionBean.getPersons();
        
        PersonBean personBean = new PersonBean();
        if (applicant != null) {
        	if (persons == null) persons =  new ArrayList<PersonBean>();
        	personBean.setName(applicant.getName());
            personBean.setIdcardNo(applicant.getIdcardno());
            personBean.setIdcardType(idCardTypeMap.get(applicant.getIdcardtype()));
            personBean.setMobile(applicant.getCellphone());
            personBean.setRole("投保人");
            
            persons.add(personBean);
            thirdCommissionBean.setPersons(persons); 
        } 
    }
	
	private double douFormat(Double target) {
        double tar = 0d;
        if (target != null) {
            tar = target.doubleValue();
        }
        return tar;
    }
	
	private Integer getVehicleClassCode(String syvehicletypename) {
		Integer result = null;
		if (StringUtil.isEmpty(syvehicletypename)) return 99;
		
		if (syvehicletypename.contains("客车")) {
			result = 1;
		} else if (syvehicletypename.contains("货车")) {
			result = 2;
		} else if (syvehicletypename.contains("挂车")) {
			result = 3;
		} else if (syvehicletypename.contains("出租车")) {
			result = 5;
		} else if (syvehicletypename.contains("摩托车")) {
			result = 8;
		} else if (syvehicletypename.contains("拖拉机")) {
			result = 9;
		} else if (syvehicletypename.contains("农用车")) {
			result = 9;
		} else if (syvehicletypename.contains("自卸车")) {
			result = 10;
		} else if (syvehicletypename.contains("低速")) {
			result = 11;
		} else if (syvehicletypename.contains("特种车一")) {
			result = 21;
		} else if (syvehicletypename.contains("特种车二")) {
			result = 22;
		} else if (syvehicletypename.contains("特种车三")) {
			result = 23;
		} else if (syvehicletypename.contains("特种车四")) {
			result = 24;
		} else if (syvehicletypename.contains("特种车")) {
			result = 7;
		} else {
			result = 99;
		} 
		
		return result;
	}
	
}
