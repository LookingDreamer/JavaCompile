package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ModelUtil;
import com.zzb.app.dao.AppFastRenewalDao;
import com.zzb.app.model.InsureConfigModel;
import com.zzb.app.model.bean.ApplicantInfo;
import com.zzb.app.model.bean.CarBasicInfo;
import com.zzb.app.model.bean.DriverInfo;
import com.zzb.app.model.bean.DriverOwer;
import com.zzb.app.model.bean.DriversList;
import com.zzb.app.model.bean.InsureAddress;
import com.zzb.app.model.bean.InsureConfig;
import com.zzb.app.model.bean.InsureDate;
import com.zzb.app.model.bean.InsureType;
import com.zzb.app.model.bean.InsuredInfo;
import com.zzb.app.model.bean.RecentInsure;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.app.model.bean.SupplierIdList;
import com.zzb.app.service.AppFastRenewalService;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBCarconfigDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBSpecifydriverDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBCarconfig;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskitem;

@Service
@Transactional
public class AppFastRenewalServiceImpl implements AppFastRenewalService{

	@Resource
	private AppFastRenewalDao appFastRenewalDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBAgentDao agentDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao; 
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBSpecifydriverDao insbSpecifydriverDao;
	@Resource
	private INSBCarconfigDao insbCarconfigDao;
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	
	/**
	 * zone=440100;channel=2;jobId=620005858;comCode=1244191733
	 */
	@Override
	public String getAllProviderList(String needVals) {
		String jobNum = ModelUtil.splitString(needVals, "jobId");
		Map<String,String> map =  new HashMap<String, String>();
		map.put("jobnum", jobNum);
		String comCode = ModelUtil.splitString(needVals, "comCode");
		map.put("stationid", comCode);
		String channel = ModelUtil.splitString(needVals, "channel");
		map.put("channeltype", channel);
		//todo
		String zone = ModelUtil.splitString(needVals, "zone");
		map.put("livingcityid", zone);
		String insureWay = ModelUtil.splitString(needVals, "insureWay");
		map.put("businesstype", insureWay);
		String isAvailable = ModelUtil.splitString(needVals, "isAvailable");
		map.put("isAvailable", isAvailable);
		List<Map<Object,Object>> proveridList = appFastRenewalDao.getAllProviderList(map);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<Object,Object> m : proveridList){
			Map<String, Object> mprover = new HashMap<String, Object>();
			List<INSBProvider> providerList = appFastRenewalDao.getChildProviderList(m.get("id").toString());
			/**
			 * "selfSelectLocation": true,
					"insBranchName": "鼎和财险广东",
					"productDescription": "",
					"promotion": 促销信息列表,
					"enable": "1",
					"class": "com.baoxian.provider.inscar.model.SimpleProvider",
					"insureInfo": null,
					"insBranchCode": "208844",
					"id": "101",
					"productFeatures": "",
					"insureSuccessInfo": null,
					"quotationInterval": "5",
					"requiredInsConfig": false,
					"effectiveTime": "2015-07-06 14:40:52",
					"renewal": false,//是否快速续保
					"insureWay": "human",
					"directInsure": false
			 */
			List<Map<String, Object>> newProList = new ArrayList<Map<String,Object>>();
			for(INSBProvider provider : providerList){
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("selfSelectLocation", "");//？？
				temp.put("insBranchName", provider.getPrvshotname());
				temp.put("productDescription", provider.getNoti());
				temp.put("promotion", "");//？？
				temp.put("enable", "");//？？
				temp.put("class", "");//？？
				temp.put("insureInfo", "");//？？
				temp.put("insBranchCode", "");//？？
				temp.put("id", provider.getId());
				temp.put("productFeatures", "");//？？
				temp.put("insureSuccessInfo", "");//？？
				temp.put("quotationInterval", provider.getQuotationtime());//？？
				temp.put("requiredInsConfig", "");//？？
				temp.put("effectiveTime", provider.getQuotationvalidity());//？？
				temp.put("renewal", "");//？？
				temp.put("insureWay", provider.getPrvtype());//？？
				temp.put("directInsure", "");//？？
				newProList.add(temp);
			}
			mprover.put("id", m.get("id").toString());
			mprover.put("providers", newProList);
			mprover.put("insComCode", m.get("prvcode").toString());
			mprover.put("name", m.get("prvshotname").toString());
			resultList.add(mprover);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "PROVIDER");
		mapHead.put("CmdId", "search");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("result", resultList);
		mapBody.put("Provider", mapResult);
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	/** 
	 * id=101;cityCode=undefined
	 * @see com.zzb.app.service.AppFastRenewalService#queryPayExpressinfo(java.lang.String)
	 */
	@Override
	public String queryPayExpressinfo(String needVals) {
		String id = ModelUtil.splitString(needVals, "id");
		String cityCode = ModelUtil.splitString(needVals, "cityCode");
		Map<String, String> data = new HashMap<String, String>();
		data.put("id", id);
		data.put("cityCode", cityCode);
		Map<String, Object> reponseBody = new HashMap<String, Object>();
		reponseBody.put("Code", "0");
		reponseBody.put("Text","OK");
		reponseBody.put("Type","0");
		Map<String, Object> MSM_HEADER = new HashMap<String, Object>();
		MSM_HEADER.put("CmdVer", "1.0.0");
		MSM_HEADER.put("CmdModule", "PROVIDER");
		MSM_HEADER.put("CmdId", "PaymentofDairlyInfo");
		MSM_HEADER.put("CmdType", "FHBX");
		reponseBody.put("MSM_HEADER", MSM_HEADER);
		List<Map<Object, Object>> paymentMethodList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> deliveryMethodList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> pay = appFastRenewalDao.queryPayExpressinfo(data);
		for (Map<Object, Object> m : pay) {
			Map<Object, Object> cc = new HashMap<Object, Object>();
			cc.put("concessionsDesc", m.get("concessionsDesc"));
			cc.put("desc", "");
			Map<String, String> tar = new HashMap<String, String>();
			if(m.get("payTarget")!=null){
				String paytarget = m.get("payTarget").toString();
				String target = appFastRenewalDao.queryPayTarget(paytarget);
				tar.put("name", target);
				tar.put("class", "com.baoxian.provider.inscar.model.PayTarget");
				tar.put("code", "insCorp");
			}else{
				tar.put("name", "");
				tar.put("class", "com.baoxian.provider.inscar.model.PayTarget");
				tar.put("code", "insCorp");
			}
			cc.put("payTarget", tar);
			cc.put("class", "com.baoxian.provider.inscar.model.PaymentMethod");
			Map<String, String> payee = new HashMap<String, String>(); 
			cc.put("payee", payee);
			cc.put("code", "Mobile99bill");
			//
			if(m.get("payChannel")!=null){
				String paychannelid = m.get("payChannel").toString();
				INSBPaychannel insbPay = appFastRenewalDao.queryPay(paychannelid);
				if(insbPay!=null){
					cc.put("payChannel", insbPay.getPaychannelname());
					cc.put("selected", insbPay.getStateflag());//是否启用 0启用 1不启用
					cc.put("name", insbPay.getPaychannelname());
					cc.put("isPayOffline", insbPay.getPaytype());//支付类型
				}
			}
			cc.put("autoGetPaymentResult", "true");
			cc.put("displayOrder", m.get("displayOrder"));
			cc.put("serviceProvider", "null");
			cc.put("canChanged", "1");
			Map<Object, Object> del = new HashMap<Object, Object>();
			if(m.get("deliveryFreight")!=null){
				String deliveryFreight = m.get("deliveryFreight").toString();
				if("1".equals(deliveryFreight)||"1"==deliveryFreight){
					del.put("deliveryFreight", "自取");
					del.put("name", "自取");
					del.put("code", "SelfTake");
				}else{
					del.put("deliveryFreight", "快递");
					del.put("name", "快递");
					del.put("code", "Express");
				}
			}else{
				del.put("deliveryFreight","费用收取方式");
				del.put("name", "名称");
				del.put("deliveryCost", "快递方式编码");
			}
			del.put("formDelivery", "配送方，暂时没有数据");
			del.put("desc", m.get("desc"));
			del.put("code", "Express");
			paymentMethodList.add(cc);
			deliveryMethodList.add(del);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("paymentMethodList", paymentMethodList);
		List<Map<Object, Object>> organizationalUnitList = new ArrayList<Map<Object,Object>>();
		result.put("organizationalUnitList", organizationalUnitList);
		result.put("deliveryMethodList", deliveryMethodList);
		Map<String, Object> PaymentofDairlyInfo = new HashMap<String, Object>();
		PaymentofDairlyInfo.put("PaymentofDairlyInfo", result);
		reponseBody.put("Body", PaymentofDairlyInfo);
		JSONObject jsonObject = JSONObject.fromObject(reponseBody);
		return jsonObject.toString();
	}

	/**
	 * uuid=B88C62ADF49D4343AA76829209CCD614
	 * @see com.zzb.app.service.AppFastRenewalService#queryShippingAddressinfo(java.lang.String)
	 */
	@Override
	public String queryShippingAddressinfo(String needVals) {
		String uuit = ModelUtil.splitString(needVals, "uuid");
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> map = new HashMap<String, String>();
		map.put("CmdVer", "1.0.0");
		map.put("CmdVer", "DELIVERY");
		map.put("CmdId", "METHOD");
		map.put("CmdVer", "FHBX");
		result.put("MSM_HEADER", map);
		List<Map<Object, Object>> addresses = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> model = appFastRenewalDao.ShippingAddressinfo(uuit);	
		for (Map<Object, Object> m : model) {
			Map<Object, Object> sumap = new HashMap<Object, Object>();
			sumap.put("id", "");
			sumap.put("address", m.get("address"));
			sumap.put("isDefault", "false");
			sumap.put("provinceId", m.get("provinceId"));
			sumap.put("cityId", m.get("cityId"));
			sumap.put("contactName", m.get("contactName"));
			sumap.put("postCode", m.get("postCode"));
			sumap.put("contactInfoId", "");
			sumap.put("contactTel", m.get("contactTel"));
			sumap.put("zoneId", m.get("zoneId"));
			addresses.add(sumap);
		}
		Map<String, Object> success = new HashMap<String, Object>();
		success.put("resp_code", "0000");
		success.put("Success", addresses);
		Map<String, Object> Body = new HashMap<String, Object>();
		Body.put("addresses", success);
		result.put("Body", Body);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String getTaskId(String needVals) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> map = new HashMap<String, String>();
		map.put("CmdModule", "MULTIQUOTES");
		map.put("CmdId", "Create");
		map.put("CmdType", "FHBX");
		result.put("MSM_HEADER", map);
		Map<String, String> mapBody = new HashMap<String, String>();
		//TODO 公共方法获取任务id
		mapBody.put("MultiQuoteId", "cfd09df69ae540a48a514efe1f410019");
		result.put("MSM_HEADER", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String queryAgentPowerById(String needVals) {
		String agentid = ModelUtil.splitString(needVals, "functional_id");
		//直接返回整个对象信息
		List<Map<String, Object>> model = appFastRenewalDao.queryAgentPowerByAgentId(agentid);
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		for(Map<String,Object> map : model){
//			//TODO 列表数据格式不统一，不知道字段代表意思，需要确定在完成
//			Map<String,Object> newMap = new HashMap<String, Object>();
//			newMap.put("", map.get(""));
//			resultList.add(map);
//		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "ATM");
		mapHead.put("CmdId", "GetFunctionAPI");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", model);
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String bindingTaskIdAndAgentId(String needVals) {
		String taskId = ModelUtil.splitString(needVals, "multiQuoteId");
		String agentId = ModelUtil.splitString(needVals, "accountId");
		INSBAgent agent = agentDao.selectById(agentId);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setCreatetime(new Date());
		insbPolicyitem.setOperator(agent.getName());
		insbPolicyitem.setAgentnum(agent.getJobnum());
		insbPolicyitem.setAgentname(agent.getName());
		insbPolicyitem.setTaskid(taskId);
		//险种类型不能为空
		insbPolicyitem.setRisktype("");
		insbPolicyitemDao.insert(insbPolicyitem);
		System.out.println("====insert INSBPolicyitem id ===="+insbPolicyitem.getId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "Binding");
		mapHead.put("CmdVer", "100");
		mapHead.put("Token", "test");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String bindingTaskIdAndProviderIds(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		INSBPolicyitem insbPolicyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setCreatetime(new Date());
		insbQuotetotalinfo.setOperator(insbPolicyitem.getAgentname());
		insbQuotetotalinfo.setAgentnum(insbPolicyitem.getAgentnum().toString());
		insbQuotetotalinfo.setTaskid(taskid);
		insbQuotetotalinfo.setAgentname(insbPolicyitem.getAgentname());
		insbQuotetotalinfoDao.insert(insbQuotetotalinfo);
		LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());
		System.out.println("=== insbQuotetotalinfo insert id =="+insbQuotetotalinfo.getId());
		SupplierIdList idList = new SupplierIdList();
		if(!"".equals(needVals)){
			List<INSBQuoteinfo> quoteinfos = new ArrayList<INSBQuoteinfo>();
			idList = ModelUtil.toBean(content, SupplierIdList.class);
			for(String id : idList.getRow()){
				INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
				insbQuoteinfo.setCreatetime(new Date());
				insbQuoteinfo.setOperator(insbPolicyitem.getAgentname());
				insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
				insbQuoteinfo.setInscomcode(id);
				quoteinfos.add(insbQuoteinfo);
			}
			insbQuoteinfoDao.insertInBatch(quoteinfos);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "SupplierIdList");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String saveInsuranceArea(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(quoteinfo);
		InsureAddress insureAddress = ModelUtil.toBean(content, InsureAddress.class);
		for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
			insbQuoteinfo.setInsprovincecode(insureAddress.getProvinceCode());
			insbQuoteinfo.setInsprovincename(insureAddress.getProvinceName());
			insbQuoteinfo.setInscitycode(insureAddress.getCityCode());
			insbQuoteinfo.setInscityname(insureAddress.getCityName());
			insbQuoteinfoDao.updateById(insbQuoteinfo);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdVer", "100");
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("Token", "test");
		mapHead.put("CmdId", "InsureAddress");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String saveInsuranceDate(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		INSBPolicyitem insbPolicyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
		InsureDate insureDate = ModelUtil.toBean(content, InsureDate.class);
		//交强险和商业险在保单表对应两条保单信息 0商业险 1交强险
		INSBPolicyitem policyitem = null;
		//用户只选择商业险或者交强险 或者两种都选
		if(!StringUtil.isEmpty(insureDate.getBusinessInsEffectDate()) && !StringUtil.isEmpty(insureDate.getTrafficInsEffectDate())){//商业险交强险都选
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setStartdate(ModelUtil.conbertStringToDate(insureDate.getBusinessInsEffectDate()));
			if(!StringUtil.isEmpty(insureDate.getBusinessInsInvalidDate())){
				insbPolicyitem.setEnddate(ModelUtil.conbertStringToDate(insureDate.getBusinessInsInvalidDate()));
			}
			//交强险
			policyitem = new INSBPolicyitem();
			policyitem.setCreatetime(new Date());
			policyitem.setOperator(insbPolicyitem.getAgentname());
			policyitem.setAgentnum(insbPolicyitem.getAgentnum());
			policyitem.setAgentname(insbPolicyitem.getAgentname());
			policyitem.setTaskid(taskid);
			policyitem.setRisktype("1");
			policyitem.setStartdate(ModelUtil.conbertStringToDate(insureDate.getTrafficInsEffectDate()));
			if(!StringUtil.isEmpty(insureDate.getTrafficInsInvalidDate())){
				policyitem.setEnddate(ModelUtil.conbertStringToDate(insureDate.getTrafficInsInvalidDate()));
			}
		}else if(!StringUtil.isEmpty(insureDate.getBusinessInsEffectDate()) && StringUtil.isEmpty(insureDate.getTrafficInsEffectDate())){//只选商业险
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setStartdate(ModelUtil.conbertStringToDate(insureDate.getBusinessInsEffectDate()));
			if(!StringUtil.isEmpty(insureDate.getBusinessInsInvalidDate())){
				insbPolicyitem.setEnddate(ModelUtil.conbertStringToDate(insureDate.getBusinessInsInvalidDate()));
			}
		}else{//只选交强险
			insbPolicyitem.setRisktype("1");
			insbPolicyitem.setStartdate(ModelUtil.conbertStringToDate(insureDate.getTrafficInsEffectDate()));
			if(!StringUtil.isEmpty(insureDate.getTrafficInsInvalidDate())){
				insbPolicyitem.setEnddate(ModelUtil.conbertStringToDate(insureDate.getTrafficInsInvalidDate()));
			}
		}
		insbPolicyitemDao.updateById(insbPolicyitem);
		if(null != policyitem){
			insbPolicyitemDao.insert(policyitem);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "InsureDate");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String saveInsurancePeopleInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(content.startsWith("<insuredInfo>")){
			InsuredInfo insuredInfo = ModelUtil.toBean(content, InsuredInfo.class);
			INSBPerson insbPerson = new INSBPerson();
			insbPerson.setCreatetime(new Date());
			insbPerson.setOperator(policyitems.get(0).getAgentname());
			insbPerson.setTaskid(taskid);
			insbPerson.setName(insuredInfo.getPersonInfo().getName());
			insbPerson.setIdcardtype(Integer.parseInt(insuredInfo.getPersonInfo().getCertificateType()));
			insbPerson.setIdcardno(insuredInfo.getPersonInfo().getCertNumber());
			insbPerson.setCellphone(insuredInfo.getPersonInfo().getTel());
			insbPerson.setEmail(insuredInfo.getPersonInfo().getEmail());
			//前端没传性别信息，目前全是男
			insbPerson.setGender(1);
			insbPersonDao.insert(insbPerson);
			//插入信息到被保人表
			INSBInsured insbInsured = new INSBInsured();
			insbInsured.setCreatetime(new Date());
			insbInsured.setOperator(policyitems.get(0).getAgentname());
			insbInsured.setTaskid(taskid);
			insbInsured.setPersonid(insbPerson.getId());
			insbInsuredDao.insert(insbInsured);
			//向保单表插入被保人数据
			for(INSBPolicyitem policyitem : policyitems){
				policyitem.setInsuredid(insbInsured.getId());
				policyitem.setInsuredname(insbPerson.getName());
				insbPolicyitemDao.updateById(policyitem);
			}
		}else{
			ApplicantInfo applicantInfo = ModelUtil.toBean(content, ApplicantInfo.class);
			INSBPerson insbPerson = new INSBPerson();
			insbPerson.setCreatetime(new Date());
			insbPerson.setOperator(policyitems.get(0).getAgentname());
			insbPerson.setTaskid(taskid);
			insbPerson.setName(applicantInfo.getPersonInfo().getName());
			insbPerson.setIdcardtype(Integer.parseInt(applicantInfo.getPersonInfo().getCertificateType()));
			insbPerson.setIdcardno(applicantInfo.getPersonInfo().getCertNumber());
			insbPerson.setCellphone(applicantInfo.getPersonInfo().getTel());
			insbPerson.setEmail(applicantInfo.getPersonInfo().getEmail());
			//前端没传性别信息，目前全是男
			insbPerson.setGender(1);
			insbPersonDao.insert(insbPerson);
			//插入信息到投保人表
			INSBApplicant insbApplicant = new INSBApplicant();
			insbApplicant.setCreatetime(new Date());
			insbApplicant.setOperator(policyitems.get(0).getAgentname());
			insbApplicant.setTaskid(taskid);
			insbApplicant.setPersonid(insbPerson.getId());
			insbApplicantDao.insert(insbApplicant);
			//向保单表插入投保人数据
			for(INSBPolicyitem policyitem : policyitems){
				policyitem.setApplicantid(insbApplicant.getId());
				policyitem.setApplicantname(insbPerson.getName());
				insbPolicyitemDao.updateById(policyitem);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "insuredInfo");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 联系人信息和投保人信息一致，向联系人表插入关联信息即可
	 */
	@Override
	public String saveInsuranceContactsInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
//		String content = ModelUtil.splitString(needVals, "content");
//		ContactsInfo contactsInfo = ModelUtil.toBean(content, ContactsInfo.class);
		INSBApplicant insbApplicant = new INSBApplicant();
		insbApplicant.setTaskid(taskid);
		insbApplicant = insbApplicantDao.selectOne(insbApplicant);
		INSBRelationperson insbRelationperson = new INSBRelationperson();
		insbRelationperson.setCreatetime(new Date());
		insbRelationperson.setOperator(insbApplicant.getOperator());
		insbRelationperson.setPersonid(insbApplicant.getPersonid());
		insbRelationperson.setTaskid(taskid);
		insbRelationpersonDao.insert(insbRelationperson);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "ContactsInfo");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String saveDriverOwerInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		DriverOwer driverOwer = ModelUtil.toBean(content, DriverOwer.class);
		INSBPerson insbPerson = new INSBPerson();
		insbPerson.setCreatetime(new Date());
		insbPerson.setOperator(policyitems.get(0).getAgentname());
		insbPerson.setTaskid(taskid);
		insbPerson.setName(driverOwer.getPersonInfo().getName());
		insbPerson.setIdcardtype(Integer.parseInt(driverOwer.getPersonInfo().getCertificateType()));
		insbPerson.setIdcardno(driverOwer.getPersonInfo().getCertNumber());
		insbPerson.setCellphone(driverOwer.getPersonInfo().getTel());
		insbPerson.setEmail(driverOwer.getPersonInfo().getEmail());
		//前端没传性别信息，目前全是男
		insbPerson.setGender(1);
		insbPersonDao.insert(insbPerson);
		//插入信息到车主信息表
		INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
		insbCarowneinfo.setCreatetime(new Date());
		insbCarowneinfo.setOperator(policyitems.get(0).getAgentname());
		insbCarowneinfo.setTaskid(taskid);
		insbCarowneinfo.setPersonid(insbPerson.getId());
		insbCarowneinfoDao.insert(insbCarowneinfo);
		//插入车辆信息
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setCreatetime(new Date());
		insbCarinfo.setOperator(policyitems.get(0).getAgentname());
		insbCarinfo.setTaskid(taskid);
		insbCarinfo.setOwner(insbPerson.getId());
		insbCarinfo.setOwnername(insbPerson.getName());
		insbCarinfo.setPhonenumber(insbPerson.getCellphone());
		insbCarinfoDao.insert(insbCarinfo);
		LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());
		for(INSBPolicyitem policyitem : policyitems){
			//向保单表插入车主数据
			policyitem.setCarownerid(insbCarowneinfo.getId());
			policyitem.setCarownername(insbPerson.getName());
			//保单和车辆信息绑定
			policyitem.setCarinfoid(insbCarinfo.getId());
			insbPolicyitemDao.updateById(policyitem);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "ownerInfo");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String saveOtherCarInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		RecentInsure recentInsure = ModelUtil.toBean(content, RecentInsure.class);
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		insbCarinfo = insbCarinfoDao.selectOne(insbCarinfo);
		String drivingRegion = "";
		switch (recentInsure.getDrivingRegion()) {
		case "1":
			drivingRegion = "出入境";
			break;
		case "2":
			drivingRegion = "境内";
			break;
		case "3":
			drivingRegion = "省内";
			break;
		case "4":
			drivingRegion = "场内";
			break;
		case "5":
			drivingRegion = "固定线路";
			break;
		default:
			break;
		}
		insbCarinfo.setDrivingarea(drivingRegion);
		insbCarinfo.setMileage(recentInsure.getAvkt());
		insbCarinfo.setPreinscode(recentInsure.getLastComId());
		insbCarinfoDao.updateById(insbCarinfo);
		LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "RecentInsure");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * multiQuoteId=c6abd2bdb86a4cbdb92e0eb10e19bb4b;content=<drivers><row><name>88888</name><gender>2</gender><birthday>2014-09-09</birthday><driverTypeCode>C1</driverTypeCode><licenseNo>371326198308138219</licenseNo><licensedDate>2015-09-09</licensedDate></row></drivers>
	 */
	@Override
	public String updateDriverInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		DriversList driversList = ModelUtil.toBean(content, DriversList.class);
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		List<INSBSpecifydriver> specifydrivers = new ArrayList<INSBSpecifydriver>();
		for(DriverInfo driver : driversList.getDriverInfos()){
			INSBSpecifydriver insbSpecifydriver = new INSBSpecifydriver();
			insbSpecifydriver.setOperator(carinfo.getOperator());
			insbSpecifydriver.setCreatetime(new Date());
			insbSpecifydriver.setCarinfoid(carinfo.getId());
			insbSpecifydriver.setTaskid(taskid);
			INSBPerson insbPerson = new INSBPerson();
			insbPerson.setCreatetime(new Date());
			insbPerson.setOperator(carinfo.getOperator());
			insbPerson.setTaskid(taskid);
			insbPerson.setName(driver.getName());
			//2 女 1男
			insbPerson.setGender("2".equals(driver.getGender())?1:0);
			insbPerson.setBirthday(ModelUtil.conbertStringToNyrDate(driver.getBirthday()));
			insbPerson.setLicensetype(driver.getDriverTypeCode());
			insbPerson.setLicenseno(driver.getLicenseNo());
			insbPerson.setLicensedate(ModelUtil.conbertStringToNyrDate(driver.getLicensedDate()));
			//前端没传性别信息，目前全是男
			insbPerson.setGender(1);
			insbPersonDao.insert(insbPerson);
			insbSpecifydriver.setPersonid(insbPerson.getId());
			specifydrivers.add(insbSpecifydriver);
		}
		insbSpecifydriverDao.insertInBatch(specifydrivers);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdVer", "100");
		mapHead.put("Token", "test");
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "ownerInfo");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String saveBasicCarInfo(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		CarBasicInfo carBasicInfo = ModelUtil.toBean(content, CarBasicInfo.class);
//		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
//		insbPolicyitem.setTaskid(taskid);
//		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		carinfo.setCarlicenseno(carBasicInfo.getPlateNumber());
		carinfo.setVincode(carBasicInfo.getVin());
		carinfo.setStandardfullname(carBasicInfo.getLicenseModelCode());
		carinfo.setEngineno(carBasicInfo.getEngineNo());
		carinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(carBasicInfo.getFirstRegisterDate()));
		if("true".equals(carBasicInfo.getChgOwnerFlag())){//过户车
			carinfo.setIsTransfercar("1");
			carinfo.setTransferdate(ModelUtil.conbertStringToNyrDate(carBasicInfo.getChgOwnerDate()));
		}else{//非过户车
			carinfo.setIsTransfercar("0");
			carinfo.setTransferdate(new Date());
		}
		carinfo.setProperty(carBasicInfo.getInstitutionType());
		carinfo.setCarproperty(carBasicInfo.getUseProperty());
		carinfo.setNoti(carBasicInfo.getModelDesc());
		insbCarinfoDao.updateById(carinfo);
		LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
		//向车型表插入数据
		INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
		insbCarmodelinfo.setOperator(carinfo.getOperator());
		insbCarmodelinfo.setCreatetime(new Date());
		insbCarmodelinfo.setCarinfoid(carinfo.getId());
		insbCarmodelinfo.setBrandname(carBasicInfo.getModelCode());
		//insbCarmodelinfo.setStandardname(carBasicInfo.getModelCode());
		insbCarmodelinfo.setStandardfullname(carBasicInfo.getModelCode());
		insbCarmodelinfo.setFullweight(Double.parseDouble(carBasicInfo.getTotalVehicleWeight()));
		insbCarmodelinfo.setSeat(Integer.parseInt(carBasicInfo.getRatedPassengerCapacity()));
		insbCarmodelinfo.setDisplacement(Double.parseDouble(carBasicInfo.getDisplacement()));
		insbCarmodelinfo.setUnwrtweight(Double.parseDouble(carBasicInfo.getTonnage()));
		//一下字段都不能为空，默认都为1
		insbCarmodelinfo.setPrice(1.0);
		insbCarmodelinfo.setTaxprice(1.0);
		insbCarmodelinfo.setAnalogyprice(1.0);
		insbCarmodelinfo.setAnalogytaxprice(1.0);
		
		insbCarmodelinfoDao.insert(insbCarmodelinfo);
//		//获取投保日期
//		for(INSBPolicyitem policyitem : policyitems){
//			//商业险
//			if("0".equals(policyitem.getRisktype())){
//				carinfo.setBusinessstartdate(policyitem.getStartdate());
//				carinfo.setBusinessenddate(policyitem.getEnddate());
//			}
//			//交强险
//			if("1".equals(policyitem.getRisktype())){
//				carinfo.setStrongstartdate(policyitem.getStartdate());
//				carinfo.setStrongenddate(policyitem.getEnddate());
//			}
//		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "vehicleInfo");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 保存保险配置信息，并关联保险公司
	 * multiQuoteId=c6abd2bdb86a4cbdb92e0eb10e19bb4b;content=xml信息
	 */
	@Override
	public String updateInsureConfigure(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		String content = ModelUtil.splitString(needVals, "content");
		InsureConfig config = ModelUtil.toBean(content, InsureConfig.class);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		//保存备注信息
		for(INSBPolicyitem policyitem : policyitems){
			policyitem.setNoti(config.getRemarks().getRemarks().get(0).getValue());
		}
		String operator = policyitems.get(0).getAgentname();
		List<INSBCarconfig> insbCarconfigs = new ArrayList<INSBCarconfig>();
		List<INSBCarkindprice> insbCarkindprices = new ArrayList<INSBCarkindprice>();
		for(InsureType insureType : config.getEnsureItemList().getEnsureItemList()){
			INSBCarconfig insbCarconfig = new INSBCarconfig();
			INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
			insbCarconfig.setOperator(operator);
			insbCarconfig.setCreatetime(new Date());
			insbCarconfig.setTaskid(taskid);
			if(insureType.getCode().startsWith("Vehicle")){
				//交强险车船税
				insbCarconfig.setInskindtype("1");
				insbCarkindprice.setInskindtype("1");
			}else{
				//商业险
				insbCarconfig.setInskindtype("0");
				insbCarkindprice.setInskindtype("0");
			}
			//险别不计免赔
			if(insureType.getCode().startsWith("Ncf")){
				insbCarconfig.setNotdeductible("1");
				insbCarkindprice.setNotdeductible("1");
			}else{
				insbCarconfig.setNotdeductible("0");
				insbCarkindprice.setNotdeductible("0");
			}
			insbCarconfig.setInskindcode(insureType.getCode());
			
			//保存险别要素已选项
			List<SelectOption> list = new ArrayList<SelectOption>();
			SelectOption selectOption = new SelectOption();
			RisksData risksData = new RisksData();
			risksData.setKEY(insureType.getSelectedOption());
			risksData.setVALUE(insureType.getCoverage());
			risksData.setUNIT(insureType.getUnit());
			if("GlassIns".equals(insureType.getCode())){//玻璃 
				selectOption.setTYPE("02");
			}else{
				selectOption.setTYPE("01");
			}
			//只存保额
			if(!"1".equals(insureType.getCoverage()) && !"2".equals(insureType.getCoverage())){
				insbCarconfig.setAmount(insureType.getCoverage());
				insbCarkindprice.setAmount(Double.parseDouble(insureType.getCoverage()));
			}
			selectOption.setVALUE(risksData);
			list.add(selectOption);
			JSONArray jsonObject = JSONArray.fromObject(list);
			insbCarconfig.setSelecteditem(jsonObject.toString());
			insbCarconfigs.add(insbCarconfig);
			
			insbCarkindprice.setOperator(operator);
			insbCarkindprice.setCreatetime(new Date());
			insbCarkindprice.setTaskid(taskid);
			insbCarkindprice.setInskindcode(insureType.getCode());
			
			insbCarkindprice.setSelecteditem(jsonObject.toString());
			
			insbCarkindprices.add(insbCarkindprice);
		}
		//保险险别选择表插入基本配置
		insbCarconfigDao.insertInBatch(insbCarconfigs);
		//查询选择的报价公司
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo(); 
		insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
		List<INSBCarkindprice> carkindprices = new ArrayList<INSBCarkindprice>();
		for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
			for(INSBCarkindprice carkindprice : insbCarkindprices){
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setOperator(carkindprice.getOperator());
				insbCarkindprice.setCreatetime(carkindprice.getCreatetime());
				insbCarkindprice.setTaskid(carkindprice.getTaskid());
				insbCarkindprice.setInskindcode(carkindprice.getInskindcode());
				insbCarkindprice.setAmount(carkindprice.getAmount());
				insbCarkindprice.setSelecteditem(carkindprice.getSelecteditem());
				insbCarkindprice.setInscomcode(carkindprice.getInscomcode());
				insbCarkindprice.setInskindtype(carkindprice.getInskindtype());
				insbCarkindprice.setNotdeductible(carkindprice.getNotdeductible());
				insbCarkindprice.setInscomcode(quoteinfo.getInscomcode());
				carkindprices.add(insbCarkindprice);
			}
		}
		//保险公司险别报价表
		insbCarkindpriceDao.insertInBatch(carkindprices);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "InsureConfig");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String updateQuoteStatus(String needVals) {
		String taskid = ModelUtil.splitString(needVals, "multiQuoteId");
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo(); 
		insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
		//更新报价信息表状态
		for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
			quoteinfo.setTaskstatus("已提交");
			insbQuoteinfoDao.updateById(quoteinfo);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		Map<String, String> mapHead = new HashMap<String, String>();
		mapHead.put("CmdModule", "MULTIQUOTES");
		mapHead.put("CmdId", "Save");
		mapHead.put("CmdType", "FHBX");
		result.put("MSM_HEADER", mapHead);
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("Success", "true");
		result.put("Body", mapBody);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String commonInsureConfig(String plankey) {
		List<InsureConfigModel> insureConfigModels = appFastRenewalDao.queryInsureConfigByKey(plankey);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != insureConfigModels && insureConfigModels.size()>0){
			result.put("Code", "0");
			result.put("Text", "OK");
		}else{
			result.put("Code", "1");
			result.put("Text", "FAIL");
		}
		result.put("Body", insureConfigModels);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@Override
	public String supplementInfo(String taskid) {
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
		insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
		List<String> ids = new ArrayList<String>();
		for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
			ids.add(quoteinfo.getInscomcode());
		}
		List<INSBRiskitem> insbRiskitems = appFastRenewalDao.queryRiskItemsByProviderids(ids);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != insbRiskitems && insbRiskitems.size()>0){
			result.put("Code", "0");
			result.put("Text", "OK");
		}else{
			result.put("Code", "1");
			result.put("Text", "FAIL");
		}
		result.put("Body", insbRiskitems);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
}
