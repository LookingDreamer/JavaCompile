package com.zzb.cm.Interface.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.WorkflowFeedbackUtil;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.cm.Interface.service.CoreInterFaceService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCorecodemap;
import com.zzb.cm.entity.INSBCoreriskmap;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBSaveerror;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCorecodemapService;
import com.zzb.cm.service.INSBCoreriskmapService;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.cm.service.INSBInsuredService;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBLastyearinsureinfoService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBSaveerrorService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBRiskkindconfigService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.conf.service.INSBWorkflowsubtrackdetailService;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.model.WorkFlow4TaskModel;

@Service
@Transactional
public class CoreInterFaceServiceImpl implements CoreInterFaceService {

	@Resource 
	private INSBOrderService insbOrderService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarinfohisService insbCarinfohisService;
	@Resource
	private INSBCarmodelinfohisService insbCarmodelinfohisService;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBCorecodemapService insbCorecodemapService;
	@Resource
	private INSBCoreriskmapService insbCoreriskmapService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBInsuredhisService insbInsuredhisService;
	@Resource
	private INSBInsuredService insbInsuredService;
	@Resource
	private INSBPersonService insbpersonService;
	@Resource
	private INSBSaveerrorService insbSaveerrorService;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBRiskkindconfigService insbriskkindconfigService;
	@Resource
	private INSBLastyearinsureinfoService insblastyearinsureinfoService;
	@Resource
	private INSBFlowerrorService insbFlowerrorService;
	@Resource
	private InterFaceService interFaceService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBWorkflowsubtrackdetailService insbWorkflowsubtrackdetailService;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	private CHNChannelService  chnChannelService ;
	@Resource
	private INSBAccountDetailsService  insbAccountDetailsService ;
	@Resource
	private INSBOrderpaymentService  insbOrderpaymentService ;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao ;
	@Resource
	private INSBAgreementDao insbAgreementDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Override
	public Map<String, Object> insurance(String orgCode, String companyId,
			String policyCode, String plateNum, String insuredName) {
		LogUtil.info("进入核心对接接口：policyCode="+policyCode+",orgCode="+orgCode+",companyId="+companyId+",plateNum="+plateNum+",insuredName="+insuredName);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Map<String,Object> agentInfo,item,suiteCodeAndSuiteList;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"),
				sdf2=new SimpleDateFormat("HH:mm:ss"),
				sdf3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		INSBOrder order;
		INSBCorecodemap insbCorecodemap;
		INSBQuoteinfo insbQuoteinfo;
		//查询法人机构 
		INSCDept legalPersonDept = inscDeptService.getLegalPersonDept(orgCode);
		HashMap<String, Object> param = new HashMap<String,Object>();
		param.put("deptcode", legalPersonDept.getId().substring(0, legalPersonDept.getId().indexOf("0")));//法人机构编码
		param.put("prvid", companyId);//保险公司编码（部分）
		if(!StringUtil.isEmpty(policyCode))param.put("policyno", policyCode);//保单号
		if(!StringUtil.isEmpty(plateNum))param.put("carlicenseno", plateNum);//车牌号
		if(!StringUtil.isEmpty(insuredName))param.put("insuredname", insuredName);//投保人姓名
		List<INSBPolicyitem> insbPolicyitemList1=insbPolicyitemService.getListByParam(param);
		List<INSBPolicyitem> insbPolicyitemList=new ArrayList<INSBPolicyitem>();
		for (INSBPolicyitem insbPolicyitem : insbPolicyitemList1) {
			if(StringUtil.isEmpty(insbPolicyitem.getInscomcode())||insbPolicyitem.getInscomcode().indexOf(companyId)==0)
				insbPolicyitemList.add(insbPolicyitem);
		}
		for (INSBPolicyitem policyitem : insbPolicyitemList) {
			order = new INSBOrder();
			order.setTaskid(policyitem.getTaskid());
			order.setPrvid(companyId);
			List<INSBOrder> listByPrvidLike = insbOrderService.getListByPrvidLike(order);
			if(listByPrvidLike.size()==0)continue;
			order=listByPrvidLike.get(0);
			item = new HashMap<String, Object>();
			item.put("policyNum", StringUtil.isEmpty(policyitem.getPolicyno())?"":policyitem.getPolicyno());//保单号
			item.put("proposeNum", StringUtil.isEmpty(policyitem.getProposalformno())?"":policyitem.getProposalformno());//投保单号
			//item.put("charge",BigDecimal.valueOf(policyitem.getPremium()==null?0.00:policyitem.getPremium()).setScale(2, RoundingMode.HALF_UP).toString());//保费
			item.put("insuredPersonInfo", this.getInsuredPersonInfo(policyitem.getTaskid(),order.getPrvid(),insuredName));//被保人
			insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(policyitem.getTaskid(), order.getPrvid());
			INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
			insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
			insbWorkflowsubtrack.setTaskcode("21");
			insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
			if(insbWorkflowsubtrack==null){
				insbWorkflowsubtrack = new INSBWorkflowsubtrack();
				insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
				insbWorkflowsubtrack.setTaskcode("20");
				insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
			}
			if(insbWorkflowsubtrack==null){
				item.put("issuingDate", "");//出单日期(年月日)
				item.put("issuingTime", "");//出单时间（时分秒）
				
			}else{
				item.put("issuingDate", StringUtil.isEmpty(insbWorkflowsubtrack.getModifytime())?"":sdf1.format(insbWorkflowsubtrack.getModifytime()));//出单日期(年月日)
				item.put("issuingTime", StringUtil.isEmpty(insbWorkflowsubtrack.getModifytime())?"":sdf2.format(insbWorkflowsubtrack.getModifytime()));//出单时间（时分秒）
				
			}
			item.put("recordingDate", StringUtil.isEmpty(policyitem.getInsureddate())?"":sdf1.format(policyitem.getInsureddate()));//签单日期
			item.put("printDate", StringUtil.isEmpty(policyitem.getInsureddate())?"":sdf1.format(policyitem.getInsureddate()));//打印日期
			
			suiteCodeAndSuiteList = this.getSuiteCodeAndSuiteList(policyitem.getTaskid(),order.getPrvid(),policyitem.getRisktype());
			item.put("suiteCode", suiteCodeAndSuiteList.get("suiteCode"));//险种代码
			item.put("suite", suiteCodeAndSuiteList.get("suite"));//险种
			item.put("charge",suiteCodeAndSuiteList.get("totalCharge"));//总保费
			
			item.put("bizTransactionId", policyitem.getTaskid()+"@"+order.getPrvid());// taskid+@+保险公司代码
			item.put("agencyOrgList", this.getDeptTree(policyitem.getTaskid(),order.getPrvid()));//出单网点机构列表
			agentInfo = new HashMap<String,Object>();
			agentInfo.put("agentCode", StringUtil.isEmpty(order.getAgentcode())?"":order.getAgentcode());//代理人编码
			agentInfo.put("agentName", StringUtil.isEmpty(order.getAgentname())?"":order.getAgentname());//代理人姓名
			item.put("agentInfo", agentInfo);//代理人信息
			item.put("insCorpCode", StringUtil.isEmpty(order.getPrvid())?"":order.getPrvid());//投保保险公司代码
			item.put("effectiveDate", StringUtil.isEmpty(policyitem.getStartdate())?"":sdf3.format(policyitem.getStartdate()));//起保日期
			item.put("expiryDate", StringUtil.isEmpty(policyitem.getEnddate())?"":sdf3.format(policyitem.getEnddate()));//检验有效日期
			Map<String, Object> carInfo = this.getCarInfo(policyitem.getTaskid(),order.getPrvid());
			item.put("carInfo", carInfo.get("carInfo"));//车辆信息
			item.put("accFrequency", carInfo.get("accFrequency"));//出险次数
			
			if(!StringUtil.isEmpty(insbQuoteinfo)&&!StringUtil.isEmpty(insbQuoteinfo.getBuybusitype())){
				insbCorecodemap=new INSBCorecodemap();
				insbCorecodemap.setType("saletype");//销售方式
				insbCorecodemap.setCmcode(insbQuoteinfo.getBuybusitype());
				insbCorecodemap=insbCorecodemapService.queryList(insbCorecodemap).get(0);
				item.put("saleWay", StringUtil.isEmpty(insbCorecodemap)?"":insbCorecodemap.getCorecode());//销售方式
			}else{
				item.put("saleWay", "");//销售方式
			}
			BigDecimal taxCharge = this.getTaxCharge(policyitem.getTaskid(),order.getPrvid());
			item.put("taxCharge", StringUtil.isEmpty(taxCharge)||"0".equals(policyitem.getRisktype())?"":taxCharge.setScale(2, RoundingMode.HALF_UP).toString());//车船税

			item.put("buyRist",this.getBuyRist(policyitem.getTaskid(),policyitem.getInscomcode()));//购买险种
			Map<String, String> custLoyalty = this.getCustLoyalty(policyitem.getTaskid(),policyitem.getInscomcode());
			item.put("custLoyalty",custLoyalty.get("custLoyalty"));//客户忠诚度
			item.put("payoutMoney",custLoyalty.get("payoutMoney"));//赔款金额
			if("0".equals(policyitem.getRisktype()))
				item.put("payoutRate",custLoyalty.get("sypayoutRate"));//赔付率
			else
				item.put("payoutRate",custLoyalty.get("jqpayoutRate"));//赔付率
				
			item.put("accFrequencysy",custLoyalty.get("accFrequencysy"));//商业出险次数
			item.put("accFrequencyjq",custLoyalty.get("accFrequencyjq"));//交强出险次数
			item.put("discount",StringUtil.isEmpty(policyitem.getDiscountRate())?"":policyitem.getDiscountRate().toString());//赔款金额
			item.put("tax3","");//增值税
			res.add(item);
		}
		
		map.put("cnt", String.valueOf(res.size()));//条数
		map.put("res", res);
		LogUtil.info("关闭核心对接接口");
		return map;
	}
	private Map<String,Object> getInsuredPersonInfo(String taskid, String companyId,
			String insuredName) {
		Map<String,Object> map = new HashMap<String, Object>();
        map.put("certKind", "");//被保人证件类型
        map.put("fullName","");//被保人全名
        map.put("certNumber", "");//被保人证件号
		INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
		insbInsuredhis.setTaskid(taskid);
		insbInsuredhis.setInscomcode(companyId);
		List<INSBInsuredhis> insbInsuredhisList = insbInsuredhisService.queryList(insbInsuredhis);
		if(insbInsuredhisList.size()==0){
			INSBInsured insbInsured = new INSBInsured();
			insbInsured.setTaskid(taskid);
			List<INSBInsured> insbInsuredList = insbInsuredService.queryList(insbInsured);
			if(insbInsuredList.size()==0)return map;
			insbInsuredhisList = new ArrayList<INSBInsuredhis>();
			for (INSBInsured insbInsured2 : insbInsuredList) {
				try {
					insbInsuredhis = new INSBInsuredhis();
					PropertyUtils.copyProperties(insbInsuredhis, insbInsured2);
					insbInsuredhisList.add(insbInsuredhis);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口车辆信息获得出错1");
					e.printStackTrace();
					return map;
				}
			}
		}
		INSBCorecodemap insbCorecodemap;
		INSBPerson insbPerson=null;
		for (INSBInsuredhis insbInsuredhis2 : insbInsuredhisList) {
			if(insbInsuredhis2.getPersonid()!=null){
				insbPerson = insbpersonService.queryById(insbInsuredhis2.getPersonid());
				if(insbPerson==null||(!StringUtil.isEmpty(insuredName)&&!insuredName.equals(insbPerson.getName())))continue;
				if(!StringUtil.isEmpty(insbPerson.getIdcardtype())){
					insbCorecodemap= new INSBCorecodemap();
					insbCorecodemap.setCmcode(String.valueOf(insbPerson.getIdcardtype()));
					insbCorecodemap.setType("idcardtype");
					List<INSBCorecodemap> queryList = insbCorecodemapService.queryList(insbCorecodemap);
					map.put("certKind", queryList.size()==0?"":queryList.get(0).getCorecode());//被保人证件类型
				}else
					map.put("certKind","");
				map.put("fullName", StringUtil.isEmpty(insbPerson.getName())?"":insbPerson.getName());//被保人全名
				map.put("certNumber", StringUtil.isEmpty(insbPerson.getIdcardno())?"":insbPerson.getIdcardno());//被保人证件号
			}
		}
//		if(insbPerson==null){
//			map.put("certKind", "");//被保人证件类型
//			map.put("fullName","");//被保人全名
//			map.put("certNumber", "");//被保人证件号
//		}
		return map;
	}
	private BigDecimal getTaxCharge(String taskid,String companyId) {

		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
		insbCarkindprice.setTaskid(taskid);//任务id
		insbCarkindprice.setInscomcode(companyId);//保险公司编码
		insbCarkindprice.setInskindcode("VehicleTax");
		List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);
		if(resultInsbCarkindpriceList.size()<=0)return null;
		Double discountCharge = resultInsbCarkindpriceList.get(0).getDiscountCharge();//折后保费
		return BigDecimal.valueOf(discountCharge==null?0.00:discountCharge).setScale(2, RoundingMode.HALF_UP);
	}
	private Map<String,Object> getCarInfo(String taskId, String insurancecompanyid){
		/**
		 *  车辆信息保存逻辑：用户首次录入的车辆信息保存在insbcarinfo中，
		 * 当用户选择完毕报价的保险公司后，并且修改了对应某一家保险公司的车辆信息，则修改后的数据保存在insbcarinfohis中；
		 * 获取这些数据需要根据taskid和companyid获得。
		 * @author zhangdi
		 * @@获取车辆信息的时候先去insbcarinfohis中查询，如果没有查到结果，则去insbcarinfo中查询。
		 */
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		//初始化数据开始
		map.put("displacement", "");
		map.put("carModelName", "");
		map.put("taxPrice", "");
		map.put("seatCnt", "");
		map.put("fullLoad", "");
		map.put("carKindCode", "");
		map.put("plateNum","");
		map.put("useProps","");
		map.put("userType", "");
		map.put("firstRegDate", "");
		map.put("vin", ""); 
		map.put("engineNum", "");
		map.put("plateType",""); 
		map.put("avgMileage", ""); 
		map.put("carDrivingArea", ""); 
		map.put("bodyColor", ""); 
		map.put("plateColor", ""); 
		map.put("useAge", ""); 
		map.put("transferVehicle", "");
		map1.put("accFrequency", "");
		map1.put("carInfo", map);

		//初始化数据结束
		
		//查询车辆表数据开始
		INSBCarinfohis insbCarinfoHis = new INSBCarinfohis();
		insbCarinfoHis.setTaskid(taskId);
		insbCarinfoHis.setInscomcode(insurancecompanyid);
		List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(insbCarinfoHis);
		if(resultInsbCarinfoHisList.size()>0){
			insbCarinfoHis = resultInsbCarinfoHisList.get(0);
		}else{
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskId);
			List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
			if(resultInsbCarinfoList.size()>0){
				insbCarinfo = resultInsbCarinfoList.get(0);
			}else{
				return map1;
			}
			try {
				PropertyUtils.copyProperties(insbCarinfoHis, insbCarinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskId:" + taskId + ",insurancecompanyid:" + insurancecompanyid + ",接口车辆信息获得出错1");
				e.printStackTrace();
				return map1;
			}
		}
		
		map.put("owner", StringUtil.isEmpty(insbCarinfoHis.getOwnername())?"":insbCarinfoHis.getOwnername());//车主
		if(StringUtil.isEmpty(insbCarinfoHis.getCarlicenseno())
				||("1".equals(insbCarinfoHis.getIsNew())&&"新车未上牌".equals(insbCarinfoHis.getCarlicenseno())))
			map.put("plateNum","0");
		else
			map.put("plateNum", StringUtil.isEmpty(insbCarinfoHis.getCarlicenseno())?"":insbCarinfoHis.getCarlicenseno());//车牌号
		INSBCorecodemap insbCorecodemap;
		if (!StringUtil.isEmpty(insbCarinfoHis.getCarproperty())) {
			String carProperty = insbCarinfoHis.getCarproperty();
			insbCorecodemap = new INSBCorecodemap();
			insbCorecodemap.setCmcode(carProperty);
			insbCorecodemap.setType("usefortype");//使用性质
			List<INSBCorecodemap> coreList = insbCorecodemapService.queryList(insbCorecodemap);
			if (coreList != null && coreList.size() > 0) {
				insbCorecodemap = insbCorecodemapService.queryList(insbCorecodemap).get(0);
				map.put("useProps", insbCorecodemap.getCorecode());//使用性质
			} else {
				LogUtil.info("insbCarinfoHis表里字段carproperty的值错误请检查EDI|精灵回写的数据,任务号为%s错误的值为%s", taskId, carProperty);
			}
		}
		if (!StringUtil.isEmpty(insbCarinfoHis.getProperty())) {
			String property = insbCarinfoHis.getProperty();
			insbCorecodemap = new INSBCorecodemap();
			insbCorecodemap.setCmcode(property);
			insbCorecodemap.setType("cartype");
			List<INSBCorecodemap> coreList = insbCorecodemapService.queryList(insbCorecodemap);
			if (coreList != null && coreList.size() > 0) {
				insbCorecodemap = coreList.get(0);
				map.put("userType", insbCorecodemap.getCorecode());//车辆用户类型
			} else {
				LogUtil.info("insbCarinfoHis表里字段property的值错误请检查EDI|精灵回写的数据,任务号为%s错误的值为%s", taskId, property);
			}
		}
		map.put("firstRegDate", StringUtil.isEmpty(insbCarinfoHis.getRegistdate())?"":sdf.format(insbCarinfoHis.getRegistdate()));//初登日期
		map.put("vin", StringUtil.isEmpty(insbCarinfoHis.getVincode())?"":insbCarinfoHis.getVincode());//车架号
		map.put("engineNum", StringUtil.isEmpty(insbCarinfoHis.getEngineno())?"":insbCarinfoHis.getEngineno());//发动机号

		if (!StringUtil.isEmpty(insbCarinfoHis.getPlateType())) {//号牌种类
			String plateType = String.valueOf(insbCarinfoHis.getPlateType());
			insbCorecodemap = new INSBCorecodemap();
			insbCorecodemap.setCmcode(plateType);
			insbCorecodemap.setType("plateType");
			List<INSBCorecodemap> coreList = insbCorecodemapService.queryList(insbCorecodemap);
			if (coreList != null && coreList.size() > 0) {
				insbCorecodemap = coreList.get(0);
				map.put("plateType", insbCorecodemap.getCorecode());//号牌种类
			} else {
				LogUtil.info("insbCarinfoHis表里字段plateType的值错误请检查EDI|精灵回写的数据,任务号为%s错误的值为%s", taskId, plateType);
			}
		}
		map.put("avgMileage", StringUtil.isEmpty(insbCarinfoHis.getMileage())?"":insbCarinfoHis.getMileage());//平均行驶里程
		map.put("carDrivingArea", StringUtil.isEmpty(insbCarinfoHis.getDrivingarea())?"":insbCarinfoHis.getDrivingarea());//约定行驶区域
		map.put("bodyColor", StringUtil.isEmpty(insbCarinfoHis.getCarBodyColor())?"":insbCarinfoHis.getCarBodyColor());//车身颜色
		map.put("plateColor", StringUtil.isEmpty(insbCarinfoHis.getPlatecolor())?"":insbCarinfoHis.getPlatecolor());//号牌颜色
		map.put("useAge", StringUtil.isEmpty(insbCarinfoHis.getUseyears())?"":insbCarinfoHis.getUseyears());//使用年限
		map.put("transferVehicle", StringUtil.isEmpty(insbCarinfoHis.getIsTransfercar())?"":("1".equals(insbCarinfoHis.getIsTransfercar())?"13":"14"));//过户车
		//查询车辆表数据结束
		
		map1.put("accFrequency", StringUtil.isEmpty(insbCarinfoHis.getAccidentnum())?"0":insbCarinfoHis.getAccidentnum());//出险次数
		map1.put("carInfo", map);

		//查询车型表数据开始
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskId);
		List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
		if(resultInsbCarinfoList.size()>0){
			insbCarinfo = resultInsbCarinfoList.get(0);
			
			INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
			insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
			insbCarmodelinfohis.setInscomcode(insurancecompanyid);
			List<INSBCarmodelinfohis> resultInsbCarmodelinfohisList = insbCarmodelinfohisService.queryList(insbCarmodelinfohis);
			if(resultInsbCarmodelinfohisList.size()>0){
				insbCarmodelinfohis = resultInsbCarmodelinfohisList.get(0);
			}else{
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setCarinfoid(insbCarinfo.getId());
				List<INSBCarmodelinfo> resultInsbCarmodelinfoList = insbCarmodelinfoService.queryList(insbCarmodelinfo);
				if(resultInsbCarmodelinfoList.size()>0){
					insbCarmodelinfo = resultInsbCarmodelinfoList.get(0);
					try {
						PropertyUtils.copyProperties(insbCarmodelinfohis, insbCarmodelinfo);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LogUtil.error("taskId:" + taskId + ",insurancecompanyid:" + insurancecompanyid + ",接口车型信息获得出错2");
						e.printStackTrace();
						return map1;
					}
				}else{
					LogUtil.error("taskId:" + taskId + ",insurancecompanyid:" + insurancecompanyid + ",接口车型信息获得出错3");
					return map1;
				}
			}
			map.put("displacement", BigDecimal.valueOf(insbCarmodelinfohis.getDisplacement()==null?0.00:insbCarmodelinfohis.getDisplacement()).setScale(4, RoundingMode.HALF_UP).toString());//发动机排量
			map.put("carModelName", StringUtil.isEmpty(insbCarmodelinfohis.getStandardname())?"":insbCarmodelinfohis.getStandardname());//车型名称
			map.put("taxPrice", BigDecimal.valueOf(insbCarmodelinfohis.getPrice()==null?0.00:insbCarmodelinfohis.getPrice()).setScale(2, RoundingMode.HALF_UP).toString());//新车购置价
			map.put("seatCnt", StringUtil.isEmpty(insbCarmodelinfohis.getSeat())?"":insbCarmodelinfohis.getSeat().toString());//座位数
			map.put("fullLoad", BigDecimal.valueOf(insbCarmodelinfohis.getUnwrtweight()==null?0.00:insbCarmodelinfohis.getUnwrtweight()).setScale(2, RoundingMode.HALF_UP).toString());//车身自重
			map.put("carKindCode", StringUtil.isEmpty(insbCarmodelinfohis.getSyvehicletypecode())?"":insbCarmodelinfohis.getSyvehicletypecode());//机动车种类
		}
		//查询车型表数据结束
		return map1;
	} 
	private Map<String,Object> getSuiteCodeAndSuiteList(String taskid, String companyId,String riskType) {
		LogUtil.error("进入getSuiteCodeAndSuiteList方法，taskId:" + taskid + ",insurancecompanyid:" + companyId + ",riskType:"+riskType);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> suiteList = new ArrayList<Map<String,Object>>();
		map.put("suiteCode", "");
		map.put("suite", suiteList);
		try {
			INSBCarinfohis insbCarinfoHis = new INSBCarinfohis();
			insbCarinfoHis.setTaskid(taskid);
			insbCarinfoHis.setInscomcode(companyId);
			List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(insbCarinfoHis);
			List<String> insbCarinfoIdList=new ArrayList<String>();
			if(resultInsbCarinfoHisList.size()>0){
				insbCarinfoIdList.add(resultInsbCarinfoHisList.get(0).getId());
			}
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
			if(resultInsbCarinfoList.size()>0){
				insbCarinfoIdList.add(resultInsbCarinfoList.get(0).getId());
			}
			if(insbCarinfoIdList.size()==0){
				LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口车辆信息获得出错0");
				return map;
			}
			INSBCarmodelinfohis insbCarmodelinfohis=null;
			for (String  insbCarinfoId : insbCarinfoIdList) {
				insbCarmodelinfohis = new INSBCarmodelinfohis();
				insbCarmodelinfohis.setCarinfoid(insbCarinfoId);
				insbCarmodelinfohis.setInscomcode(companyId);
				List<INSBCarmodelinfohis> resultInsbCarmodelinfohisList = insbCarmodelinfohisService.queryList(insbCarmodelinfohis);
				if(resultInsbCarmodelinfohisList.size()>0){
					insbCarmodelinfohis = resultInsbCarmodelinfohisList.get(0);
				}else{
					INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
					insbCarmodelinfo.setCarinfoid(insbCarinfoId);
					List<INSBCarmodelinfo> resultInsbCarmodelinfoList = insbCarmodelinfoService.queryList(insbCarmodelinfo);
					if(resultInsbCarmodelinfoList.size()>0){
						insbCarmodelinfo = resultInsbCarmodelinfoList.get(0);
						try {
							PropertyUtils.copyProperties(insbCarmodelinfohis, insbCarmodelinfo);
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口车型信息获得出错2");
							e.printStackTrace();
							return map;
						}
					}
				}
			}
			INSBCorecodemap insbCorecodemap = new INSBCorecodemap();
			insbCorecodemap.setCmcode(String.valueOf(insbCarmodelinfohis==null||insbCarmodelinfohis.getJgVehicleType()==null?0:insbCarmodelinfohis.getJgVehicleType()));
			insbCorecodemap.setType("cartype");
			List<INSBCorecodemap> corecodemapList = insbCorecodemapService.queryList(insbCorecodemap);
			if(corecodemapList.size()==0){
				LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口核心编码信息获得出错1");
			}
			
			insbCorecodemap = corecodemapList.get(0);
			INSBCoreriskmap insbCoreriskmap = new INSBCoreriskmap();
			insbCoreriskmap.setCmcartype(insbCorecodemap.getCorecode());
			insbCoreriskmap.setInscomcode(companyId.length()>4?companyId.substring(0, 4):companyId);
			insbCoreriskmap.setCmriskcode(riskType.equals("0")?"CommercialIns":"VehicleCompulsoryIns");
			
			List<INSBCoreriskmap> insbCoreriskmapList = insbCoreriskmapService.queryList(insbCoreriskmap);
			if(insbCoreriskmapList.size()==0){
				LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口核心险别编码信息获得出错1");
			}
			//map.put("suiteCode", insbCoreriskmapList.size()==0?"":insbCoreriskmapList.get(0).getRiskcode());//险种编码
			Map<String,Object> item;
			BigDecimal totalCharge=new BigDecimal(0.0);
			if(riskType.equals("0")){
				//车辆类型
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setTaskid(taskid);
				//insbCarkindprice.setInskindtype(riskType);
				insbCarkindprice.setInscomcode(companyId);
				List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);
				if(resultInsbCarkindpriceList.size()<=0){
					LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口险别信息获得出错1");
					return map;
				}
				List<INSBCoreriskmap> coreriskmapList;
				for (INSBCarkindprice carkindprice : resultInsbCarkindpriceList) {
					if(!"1".equals(carkindprice.getInskindtype())&&!"0".equals(carkindprice.getInskindtype()))continue;
					item = new HashMap<String,Object>();
					insbCoreriskmap = new INSBCoreriskmap();
					insbCoreriskmap.setCmcartype(insbCorecodemap.getCorecode());
					insbCoreriskmap.setInscomcode(companyId.length()>4?companyId.substring(0, 4):companyId);
					insbCoreriskmap.setCmriskcode(riskType.equals("0")?"CommercialIns":"VehicleCompulsoryIns");
					insbCoreriskmap.setCmriskkindcode(carkindprice.getInskindcode());
					coreriskmapList = insbCoreriskmapService.queryList(insbCoreriskmap);
					if(coreriskmapList.size()==0){
						LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口险别信息获得出错2,Cmcartype:"+insbCoreriskmap.getCmcartype()+",Inscomcode:"+insbCoreriskmap.getInscomcode()+",Cmriskcode:"+insbCoreriskmap.getCmriskcode()+",Cmriskkindcode:"+insbCoreriskmap.getCmriskkindcode());
						continue;
					}
					insbCoreriskmap=coreriskmapList.get(0);
					if(insbCoreriskmap==null)continue;
					if(StringUtil.isEmpty(map.get("suiteCode")))
						map.put("suiteCode",StringUtil.isEmpty(insbCoreriskmap.getRiskcode())?"":insbCoreriskmap.getRiskcode());//险种编码
					item.put("ecode", StringUtil.isEmpty(insbCoreriskmap.getRiskkindcode())?"":insbCoreriskmap.getRiskkindcode());//险别代码
					if("1".equals(carkindprice.getInskindtype()))item.put("amount", "1");
					else item.put("amount", BigDecimal.valueOf(carkindprice.getAmount()==null?0.00:carkindprice.getAmount()).setScale(2, RoundingMode.HALF_UP).toString());//保额
					item.put("charge", BigDecimal.valueOf(carkindprice.getDiscountCharge()==null?0.00:carkindprice.getDiscountCharge()).setScale(2, RoundingMode.HALF_UP).toString());//折后保费
					totalCharge=totalCharge.add(BigDecimal.valueOf(carkindprice.getDiscountCharge()==null?0.00:carkindprice.getDiscountCharge()));//总保费
					suiteList.add(item);
				}
			}else{
				map.put("suiteCode", insbCoreriskmapList.size()==0?"":insbCoreriskmapList.get(0).getRiskcode());//险种编码
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setTaskid(taskid);
				//insbCarkindprice.setInskindtype(riskType);
				insbCarkindprice.setInscomcode(companyId);
				insbCarkindprice.setInskindtype("2");
				insbCarkindprice=insbCarkindpriceService.queryOne(insbCarkindprice);
				totalCharge=totalCharge.add(BigDecimal.valueOf(insbCarkindprice.getDiscountCharge()==null?0.00:insbCarkindprice.getDiscountCharge()));
			}
			map.put("totalCharge", totalCharge.setScale(2, RoundingMode.HALF_UP).toString());//总保费
			map.put("suite", suiteList);
		} catch (Exception e) {
			LogUtil.error("taskId:" + taskid + ",insurancecompanyid:" + companyId + ",接口险种信息获得出错1");
			e.printStackTrace();
		}
		LogUtil.error("关闭getSuiteCodeAndSuiteList方法，taskId:" + taskid + ",insurancecompanyid:" + companyId + ",riskType:"+riskType);
		return map;
	}
	@Override
	public List<Map<String, Object>> save(String saveJsons) {
		LogUtil.info("进入任务关闭接口：saveJsons="+saveJsons+"====");
		List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
		JSONArray jsonArray;
		try {
			jsonArray = JSONArray.parseArray(saveJsons);
		} catch (Exception e1) {
			try {
				jsonArray = new JSONArray();
				jsonArray.add(JSONObject.parseObject(saveJsons));
			} catch (Exception e) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("resultCode", "0");
				map.put("message", "参数形式出错");
				arrayList.add(map);
				return arrayList;
			}
		}
		for (Object saveJson : jsonArray) {
			
			LogUtil.info("任务关闭接口：saveJson="+saveJson+"====");
			Map<String, Object> map = new HashMap<String,Object>();
			String taskid=null,companyId=null,policyNum=null,risktype=null;
			try {
				JSONObject jsonObject = (JSONObject)saveJson;
				JSONObject insuredPersonInfo = jsonObject.getJSONObject("insuredPersonInfo");//受益人信息
//				JSONObject agentInfo = jsonObject.getJSONObject("agentInfo");
				String suiteCode = jsonObject.getString("suiteCode");//险种编码
				/*if(!jsonObject.containsKey("bizTransactionId")){
				LogUtil.info("进入====任务关闭接口：错误，key：businessId，不存在====");
				map.put("resultCode", 0);
				map.put("message", "businessId不存在");
				return map;
			}*/
				String bizTransactionId = jsonObject.getString("bizTransactionId");
				if(!StringUtil.isEmpty(jsonObject.get("bizTransactionId"))){
					if(bizTransactionId.indexOf("@")<0){
						LogUtil.info("进入====任务关闭接口：错误，bizTransactionId格式不对，没有@符号====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "bizTransactionId格式不对，没有@符号");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
					if(bizTransactionId.split("@")[1].length()<4){
						LogUtil.info("进入====任务关闭接口：错误，bizTransactionId中保险公司编码长度不对，小于4====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "bizTransactionId中保险公司编码长度不对，小于4");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
					taskid=bizTransactionId.split("@")[0];
					companyId=bizTransactionId.split("@")[1];//保险公司编码
				}else{
					if(!jsonObject.containsKey("carInfo")||
							StringUtil.isEmpty(jsonObject.getJSONObject("carInfo").get("plateNum"))){
						LogUtil.info("进入====任务关闭接口：错误，bizTransactionId为不存在是，plateNum不可为空====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "bizTransactionId格式不对，没有@符号");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
					Map<String, Object> param = new HashMap<String, Object>();
					if(jsonObject.getJSONObject("carInfo").getString("plateNum").equals("0"))
						param.put("carlicenseno","新车未上牌");
					else 
						param.put("carlicenseno", jsonObject.getJSONObject("carInfo").getString("plateNum"));
					param.put("insuredname",insuredPersonInfo.getString("fullName"));
					param.put("prvid",jsonObject.getString("insCorpCode"));
					List<INSBPolicyitem> policyitemList = insbPolicyitemService.getListByParam(param);
					for (INSBPolicyitem insbPolicyitem : policyitemList) {
						Double charge;
						INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
						queryInsbCarkindprice.setTaskid(insbPolicyitem.getTaskid());
						queryInsbCarkindprice.setInscomcode(insbPolicyitem.getInscomcode());
						List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
						BigDecimal forcePremium=new BigDecimal("0");
						BigDecimal businessPremium=new BigDecimal("0");
						for(INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList){
							BigDecimal decimal = new BigDecimal(String.valueOf(dataInsbCarkindprice.getDiscountCharge()==null?0:dataInsbCarkindprice.getDiscountCharge()));
							if(dataInsbCarkindprice.getInskindtype().equals("0")
									||dataInsbCarkindprice.getInskindtype().equals("1")){
								businessPremium=businessPremium.add(decimal);
							}
							if(dataInsbCarkindprice.getInskindtype().equals("2")){
								forcePremium=forcePremium.add(decimal);
							}
						}
						if(insbPolicyitem.getRisktype().equals("0"))
							charge=businessPremium.setScale(2, RoundingMode.HALF_UP).doubleValue();
						else
							charge=forcePremium.setScale(2, RoundingMode.HALF_UP).doubleValue();
						
						if(Double.valueOf(jsonObject.getString("charge")).equals(charge)){
							taskid=insbPolicyitem.getTaskid();
							companyId=insbPolicyitem.getInscomcode();
							risktype=insbPolicyitem.getRisktype();
							break;
						}
					}
				}
				
				if(taskid==null&&companyId==null){
					LogUtil.info("进入====任务关闭接口：未找到匹配的保单====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "未找到匹配的保单");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				policyNum = jsonObject.getString("policyNum");//保单号
				String proposeNum = jsonObject.getString("proposeNum");//投保单号
				String effectiveDate = jsonObject.getString("effectiveDate");//保单生效日期
				String issuingDate = jsonObject.getString("issuingDate");//出单日期
				String charge = jsonObject.getString("charge");//保费
				String saleWay = jsonObject.getString("saleWay");//销售方式
				String platformCode = jsonObject.getString("platformCode");//平台编码
				JSONObject carInfo = jsonObject.getJSONObject("carInfo");
				String plateNum = carInfo.getString("plateNum");//车牌号
				String vin = carInfo.getString("vin");//车架号
				String engineNum = carInfo.getString("engineNum");//发动机号
				String owner = carInfo.getString("owner");//车主姓名
				if("0".equals(plateNum))plateNum="新车未上牌";
				JSONObject agencyOrg = jsonObject.getJSONObject("agencyOrg");
				String comCode = agencyOrg.getString("comCode");
				//查询INSBCoreriskmap表
				INSBCoreriskmap insbCoreriskmap = new INSBCoreriskmap();
				insbCoreriskmap.setRiskkindcode(suiteCode);
				insbCoreriskmap = insbCoreriskmapService.queryOne(insbCoreriskmap);
				if(insbCoreriskmap==null){
					insbCoreriskmap=new INSBCoreriskmap();
					insbCoreriskmap.setRiskcode(suiteCode);
					List<INSBCoreriskmap> insbCoreriskmapList = insbCoreriskmapService.queryList(insbCoreriskmap);
					if(insbCoreriskmapList.size()>0)insbCoreriskmap = insbCoreriskmapList.get(0);
					else insbCoreriskmap = null;
				}
				if(insbCoreriskmap == null){
					LogUtil.info("数据回写====任务关闭接口：错误，代码映射，险种代码不存在====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "险种代码不存在");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				
				//查询险种类型
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setTaskid(taskid);
				insbCarkindprice.setInscomcode(companyId);
				if("CommercialIns".equals(insbCoreriskmap.getCmriskcode()))insbCarkindprice.setInskindtype("0");//商业险
				if("VehicleCompulsoryIns".equals(insbCoreriskmap.getCmriskcode()))insbCarkindprice.setInskindtype("2");//交强险
				if("VehicleTax".equals(insbCoreriskmap.getCmriskcode()))insbCarkindprice.setInskindtype("3");//车船税
				List<INSBCarkindprice> insbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);
				if(insbCarkindpriceList.size()==0){
					LogUtil.info("数据回写====任务关闭接口：错误，没有对应的险别报价数据====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "没有对应的险别报价数据");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				INSCDept inscDept = new INSCDept();
				inscDept.setComcode(comCode);
				inscDept = inscDeptService.queryOne(inscDept);
				if(inscDept==null||!inscDept.getComtype().equals("05")){
					LogUtil.info("数据回写====任务关闭接口：错误，网点不存在====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "网点不存在");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				INSBOrder insbOrder = new INSBOrder();
				insbOrder.setTaskid(taskid);
				insbOrder.setPrvid(companyId);
				insbOrder = insbOrderService.queryOne(insbOrder);
				if(insbOrder==null){
					LogUtil.info("数据回写====任务关闭接口：错误，订单不存在====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "订单不存在");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				insbOrder.setDeptcode(comCode);
				insbOrderService.updateById(insbOrder);
				INSBQuoteinfo insbQuoteinfo1 = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, companyId);
				if(insbQuoteinfo1!=null){
					insbQuoteinfo1.setDeptcode(comCode);
					insbQuoteinfoService.updateById(insbQuoteinfo1);
				}
				//查询INSBPolicyitem表
				INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
				insbPolicyitem.setTaskid(taskid);
				insbPolicyitem.setRisktype("0".equals(insbCarkindpriceList.get(0).getInskindtype())?"0":"1");
				insbPolicyitem.setInscomcode(companyId);
				insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
				if(insbPolicyitem==null){
					insbPolicyitem = new INSBPolicyitem();
					insbPolicyitem.setTaskid(taskid);
					insbPolicyitem.setRisktype("0".equals(insbCarkindpriceList.get(0).getInskindtype())?"0":"1");
					insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
				}
				if(insbPolicyitem==null){
					LogUtil.info("数据回写====任务关闭接口：错误，保单不存在====");
					map.put("resultCode", 0);
					if(jsonObject.containsKey("policyNum"))
						map.put("policyNum", jsonObject.getString("policyNum"));
					map.put("message", "保单不存在");
					INSBSaveerror insbSaveerror = new INSBSaveerror();
					insbSaveerror.setCreatetime(new Date());
					insbSaveerror.setOperator("admin");
					insbSaveerror.setMessage(saveJson.toString());
					insbSaveerror.setErrordesc(map.get("message").toString());
					insbSaveerrorService.insert(insbSaveerror);
					arrayList.add(map);
					continue;
				}
				//更新INSBPolicyitem表
				if(!StringUtil.isEmpty(charge))insbPolicyitem.setDiscountCharge(Double.parseDouble(charge));
				if(!StringUtil.isEmpty(policyNum))insbPolicyitem.setPolicyno(policyNum);
				if(!StringUtil.isEmpty(proposeNum))insbPolicyitem.setProposalformno(proposeNum);
				if(!StringUtil.isEmpty(insuredPersonInfo)&&!StringUtil.isEmpty(insuredPersonInfo.getString("fullName")))//投保人姓名
					insbPolicyitem.setInsuredname(insuredPersonInfo.getString("fullName"));
				/*if(!StringUtil.isEmpty(agentInfo)){
					if(!StringUtil.isEmpty(agentInfo.getString("agentCode")))insbPolicyitem.setAgentnum(agentInfo.getString("agentCode"));//代理人编码
					if(!StringUtil.isEmpty(agentInfo.getString("agentName")))insbPolicyitem.setAgentname(agentInfo.getString("agentName"));//代理人姓名
					if(!StringUtil.isEmpty(agentInfo.getString("agentTeamBelong")))insbPolicyitem.setTeam(agentInfo.getString("agentTeamBelong"));//代理人所属团队
				}*/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(!StringUtil.isEmpty(effectiveDate)){
					insbPolicyitem.setStartdate(sdf.parse(effectiveDate));
					//结束时间为开始时间加一年
					Date endDate = sdf.parse(effectiveDate);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(endDate);
					calendar.add(Calendar.YEAR, +1);
					endDate=calendar.getTime();
					insbPolicyitem.setEnddate(endDate);
				}
				if(!StringUtil.isEmpty(issuingDate))insbPolicyitem.setInsureddate(sdf.parse(issuingDate));
				insbPolicyitem.setModifytime(new Date());
				insbPolicyitem.setClosedstatus("1");
				insbPolicyitem.setPolicystatus("1");
				insbPolicyitemService.updateById(insbPolicyitem);
				
				//更新INSBCarinfo
				INSBCarinfohis dataInsbCarinfoHis = new INSBCarinfohis();
				
				dataInsbCarinfoHis.setTaskid(taskid);
				dataInsbCarinfoHis.setInscomcode(companyId);
				dataInsbCarinfoHis.setCarlicenseno(plateNum);
				dataInsbCarinfoHis = insbCarinfohisService.queryOne(dataInsbCarinfoHis);
				
				if(dataInsbCarinfoHis==null){
					INSBCarinfo dataInsbCarinfo = new INSBCarinfo();
					dataInsbCarinfo.setTaskid(taskid);
					dataInsbCarinfo.setCarlicenseno(plateNum);
					dataInsbCarinfo = insbCarinfoService.queryOne(dataInsbCarinfo);
					if(dataInsbCarinfo==null){
						LogUtil.info("====任务关闭接口：错误，车辆信息不存在====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "车辆信息不存在");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
					if(!StringUtil.isEmpty(owner))dataInsbCarinfo.setOwnername(owner);
					if(!StringUtil.isEmpty(vin))dataInsbCarinfo.setVincode(vin);
					if(!StringUtil.isEmpty(engineNum))dataInsbCarinfo.setEngineno(engineNum);
					dataInsbCarinfo.setModifytime(new Date());
					insbCarinfoService.updateById(dataInsbCarinfo);
				}else{
					if(!StringUtil.isEmpty(owner))dataInsbCarinfoHis.setOwnername(owner);
					if(!StringUtil.isEmpty(vin))dataInsbCarinfoHis.setVincode(vin);
					if(!StringUtil.isEmpty(engineNum))dataInsbCarinfoHis.setEngineno(engineNum);
					dataInsbCarinfoHis.setModifytime(new Date());
					insbCarinfohisService.updateById(dataInsbCarinfoHis);
				}
				if(!StringUtil.isEmpty(saleWay)){
					INSBCorecodemap insbCorecodemap = new INSBCorecodemap();
					insbCorecodemap.setType("saletype");//销售方式
					insbCorecodemap.setCorecode(saleWay);
					List<INSBCorecodemap> queryList = insbCorecodemapService.queryList(insbCorecodemap);
					if(queryList.size()==0){
						LogUtil.info("====任务关闭接口：错误，核心代码映射中未找到对应的销售方式====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "核心代码映射中未找到对应的销售方式");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
					insbCorecodemap = queryList.get(0);
					if(insbCorecodemap!=null){
						INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, companyId);
						if(insbQuoteinfo!=null){
							insbQuoteinfo.setBuybusitype(insbCorecodemap.getCmcode());
							insbQuoteinfoService.updateById(insbQuoteinfo);
						}else{
							LogUtil.info("====任务关闭接口：错误，销售方式保存失败====");
							map.put("resultCode", 0);
							if(jsonObject.containsKey("policyNum"))
								map.put("policyNum", jsonObject.getString("policyNum"));
							map.put("message", "销售方式保存失败");
							INSBSaveerror insbSaveerror = new INSBSaveerror();
							insbSaveerror.setCreatetime(new Date());
							insbSaveerror.setOperator("admin");
							insbSaveerror.setMessage(saveJson.toString());
							insbSaveerror.setErrordesc(map.get("message").toString());
							insbSaveerrorService.insert(insbSaveerror);
							arrayList.add(map);
							continue;
						}
					}else{
						LogUtil.info("====任务关闭接口：错误，销售方式在映射表中不存在====");
						map.put("resultCode", 0);
						if(jsonObject.containsKey("policyNum"))
							map.put("policyNum", jsonObject.getString("policyNum"));
						map.put("message", "销售方式在映射表中不存在");
						INSBSaveerror insbSaveerror = new INSBSaveerror();
						insbSaveerror.setCreatetime(new Date());
						insbSaveerror.setOperator("admin");
						insbSaveerror.setMessage(saveJson.toString());
						insbSaveerror.setErrordesc(map.get("message").toString());
						insbSaveerrorService.insert(insbSaveerror);
						arrayList.add(map);
						continue;
					}
				}
				INSBPolicyitem policyitem = new INSBPolicyitem();
				policyitem.setTaskid(taskid);
				policyitem.setInscomcode(companyId);
				List<INSBPolicyitem> list = insbPolicyitemService.queryList(policyitem);
				policyitem = new INSBPolicyitem();
				policyitem.setTaskid(taskid);
				policyitem.setInscomcode(companyId);
				policyitem.setClosedstatus("1");
				List<INSBPolicyitem> list1 = insbPolicyitemService.queryList(policyitem);
				if(list.size()==list1.size()){
					//节点状态改为反向关闭					
					INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, companyId);

					INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
					insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
					insbWorkflowsubtrack.setTaskcode("39");
					insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
					if(insbWorkflowsubtrack==null){
						insbWorkflowsubtrack = new INSBWorkflowsubtrack();
						insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
						insbWorkflowsubtrack.setTaskcode("39");
						insbWorkflowsubtrack.setOperator("admin");
						insbWorkflowsubtrack.setCreatetime(new Date());
						insbWorkflowsubtrack.setMaininstanceid(taskid);
						insbWorkflowsubtrack.setTaskstate("Ready");
						insbWorkflowsubtrackService.insert(insbWorkflowsubtrack);
						
						
						INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
						insbWorkflowsub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
						insbWorkflowsub = insbWorkflowsubService.queryOne(insbWorkflowsub);
						if(insbWorkflowsub!=null){
							insbWorkflowsub.setTaskcode("39");
							insbWorkflowsub.setTaskstate("Ready");
							insbWorkflowsub.setModifytime(new Date());
							insbWorkflowsubService.updateById(insbWorkflowsub);
						}
						
						INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
						insbOrderpayment.setTaskid(taskid);
						insbOrderpayment.setPayresult("02");
						List<INSBOrderpayment> queryList = insbOrderpaymentService.queryList(insbOrderpayment);
						if(queryList.size()!=0)
							insbOrderpayment=queryList.get(0);
						insbOrderpayment.setOperator("核心");
						insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
						insbOrderpayment.setOrderid(insbOrder.getId());
						insbOrderpayment.setPaychannelid("4");//把insborderpayment表的paychannelid设为4，把noti设为03，表示线下支付
						insbOrderpayment.setNoti("03");//把insborderpayment表的paychannelid设为4，把noti设为03，表示线下支付
						insbOrderpayment.setAmount(insbOrder.getTotalpaymentamount());
						insbOrderpayment.setCreatetime(new Date());
						insbOrderpayment.setCurrencycode(insbOrder.getCurrency());
						insbOrderpayment.setPaytime(new Date());
						if(queryList.size()==0){
							insbOrderpaymentService.insert(insbOrderpayment);
						}else{
							insbOrderpaymentService.updateById(insbOrderpayment);
						}
						insbOrder.setPaymentmethod("4");//把insborder 表的Paymentmethod设为4，把noti设为03，表示线下支付
						insbOrder.setNoti("03");//把insborder 表的Paymentmethod设为4，把noti设为03，表示线下支付
						insbOrder.setPaymentstatus("02");
						insbOrderService.updateById(insbOrder);
						
						INSBWorkflowsubtrackdetail insbWorkflowsubtrackdetail = new INSBWorkflowsubtrackdetail();
						insbWorkflowsubtrackdetail.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
						insbWorkflowsubtrackdetail.setTaskcode("39");
						insbWorkflowsubtrackdetail.setOperator("admin");
						insbWorkflowsubtrackdetail.setCreatetime(new Date());
						insbWorkflowsubtrackdetail.setMaininstanceid(taskid);
						insbWorkflowsubtrackdetail.setTaskstate("Ready");
						insbWorkflowsubtrackdetailService.insert(insbWorkflowsubtrackdetail);
						
						//关闭工作流
						Map<String,String> map2 = new HashMap<String, String>();
						map2.put("mainprocessinstanceid", taskid);
						String result =null ;
						try {
							result = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url")+"/process/setProcessSuccess", map2);
							JSONObject parseObject = JSONObject.parseObject(result);
							if("fail".equals(parseObject.getString("message"))){
								map.put("resultCode", 0);
								map.put("policyNum", policyNum);
								map.put("message", parseObject.getString("reason"));
								INSBSaveerror insbSaveerror = new INSBSaveerror();
								insbSaveerror.setCreatetime(new Date());
								insbSaveerror.setOperator("admin");
								insbSaveerror.setMessage(saveJson.toString());
								insbSaveerror.setErrordesc(map.get("message").toString());
								insbSaveerrorService.insert(insbSaveerror);
								arrayList.add(map);
							}
						} catch (Exception e) {
							map.put("resultCode", 0);
							map.put("policyNum", policyNum);
							map.put("message", "请求工作流失败");
							INSBSaveerror insbSaveerror = new INSBSaveerror();
							insbSaveerror.setCreatetime(new Date());
							insbSaveerror.setOperator("admin");
							insbSaveerror.setMessage(saveJson.toString());
							insbSaveerror.setErrordesc(map.get("message").toString());
							insbSaveerrorService.insert(insbSaveerror);
							arrayList.add(map);
							//continue;
						}
						LogUtil.info("任务关闭接口：关闭工作流 taskid=" + taskid + ",companyid=" + companyId +",url="+ValidateUtil.getConfigValue("workflow.url")+"/process/setProcessSuccess,param:"+JSONObject.toJSONString(map2)+",结果："+result);
						//推cif
						String pushtocif = interFaceService.pushtocif(taskid, companyId);
						LogUtil.info("====任务关闭接口：推cif====taskid="+taskid+",companyId="+companyId+",结果："+pushtocif);
						
						WorkFlow4TaskModel workFlow4TaskModel = new WorkFlow4TaskModel();
						workFlow4TaskModel.setMainInstanceId(taskid);
						workFlow4TaskModel.setSubInstanceId(insbQuoteinfo.getWorkflowinstanceid());
						workFlow4TaskModel.setProviderId(companyId);
						workFlow4TaskModel.setTaskName("核心反向关闭");
						workFlow4TaskModel.setTaskCode("39");
						workFlow4TaskModel.setTaskStatus("Ready");
						chnChannelService.callback(workFlow4TaskModel);
						
						INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, companyId);
						LogUtil.info("任务关闭接口：关闭任务 mainInstanceId=" + taskid + ",instanceid=" + quoteinfo.getWorkflowinstanceid());
						dispatchService.completedAllTaskByCore(taskid, quoteinfo.getWorkflowinstanceid());
						
						insbAccountDetailsService.refreshRedPackets(taskid,companyId);
						insbAccountDetailsService.genCommissionAndRedPackets(taskid,companyId);
						
					}
					
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info("====任务关闭接口：错误，保存出错====");
				map.put("resultCode", 0);
				map.put("policyNum", policyNum);
				map.put("message", "保存出错");
				INSBSaveerror insbSaveerror = new INSBSaveerror();
				insbSaveerror.setCreatetime(new Date());
				insbSaveerror.setOperator("admin");
				insbSaveerror.setMessage(saveJson.toString());
				insbSaveerror.setErrordesc(map.get("message").toString());
				insbSaveerrorService.insert(insbSaveerror);
				arrayList.add(map);
				continue;
			}
			
			map.put("resultCode", 1);
			map.put("policyNum", policyNum);
			map.put("message", "Congratulation,数据保存成功");
			LogUtil.info("任务关闭接口：Congratulation,数据保存成功====");
			arrayList.add(map);
			continue;
		}
		LogUtil.info("关闭任务关闭接口：saveJsons="+saveJsons+"====");
		return arrayList;
	}

	@Override
	public String pushtocore(String taskid, String companyid) {
		LogUtil.info("进入推核心任务接口：taskid=" + taskid + ",companyid=" + companyid + ",url=" + ValidateUtil.getConfigValue("core.url") + "/ZHBCarCont/ZHBDockDeal");
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(companyid)) {
			return "{\"resultCode\":\"0\",\"message\":\"参数不符合要求\"}";
		}

		Map<String,Object> map = new HashMap<String,Object>();
		String policyid=null;
		try {
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if (insbQuotetotalinfo == null) {
				LogUtil.info("%s报价总表不存在记录", taskid);
				return "{\"resultCode\":\"0\",\"message\":\"报价总表不存在记录\"}";
			}
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			quoteinfo.setInscomcode(companyid);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			if (quoteinfo == null) {
				LogUtil.info("%s报价分表不存在记录", taskid);
				return "{\"resultCode\":\"0\",\"message\":\"报价分表不存在记录\"}";
			}
			String bussNature = "01";//默认内部协议
			INSBAgreement agreement = insbAgreementDao.selectById(quoteinfo.getAgreementid());
			if (agreement != null && agreement.getAgreementType() == 1) {
//				LogUtil.info("%s为外部协议不推核心", taskid);
//				return "{\"resultCode\":\"0\",\"message\":\"外部协议不推核心\"}";
				bussNature = "03";//外部协议
			}
			List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
			Map<String,Object> agentInfo,item,suiteCodeAndSuiteList;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"),
					sdf2=new SimpleDateFormat("HH:mm:ss"),
					sdf3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			INSBOrder order=null;
			INSBCorecodemap insbCorecodemap;
			INSBQuoteinfo insbQuoteinfo;
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			insbPolicyitem.setInscomcode(companyid);
			List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
			if(insbPolicyitemList.size()==0){
				insbPolicyitem = new INSBPolicyitem();
				insbPolicyitem.setTaskid(taskid);
				insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
			}
			for (INSBPolicyitem policyitem : insbPolicyitemList) {
				order = new INSBOrder();
				order.setTaskid(policyitem.getTaskid());
				order.setPrvid(companyid);
				order = insbOrderService.queryOne(order);
				if(order==null)continue;
				if(StringUtil.isEmpty(policyitem.getPolicyno())){
					INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
					insbCarkindprice.setTaskid(taskid);
					insbCarkindprice.setInscomcode(companyid);
					if("1".equals(policyitem.getRisktype()))
						insbCarkindprice.setInskindtype("2");
					else
						insbCarkindprice.setInskindtype(policyitem.getRisktype());
					if(insbCarkindpriceService.queryList(insbCarkindprice).size()!=0)
						throw new  Exception("保单号为空");
				}
				item = new HashMap<String, Object>();
				item.put("policyNum", StringUtil.isEmpty(policyitem.getPolicyno())?"":policyitem.getPolicyno());//保单号
				item.put("proposeNum", StringUtil.isEmpty(policyitem.getProposalformno())?"":policyitem.getProposalformno());//投保单号
				//item.put("charge",BigDecimal.valueOf(policyitem.getPremium()==null?0.00:policyitem.getPremium()).setScale(2, RoundingMode.HALF_UP).toString());//保费
				item.put("insuredPersonInfo", this.getInsuredPersonInfo(policyitem.getTaskid(),companyid,null));//被保人
				insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(policyitem.getTaskid(), companyid);
				INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
				insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
				insbWorkflowsubtrack.setTaskcode("21");
				insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
				if(insbWorkflowsubtrack==null){
					insbWorkflowsubtrack = new INSBWorkflowsubtrack();
					insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
					insbWorkflowsubtrack.setTaskcode("20");
					insbWorkflowsubtrack.setTaskstate("Completed");
					insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
				}
				if(insbWorkflowsubtrack==null){
					item.put("issuingDate", "");//出单日期(年月日)
					item.put("issuingTime", "");//出单时间（时分秒）
					
				}else{
					item.put("issuingDate", StringUtil.isEmpty(insbWorkflowsubtrack.getModifytime())?"":sdf1.format(insbWorkflowsubtrack.getModifytime()));//出单日期(年月日)
					item.put("issuingTime", StringUtil.isEmpty(insbWorkflowsubtrack.getModifytime())?"":sdf2.format(insbWorkflowsubtrack.getModifytime()));//出单时间（时分秒）
					
				}
				item.put("recordingDate", StringUtil.isEmpty(policyitem.getInsureddate())?"":sdf1.format(policyitem.getInsureddate()));//签单日期
				item.put("printDate", StringUtil.isEmpty(policyitem.getInsureddate())?"":sdf1.format(policyitem.getInsureddate()));//打印日期
				
				suiteCodeAndSuiteList = this.getSuiteCodeAndSuiteList(policyitem.getTaskid(),companyid,policyitem.getRisktype());
				item.put("suiteCode", suiteCodeAndSuiteList.get("suiteCode"));//险种代码
				item.put("suite", suiteCodeAndSuiteList.get("suite"));//险种
				item.put("charge",suiteCodeAndSuiteList.get("totalCharge"));//总保费
				
				item.put("bizTransactionId", policyitem.getTaskid()+"@"+companyid);// taskid+@+保险公司代码
				item.put("agencyOrgList", this.getDeptTree(policyitem.getTaskid(),companyid));//出单网点机构列表
				agentInfo = new HashMap<String,Object>();
				if(!StringUtil.isEmpty(order.getAgentcode())){
					INSBAgent insbAgent = new INSBAgent();
					insbAgent.setAgentcode(order.getAgentcode());
					insbAgent.setIstest(3);
					insbAgent = insbAgentService.queryOne(insbAgent);
					if(insbAgent!=null){
						agentInfo.put("agentCode",insbAgent.getJobnum4virtual());//代理人编码
					}else{
						agentInfo.put("agentCode",order.getAgentcode());//代理人编码
					}
				}else{
					agentInfo.put("agentCode","");//代理人编码
				}
				agentInfo.put("agentName", StringUtil.isEmpty(order.getAgentname())?"":order.getAgentname());//代理人姓名
				item.put("agentInfo", agentInfo);//代理人信息
				item.put("insCorpCode", StringUtil.isEmpty(order.getPrvid())?"":order.getPrvid());//投保保险公司代码
				item.put("effectiveDate", StringUtil.isEmpty(policyitem.getStartdate())?"":sdf3.format(policyitem.getStartdate()));//起保日期
				item.put("expiryDate", StringUtil.isEmpty(policyitem.getEnddate())?"":sdf3.format(policyitem.getEnddate()));//有效日期至
				Map<String, Object> carInfo = this.getCarInfo(policyitem.getTaskid(),companyid);
				item.put("carInfo", carInfo.get("carInfo"));
				item.put("accFrequency", carInfo.get("accFrequency"));//出险次数
				if(!StringUtil.isEmpty(insbQuoteinfo)&&!StringUtil.isEmpty(insbQuoteinfo.getBuybusitype())){
					insbCorecodemap=new INSBCorecodemap();
					insbCorecodemap.setType("saletype");//销售方式
					insbCorecodemap.setCmcode(insbQuoteinfo.getBuybusitype());
					insbCorecodemap=insbCorecodemapService.queryList(insbCorecodemap).get(0);
					item.put("saleWay", StringUtil.isEmpty(insbCorecodemap)?"":insbCorecodemap.getCorecode());
				}else{
					item.put("saleWay", "");//销售方式
				}
				BigDecimal taxCharge = this.getTaxCharge(policyitem.getTaskid(),companyid);
				item.put("taxCharge", StringUtil.isEmpty(taxCharge)||"0".equals(policyitem.getRisktype())?"":taxCharge.setScale(2, RoundingMode.HALF_UP).toString());//车船税
				item.put("buyRist",this.getBuyRist(policyitem.getTaskid(),companyid));//购买险种
				Map<String, String> custLoyalty = this.getCustLoyalty(policyitem.getTaskid(),companyid);
				item.put("custLoyalty",custLoyalty.get("custLoyalty"));//客户忠诚度
				item.put("payoutMoney",custLoyalty.get("payoutMoney"));//赔款金额
				if("0".equals(policyitem.getRisktype()))
					item.put("payoutRate",custLoyalty.get("sypayoutRate"));//赔付率
				else
					item.put("payoutRate",custLoyalty.get("jqpayoutRate"));//赔付率
					
				item.put("accFrequencysy",custLoyalty.get("accFrequencysy"));//商业出险次数
				item.put("accFrequencyjq",custLoyalty.get("accFrequencyjq"));//交强出险次数
				item.put("discount",StringUtil.isEmpty(policyitem.getDiscountRate())?"":policyitem.getDiscountRate().toString());//赔款金额
				item.put("tax3","");//增值税
				item.put("bussNature", bussNature);//业务性质(01-直接个人、03-平台业务)
				res.add(item);
				policyid=policyitem.getId();
			}
			
			map.put("cnt", String.valueOf(res.size()));//条数
			map.put("res", res);
			
			String result = HttpClientUtil.doPostJsonAsyncClientJson(ValidateUtil.getConfigValue("core.url")+"/ZHBCarCont/ZHBDockDeal", JSONObject.toJSONString(map),"utf-8");
			LogUtil.info("pushtocore end result:" + result+",taskid:"+taskid+",param:"+JSONObject.toJSONString(map));
            if(StringUtil.isEmpty(result))
				this.coreSyncErrorLog(taskid, companyid, "推送核心返回超时");
			else
				this.coreSyncErrorLog(taskid, companyid, "核心处理数据中");
		} catch (Exception e) {
			e.printStackTrace();
			this.coreSyncErrorLog(taskid, companyid, e.getMessage());
		}

		LogUtil.info("结束推核心任务接口：taskid=" + taskid + ",companyid=" + companyid +",url="+ValidateUtil.getConfigValue("core.url")+"/ZHBCarCont/ZHBDockDeal,param:"+JSONObject.toJSONString(map));
		return "{\"resultCode\":\"1\",\"message\":\"\"}";
	}

	@Override
	public String callback(String result) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LogUtil.info("进入推核心任务回调接口：result="+result);
		try {
			JSONObject resjson = JSONObject.parseObject(result);
			String[] split = resjson.getString("bizTransactionId").split("@");
			String taskid=split[0];
			String companyid=split[1];
				if(!StringUtil.isEmpty(result)){
					JSONObject parseObject;
					try {
						parseObject = JSONObject.parseObject(result);
						if("-1".equals(parseObject.getString("resultCode")))
							this.coreSyncErrorLog(taskid, companyid, parseObject.getString("message"));
						else{
							INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
							insbPolicyitem.setTaskid(taskid);
							insbPolicyitem.setInscomcode(companyid);
							insbPolicyitem = insbPolicyitemService.queryList(insbPolicyitem).get(0);
							INSBFlowerror queryInsbFlowerror = new INSBFlowerror();
							queryInsbFlowerror.setTaskid(insbPolicyitem.getTaskid());
							queryInsbFlowerror.setInscomcode(insbPolicyitem.getInscomcode());
							queryInsbFlowerror.setFlowcode("-1");
							List<INSBFlowerror> queryList = insbFlowerrorService.queryList(queryInsbFlowerror);
							for (INSBFlowerror dataInsbFlowerror : queryList) {
								if(!StringUtil.isEmpty(dataInsbFlowerror)){
									insbFlowerrorService.deleteById(dataInsbFlowerror.getId());
								}
							}
						}
					} catch (Exception e) {
						this.coreSyncErrorLog(taskid, companyid, result);
					}
				}else
					this.coreSyncErrorLog(taskid, companyid, "推送核心返回空信息");
			LogUtil.info("结束推核心任务回调接口：result="+result);
		} catch (Exception e) {
			LogUtil.info("推核心任务回调接口出错：result="+result);
			e.printStackTrace();
		}
		return "200";
	}
	private String getBuyRist(String taskid, String companyid) { 
		String res="";
		/*INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		insbCarinfo = insbCarinfoService.queryOne(insbCarinfo);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("vincode", insbCarinfo.getVincode());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		map.put("startdate", DateUtil.parse(calendar.get(Calendar.YEAR)+"-01-01","yyyy-MM-dd"));
		map.put("enddate", new Date());
		List<INSBCarinfo> insbCarinfoList = insbCarinfoDao.getByCreattime(map);*/
		INSBPolicyitem businessPolicy=null,jqPolicy=null;
//		for (INSBCarinfo insbCarinfo2 : insbCarinfoList) {
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setTaskid(taskid);
			policyitem.setInscomcode(companyid);
			policyitem.setRisktype("0");
			if(businessPolicy==null)businessPolicy = insbPolicyitemService.queryOne(policyitem);
			policyitem.setRisktype("1");
			if(jqPolicy==null)jqPolicy = insbPolicyitemService.queryOne(policyitem);
//		}
		
		if(businessPolicy!=null&&jqPolicy==null){
			res="01";//单商业
		}else if(businessPolicy==null&&jqPolicy!=null)
			res="02";//单交强
		else if(businessPolicy!=null&&jqPolicy!=null)
			res="03";//商业、交强合买
		return res;
	}
	private Map<String,String> getCustLoyalty(String taskid, String companyid) {
		Map<String,String> map = new HashMap<String,String>();

		INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//		insbLastyearinsureinfo.setTaskid(taskid);
		String maincompanyid = companyid.substring(0, 4);
//		insbLastyearinsureinfo.setSupplierid(maincompanyid);
//		insbLastyearinsureinfo = insblastyearinsureinfoService.queryOne(insbLastyearinsureinfo);
//		if(insbLastyearinsureinfo==null){
//			insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//			insbLastyearinsureinfo.setTaskid(taskid);
//			List<INSBLastyearinsureinfo> lastyearinsureinfoList = insblastyearinsureinfoService.queryList(insbLastyearinsureinfo);
//			if(lastyearinsureinfoList.size()>0)
//			insbLastyearinsureinfo = lastyearinsureinfoList.get(0);
//		}
		insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(insbLastyearinsureinfo!=null){
			if(!StringUtil.isEmpty(insbLastyearinsureinfo.getSupplierid())
					&&insbLastyearinsureinfo.getSupplierid().indexOf(maincompanyid)==0){
				map.put("custLoyalty", "01");
			}else{
				if(!StringUtil.isEmpty(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()))
						&&!"0".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype())))
					map.put("custLoyalty", "03");
				else
					map.put("custLoyalty", "02");
			}
			BigDecimal sylastclaimsum = new BigDecimal(insbLastyearinsureinfo.getSylastclaimsum()==null?"0":insbLastyearinsureinfo.getSylastclaimsum().toString());
			BigDecimal jqlastclaimsum = new BigDecimal(insbLastyearinsureinfo.getJqlastclaimsum()==null?"0":insbLastyearinsureinfo.getJqlastclaimsum().toString());
			map.put("payoutMoney", sylastclaimsum.add(jqlastclaimsum).setScale (2,   BigDecimal.ROUND_HALF_UP).toString());
			map.put("jqpayoutRate",StringUtil.isEmpty(insbLastyearinsureinfo.getJqclaimrate())?"":insbLastyearinsureinfo.getJqclaimrate().toString());
			map.put("sypayoutRate",StringUtil.isEmpty(insbLastyearinsureinfo.getSyclaimrate())?"":insbLastyearinsureinfo.getSyclaimrate().toString());
			map.put("accFrequencysy",StringUtil.isEmpty(insbLastyearinsureinfo.getSyclaimtimes())?"":insbLastyearinsureinfo.getSyclaimtimes().toString());
			map.put("accFrequencyjq",StringUtil.isEmpty(insbLastyearinsureinfo.getJqclaimtimes())?"":insbLastyearinsureinfo.getJqclaimtimes().toString());
		}else{
			map.put("custLoyalty", "");
			map.put("payoutMoney", "");
			map.put("jqpayoutRate","");
			map.put("sypayoutRate","");
			map.put("accFrequencysy","");
			map.put("accFrequencyjq","");
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
			if(resultInsbCarinfoList.size()>0&&"1".equals(resultInsbCarinfoList.get(0).getIsNew()))
				map.put("custLoyalty", "03");
		}
		
		return map;
	}

	private List<Map<String,Object>> getDeptTree(String taskid,String companyId){
		LogUtil.info("进入getDeptTree方法：taskid=" + taskid + ",companyid=" + companyId);
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, companyId);
		if(insbQuoteinfo==null)return null;
		
		List<Map<String,Object>> agencyOrgList = new ArrayList<Map<String,Object>>();
		INSCDept inscDept = new INSCDept();
		inscDept.setComcode(insbQuoteinfo.getDeptcode());
		inscDept = inscDeptService.queryOne(inscDept);
		if(inscDept==null||!"04".equals(inscDept.getComgrade())){
			LogUtil.info("getDeptTree方法：taskid=" + taskid + ",companyid=" + companyId+",获取的出单网点失败");
			return agencyOrgList;
		}
		Map<String,Object> inscDeptMap = new HashMap<String,Object>();
		inscDeptMap.put("comCode", StringUtil.isEmpty(inscDept.getComcode())?"":inscDept.getComcode());//机构代码
		inscDeptMap.put("orgType", String.valueOf(Integer.parseInt(inscDept.getComgrade())));//机构类型  0-事业部,1-平台公司,2-法人公司,3-分公司,4-网点
		inscDeptMap.put("fullName", StringUtil.isEmpty(inscDept.getComname())?"":inscDept.getComname());//机构全称
		agencyOrgList.add(inscDeptMap);
		INSCDept queryInscDept=null;
		while(true){
			queryInscDept = new INSCDept();
			queryInscDept.setComcode(inscDept.getUpcomcode());
			inscDept = inscDeptService.queryOne(queryInscDept);
			inscDeptMap = new HashMap<String,Object>();
			inscDeptMap.put("comCode", StringUtil.isEmpty(inscDept.getComcode())?"":inscDept.getComcode());//机构代码
			inscDeptMap.put("orgType", StringUtil.isEmpty(inscDept.getComgrade())?"0":String.valueOf(Integer.parseInt(inscDept.getComgrade())));//机构类型  0-事业部,1-平台公司,2-法人公司,3-分公司,4-网点
			inscDeptMap.put("fullName", StringUtil.isEmpty(inscDept.getComname())?"":inscDept.getComname());//机构全称
			agencyOrgList.add(inscDeptMap);
			if("00".equals(inscDept.getComgrade())||"01".equals(inscDept.getComtype()))break;//机构级别
		}
		LogUtil.info("关闭getDeptTree方法：taskid=" + taskid + ",companyid=" + companyId);
		return agencyOrgList;
	}

	private void coreSyncErrorLog(String taskid,String inscomcode,String errordesc){
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemService.queryList(insbPolicyitem);

		if (policyitemList == null || policyitemList.isEmpty()) {
			LogUtil.error(taskid+","+inscomcode+"没有查询到保单数据");
			return;
		}

		insbPolicyitem = policyitemList.get(0);

		INSBFlowerror queryInsbFlowerror = new INSBFlowerror();
		queryInsbFlowerror.setTaskid(insbPolicyitem.getTaskid());
		queryInsbFlowerror.setInscomcode(insbPolicyitem.getInscomcode());
		queryInsbFlowerror.setTaskstatus("pushtocore");
		List<INSBFlowerror> queryList = insbFlowerrorService.queryList(queryInsbFlowerror);
		if(queryList.size()>1){
			for (int i = 1; i < queryList.size(); i++) {
				INSBFlowerror insbFlowerror=queryList.get(i);
				insbFlowerrorService.deleteById(insbFlowerror.getId());
			}
		}
		INSBFlowerror dataInsbFlowerror =null;
		if(queryList.size()==0){
			dataInsbFlowerror = new INSBFlowerror();
		}else{
			dataInsbFlowerror=queryList.get(0);
		}
		dataInsbFlowerror.setOperator("admin");
		dataInsbFlowerror.setTaskid(insbPolicyitem.getTaskid());
		dataInsbFlowerror.setInscomcode(insbPolicyitem.getInscomcode());
		dataInsbFlowerror.setFlowcode("-1");
		dataInsbFlowerror.setFlowname("保单推送到核心失败");
		dataInsbFlowerror.setFiroredi("5");
		dataInsbFlowerror.setTaskstatus("pushtocore");
		dataInsbFlowerror.setResult("false");
		dataInsbFlowerror.setErrordesc(errordesc);
		dataInsbFlowerror.setErrorcode("-1");
		if(!StringUtil.isEmpty(dataInsbFlowerror.getId())){
			dataInsbFlowerror.setModifytime(new Date());
			insbFlowerrorService.updateById(dataInsbFlowerror);
		}else{
			dataInsbFlowerror.setCreatetime(new Date());
			insbFlowerrorService.insert(dataInsbFlowerror);
		}
	}

	@Override
	public String closstask(String taskid, String inscomcode) {
		//关闭工作流
		Map<String,String> map2 = new HashMap<String, String>();
		map2.put("mainprocessinstanceid", taskid);
		String result =null ;
		try {
			result = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url")+"/process/setProcessSuccess", map2);
		} catch (Exception e) {
		}
		LogUtil.info("任务手动关闭接口：关闭工作流 taskid=" + taskid + ",companyid=" + inscomcode +",url="+ValidateUtil.getConfigValue("workflow.url")+"/process/setProcessSuccess,param:"+JSONObject.toJSONString(map2)+",结果："+result);
		//推cif
		String pushtocif = interFaceService.pushtocif(taskid, inscomcode);
		LogUtil.info("====任务手动关闭接口：推cif====taskid="+taskid+",companyId="+inscomcode+",结果："+pushtocif);
		//节点状态改为反向关闭
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
		//设置流程轨迹备注与处理人
		WorkflowFeedbackUtil.setWorkflowFeedback(null, insbQuoteinfo.getWorkflowinstanceid(), "39", "Ready", "反向关闭", "反向关闭","admin");
		INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
		insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
		insbWorkflowsubtrack.setTaskcode("39");
		insbWorkflowsubtrack = insbWorkflowsubtrackService.queryOne(insbWorkflowsubtrack);
		if(insbWorkflowsubtrack==null){
			insbWorkflowsubtrack = new INSBWorkflowsubtrack();
			insbWorkflowsubtrack.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
			insbWorkflowsubtrack.setTaskcode("39");
			insbWorkflowsubtrack.setOperator("admin");
			insbWorkflowsubtrack.setCreatetime(new Date());
			insbWorkflowsubtrack.setMaininstanceid(taskid);
			insbWorkflowsubtrack.setTaskstate("Ready");
			insbWorkflowsubtrackService.insert(insbWorkflowsubtrack);
		}
		
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
		insbWorkflowsub = insbWorkflowsubService.queryOne(insbWorkflowsub);
		if(insbWorkflowsub!=null){
			insbWorkflowsub.setTaskcode("39");
			insbWorkflowsub.setTaskstate("Ready");
			insbWorkflowsub.setModifytime(new Date());
			insbWorkflowsub.setWfsubtrackid(insbWorkflowsubtrack.getId());//
			insbWorkflowsubService.updateById(insbWorkflowsub);
		}
		
		INSBWorkflowsubtrackdetail insbWorkflowsubtrackdetail = new INSBWorkflowsubtrackdetail();
		insbWorkflowsubtrackdetail.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
		insbWorkflowsubtrackdetail.setTaskcode("39");
		insbWorkflowsubtrackdetail.setOperator("admin");
		insbWorkflowsubtrackdetail.setCreatetime(new Date());
		insbWorkflowsubtrackdetail.setMaininstanceid(taskid);
		insbWorkflowsubtrackdetail.setTaskstate("Ready");
		insbWorkflowsubtrackdetail.setWfsubtrackid(insbWorkflowsubtrack.getId());//
		insbWorkflowsubtrackdetailService.insert(insbWorkflowsubtrackdetail);
		WorkFlow4TaskModel workFlow4TaskModel = new WorkFlow4TaskModel();
		workFlow4TaskModel.setMainInstanceId(taskid);
		workFlow4TaskModel.setSubInstanceId(insbQuoteinfo.getWorkflowinstanceid());
		workFlow4TaskModel.setProviderId(inscomcode);
		workFlow4TaskModel.setTaskName("核心反向关闭");
		workFlow4TaskModel.setTaskCode("39");
		workFlow4TaskModel.setTaskStatus("Ready");
		chnChannelService.callback(workFlow4TaskModel);
	
		LogUtil.info("任务手动关闭接口：关闭任务 mainInstanceId=" + taskid + ",instanceid=" + insbQuoteinfo.getWorkflowinstanceid());
		dispatchService.completedAllTaskByCore(taskid, insbQuoteinfo.getWorkflowinstanceid());
		return "success";
	}

	@Override
	public String validatBeforpush(String taskid,String inscomcode){
		String result="success";
		String errordesc="";
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> queryList = insbPolicyitemService.queryList(insbPolicyitem);
		INSBFlowerror flowerror = new INSBFlowerror();
		flowerror.setTaskid(taskid);
		flowerror.setInscomcode(inscomcode);
		flowerror.setFiroredi("6");
		List<INSBFlowerror> list = insbFlowerrorService.queryList(flowerror);
		for (INSBFlowerror insbFlowerror : list) {
			insbFlowerrorService.deleteById(insbFlowerror.getId());
		}
		if(queryList.size()==0){
			insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "保单不存在", "admin");
			result="fail";
			errordesc=errordesc+",保单不存在";
		}else
			for (INSBPolicyitem insbPolicyitem2 : queryList) {
				if(StringUtil.isEmpty(insbPolicyitem2.getPolicyno())){
					INSBFlowerror insbFlowerror = new INSBFlowerror();
					insbFlowerror.setTaskid(taskid);
					insbFlowerror.setInscomcode(inscomcode);
					insbFlowerror.setFiroredi("6");
					insbFlowerror.setErrordesc("保单号为空");
					if(insbFlowerrorService.queryList(insbFlowerror).size()==0){
						insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "保单号为空", "admin");
						result="fail";
						errordesc=errordesc+",保单号为空";
					}
				}
			}
		
		Map<String, Object> carInfo = (Map<String, Object>)this.getCarInfo(taskid, inscomcode).get("carInfo");
		/*String plateNum = (String)carInfo.get("plateNum");
		if(!"0".equals(plateNum)){
			String regEx = "^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(plateNum);
			if(!m.matches()){
				insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "车牌号包含特殊符号", "admin");
				result="fail";
				errordesc=errordesc+",车牌号包含特殊符号";
			}
		}*/
		Integer accFrequencysy=StringUtil.isEmpty(carInfo.get("accFrequencysy"))?0:Integer.valueOf((String) carInfo.get("accFrequencysy"));
		Integer accFrequencyjq=StringUtil.isEmpty(carInfo.get("accFrequencyjq"))?0:Integer.valueOf((String) carInfo.get("accFrequencyjq"));
		Integer accFrequency = Integer.valueOf(StringUtil.isEmpty((String)carInfo.get("accFrequency"))?"0":(String)carInfo.get("accFrequency"));
		if(accFrequency<accFrequencysy||accFrequency<accFrequencyjq){
			insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "车辆出险次数错误", "admin");
			result="fail";
			errordesc=errordesc+",车辆出险次数错误";
		}
		Map<String, Object> insuredPersonInfo = this.getInsuredPersonInfo(taskid, inscomcode, null);
		if(StringUtil.isEmpty(insuredPersonInfo.get("certKind"))){
			insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "被保人证件类型为空", "admin");
			result="fail";
			errordesc=errordesc+",被保人证件类型为空";
		}
		if(StringUtil.isEmpty(insuredPersonInfo.get("fullName"))){
			insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "被保人姓名为空", "admin");
			result="fail";
			errordesc=errordesc+",被保人姓名为空";
		}
		if(StringUtil.isEmpty(insuredPersonInfo.get("certNumber"))){
			insbFlowerrorService.insertInsbFlowerror(taskid, inscomcode, "-2", "保单校验失败", "6", "validitbeforepush", "false", "被保人证件号为空", "admin");
			result="fail";
			errordesc=errordesc+",被保人证件号为空";
		}
		if("success".equals(result))
			return result;
		else
			return errordesc.substring(1);
	}
	@Override
	public String feeserverinfo(String requestJson) {
		// TODO Auto-generated method stub
		return null;
	}
}