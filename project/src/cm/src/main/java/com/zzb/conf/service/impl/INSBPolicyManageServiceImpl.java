package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.entity.INSBAgentTask;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.service.INSBCarowneinfoService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBPolicyManageDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.model.PolicyDetailedModel;

@Service
@Transactional
public class INSBPolicyManageServiceImpl extends BaseServiceImpl<INSBPolicyitem> implements INSBPolicyManageService {

	@Resource
	private INSBPolicyManageDao insbPolicyManageDao;
	@Resource
	private INSBAgentDao agentDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBRiskkindDao insbRiskkindDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBPersonDao insbPersonDao; 
	@Resource
	private INSBSpecifydriverDao insbSpecifydriverDao;
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;
	@Resource
	private INSCDeptDao inscdeptDao;
	@Resource
	private INSBRegionDao insbRegionDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private INSCCodeDao insccodeDao;
	@Resource
	private INSBAgentTaskDao insbAgentTaskDao;	
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	
	@Override
	protected BaseDao<INSBPolicyitem> getBaseDao() {
		// TODO Auto-generated method stub
		return insbPolicyManageDao;
	}
	
	@Override
	public String queryPagingList(Map<String, Object> map,String comcode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(comcode!=null&&!"".equals(comcode)){
			INSCDept dept = inscdeptDao.selectById(comcode);
			if(dept!=null&&dept.getDeptinnercode()!=null){
				map.put("parentcodes", dept.getDeptinnercode());
			}
		}
		long total = insbPolicyManageDao.queryPagingListCount(map);
		List<Map<Object, Object>> infoList = insbPolicyManageDao.queryPagingList(map);
		resultMap.put("total", total);
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
	    return jsonObject.toString();
	}

	@Override
	public PolicyDetailedModel queryPolictDetailedByPId(String id) {
		INSBPolicyitem insbPolicyitem = insbPolicyManageDao.selectById(id);
		if(null != insbPolicyitem){
			return queryDetailedByOId(insbPolicyitem.getOrderid(),insbPolicyitem.getInscomcode());
		}
		LogUtil.info("this id is not have policy info");
		return new PolicyDetailedModel();
	}

	private PolicyDetailedModel queryDetailedByOId(String orderid,String proid) {
		PolicyDetailedModel model = new PolicyDetailedModel();
		try {
			INSBOrder insbOrder = insbOrderDao.queryOrderByid(orderid,proid);
			if(null != insbOrder){
				//订单表
				model.setInsbOrder(insbOrder);

				//保单信息
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderid", orderid);

				Map<String, String> maphis = new HashMap<String, String>();
				maphis.put("taskid", insbOrder.getTaskid());
				maphis.put("proid", proid);

//				受益人
				INSBPolicyitem benefitPolicyitemhis = insbPolicyManageDao.selectPolcyByOIdAndType2his(maphis);
				if(benefitPolicyitemhis!=null){
					model.setBenefitPolicyitem(benefitPolicyitemhis);
				}else{
					INSBPolicyitem benefitPolicyitem = insbPolicyManageDao.selectPolcyByOIdAndType2(map);
					model.setBenefitPolicyitem(benefitPolicyitem);
				}
//				被保人
				INSBPolicyitem recognizeePolicyitemhis = insbPolicyManageDao.selectRecognizeeByorderidhis(maphis);
				if(recognizeePolicyitemhis!=null){
					model.setRecognizeePolicyitem(recognizeePolicyitemhis);
				}else{
					INSBPolicyitem recognizeePolicyitem = insbPolicyManageDao.selectRecognizeeByorderid(map);
					model.setRecognizeePolicyitem(recognizeePolicyitem);
				}
//				投保人
				INSBPolicyitem insurePolicyitemhis = insbPolicyManageDao.selectInsureByorderidhis(maphis);
				if(insurePolicyitemhis!=null){
					model.setInsurePolicyitem(insurePolicyitemhis);
				}else{
					INSBPolicyitem insurePolicyitem = insbPolicyManageDao.selectInsureByorderid(map);
					model.setInsurePolicyitem(insurePolicyitem);
				}
//				联系人
				INSBPolicyitem contactsPolicyitemhis = insbPolicyManageDao.selectContactsByTask(maphis);
				if(contactsPolicyitemhis!=null){
					model.setContactsPolicyitem(contactsPolicyitemhis);
				}else{
					INSBPolicyitem contactsPolicyitem = insbPolicyManageDao.selectContactsByOrderid(map);
					model.setContactsPolicyitem(contactsPolicyitem);
				}

				//商业险
				map.put("type", "0");
				INSBPolicyitem syPolicyitem = insbPolicyManageDao.selectPolcyByOIdAndType(map);
				model.setInsbPolicyitem(syPolicyitem);
				//交强险
				map.put("type", "1");
				INSBPolicyitem jqPolicyitem = insbPolicyManageDao.selectPolcyByOIdAndType(map);
				model.setJqPolicyitem(jqPolicyitem);
				//供应商
				INSBProvider insbProvider =  insbProviderDao.selectById(insbOrder.getPrvid());
				model.setInsbProvider(insbProvider);
				//代理人信息
				INSBAgent agent = insbPolicyManageDao.selectAgentByNum(insbOrder.getAgentcode());

				//mini代理人信息
				INSBAgentTask agentTaskquery = new INSBAgentTask();
				if (StringUtil.isNotEmpty(insbOrder.getTaskid())) {
					agentTaskquery.setTaskid(insbOrder.getTaskid());
					INSBAgentTask agentTask = insbAgentTaskDao.selectOne(agentTaskquery);
					if (agentTask != null && StringUtil.isNotEmpty(agentTask.getAgentid())) {
						INSBAgent miniagent = agentDao.selectById(agentTask.getAgentid());
						model.setMiniagentcode(miniagent == null ? "" : miniagent.getJobnum());
					}
				}

				// 取得报价信息总表信息
				INSBQuotetotalinfo quotetotalinfoTemp = new INSBQuotetotalinfo();
				quotetotalinfoTemp.setTaskid(insbOrder.getTaskid());
				quotetotalinfoTemp = insbQuotetotalinfoDao.selectOne(quotetotalinfoTemp);
				if (quotetotalinfoTemp != null) {//设置渠道来源
					if (quotetotalinfoTemp.getPurchaserChannel() != null) {
						agent.setPurchaserchannel(quotetotalinfoTemp.getPurchaserchannelName());//加渠道来源 askqvn20160516
					}
				}
				
				model.setAgent(agent);
				//机构表
				if(agent!=null){
					INSCDept dept = inscdeptDao.selectById(agent.getDeptid());
					//投保地区
					//省
					if(dept!=null){
						INSBRegion regionp = insbRegionDao.selectById(dept.getProvince());
						if(regionp!=null){
							String province = regionp.getComcodename();
							model.setProvince(province);
						}
						//市
						INSBRegion regionc = insbRegionDao.selectById(dept.getCity());
						if(regionc!=null){
							String city = regionc.getComcodename();
							model.setCity(city);
						}
					}
					
				}
				String taskid = insbOrder.getTaskid();
				// 快递信息
				Map<String, String> params = new HashMap<String, String>();
				params.put("mainInstanceId", taskid);	
				params.put("orderId", orderid);
				String orderdeliveryaddress =insbOrderdeliveryDao.getOrderdeliveryAddress(params);
				if(orderdeliveryaddress!=null && !"".equals(orderdeliveryaddress)){
					model.setOrderdeliveryaddress(orderdeliveryaddress);// 快递地址
				}
				INSBOrderdelivery insbOrderdelivery= insbOrderdeliveryDao.getOrderdelivery(params);
				if(insbOrderdelivery!=null){
					model.setInsbOrderdelivery(insbOrderdelivery);
					if(!"".equals(insbOrderdelivery.getDeptcode()) && insbOrderdelivery.getDeptcode()!=null ){			
						INSCDept inscdept = inscdeptDao.selectByCode( insbOrderdelivery.getDeptcode());
						if(inscdept!=null && inscdept.getComname()!=null && !"".equals(inscdept.getComname())){
							model.setAddresscodename(inscdept.getComname());//自取网点
						}
						if(inscdept!=null && inscdept.getAddress()!=null && !"".equals(inscdept.getAddress())){
							model.setAddress(inscdept.getAddress());//自取网点
						}
					}
					if(insbOrderdelivery.getLogisticscompany()!=null && !"".equals(insbOrderdelivery.getLogisticscompany()) ){
						String codename =insccodeDao.getCodenameByLogisticscompany(insbOrderdelivery.getLogisticscompany());
						if(codename!=null && !"".equals(codename)){
							model.setCodenamestyle(codename);//物流公司
						}	
					}
				}
				
				//车辆信息
				INSBCarinfohis carinfohis = insbPolicyManageDao.selectCarInfoByhis(taskid,proid);
				if(carinfohis==null){
					INSBCarinfo carinfo = insbPolicyManageDao.selectCarInfoByTaskId(taskid);
					carinfohis = new INSBCarinfohis();
					try {
						PropertyUtils.copyProperties(carinfohis, carinfo);
					} catch (Exception e) {
						LogUtil.info("--订单车辆信息出错--");
					}
				}
				//获取车主名称
				INSBPerson insbPerson = insbPersonService.selectCarOwnerPersonByTaskId(taskid);
				if(insbPerson!=null){
					carinfohis.setOwnername(insbPerson.getName());
				}
				model.setCarinfohis(carinfohis);
			}
		} catch (Exception e) {
			LogUtil.info("orderid=" + orderid + ",proid=" + proid);
			e.printStackTrace();
		}
		return model;
	}

	private PolicyDetailedModel queryDetailedByOId2(String orderid,String proid) {
		PolicyDetailedModel model = new PolicyDetailedModel();
		try {
			INSBPolicyitem po = insbPolicyitemDao.selectById(orderid);
			if(null != po){
				
				Map<String, String> maphis = new HashMap<String, String>();
				maphis.put("taskid", po.getTaskid());
				maphis.put("proid", proid);
//				被保人
				INSBPolicyitem recognizeePolicyitemhis = insbPolicyManageDao.selectRecognizeeByorderidhis(maphis);
				if(recognizeePolicyitemhis!=null){
					model.setRecognizeePolicyitem(recognizeePolicyitemhis);
				}else{
					INSBPolicyitem recognizeePolicyitem = insbPolicyManageDao.selectRecognizeeBypoid(orderid);
					model.setRecognizeePolicyitem(recognizeePolicyitem);
				}
//				投保人
				INSBPolicyitem insurePolicyitemhis = insbPolicyManageDao.selectInsureByorderidhis(maphis);
				if(insurePolicyitemhis!=null){
					model.setInsurePolicyitem(insurePolicyitemhis);
				}else{
					INSBPolicyitem insurePolicyitem = insbPolicyManageDao.selectInsureBypoid(orderid);
					model.setInsurePolicyitem(insurePolicyitem);
				}
//				受益人
				INSBPolicyitem benefitPolicyitemhis = insbPolicyManageDao.selectPolcyByOIdAndType2his(maphis);
				if(benefitPolicyitemhis!=null){
					model.setBenefitPolicyitem(benefitPolicyitemhis);
				}else{
					INSBPolicyitem benefitPolicyitem = insbPolicyManageDao.selectPolcyBypId(orderid);
					model.setBenefitPolicyitem(benefitPolicyitem);
				}
//				联系人
				INSBPolicyitem contactsPolicyitemhis = insbPolicyManageDao.selectContactsByTask(maphis);
				if(contactsPolicyitemhis!=null){
					model.setContactsPolicyitem(contactsPolicyitemhis);
				}else{
					INSBPolicyitem contactsPolicyitem = insbPolicyManageDao.selectContactsBypoid(orderid);
					model.setContactsPolicyitem(contactsPolicyitem);
				}

//				保单信息
				model.setPolicyitem(po);
				//供应商
				INSBProvider insbProvider =  insbProviderDao.selectById(po.getInscomcode());
				model.setInsbProvider(insbProvider);
				//代理人信息
				INSBAgent agent = insbPolicyManageDao.selectAgentByNum(po.getAgentnum());
				model.setAgent(agent);
				//机构表
				if(agent!=null){
					INSCDept dept = inscdeptDao.selectById(agent.getDeptid());
					//投保地区
					//省
					if(dept!=null){
						INSBRegion regionp = insbRegionDao.selectById(dept.getProvince());
						if(regionp!=null){
							String province = regionp.getComcodename();
							model.setProvince(province);
						}
						//市
						INSBRegion regionc = insbRegionDao.selectById(dept.getCity());
						if(regionc!=null){
							String city = regionc.getComcodename();
							model.setCity(city);
						}
					}
					model.setInscdept(dept);
				}

				String taskid = po.getTaskid();
				//快递信息
				Map<String, String> params = new HashMap<String, String>();
				params.put("mainInstanceId", taskid);	
				params.put("orderId", orderid);
				String orderdeliveryaddress =insbOrderdeliveryDao.getOrderdeliveryAddress(params);
				if(orderdeliveryaddress!=null && !"".equals(orderdeliveryaddress)){
					model.setOrderdeliveryaddress(orderdeliveryaddress);//快递地址
				}
				INSBOrderdelivery insbOrderdelivery= insbOrderdeliveryDao.getOrderdelivery(params);
				if(insbOrderdelivery!=null){
					model.setInsbOrderdelivery(insbOrderdelivery);
					if(!"".equals(insbOrderdelivery.getDeptcode()) && insbOrderdelivery.getDeptcode()!=null ){			
						INSCDept inscdept = inscdeptDao.selectByCode( insbOrderdelivery.getDeptcode());
						if(inscdept!=null && inscdept.getComname()!=null && !"".equals(inscdept.getComname())){
							model.setAddresscodename(inscdept.getComname());//自取网点
						}
						if(inscdept!=null && inscdept.getAddress()!=null && !"".equals(inscdept.getAddress())){
							model.setAddress(inscdept.getAddress());//自取网点
						}
					}
					if(insbOrderdelivery.getLogisticscompany()!=null && !"".equals(insbOrderdelivery.getLogisticscompany()) ){
						String codename =insccodeDao.getCodenameByLogisticscompany(insbOrderdelivery.getLogisticscompany());
						if(codename!=null && !"".equals(codename)){
							model.setCodenamestyle(codename);//物流公司
						}	
					}
				}

				//车辆信息  先查历史表--若没有去查主表
				INSBCarinfohis carinfohis = insbPolicyManageDao.selectCarInfoByhis(taskid,proid);
				if(carinfohis==null){
					INSBCarinfo carinfo = insbPolicyManageDao.selectCarInfoByTaskId(taskid);
					carinfohis = new INSBCarinfohis();
					try {
						PropertyUtils.copyProperties(carinfohis, carinfo);
					} catch (Exception e) {
						LogUtil.info("--订单车辆信息出错--");
					}
				}
				//获取车主名称
				INSBPerson insbPerson = insbPersonService.selectCarOwnerPersonByTaskId(taskid);
				if(insbPerson!=null){
					carinfohis.setOwnername(insbPerson.getName());
				}
				model.setCarinfohis(carinfohis);
			}
		} catch (Exception e) {
			LogUtil.info(e.getMessage());
		}
		return model;
	}
	
	/**
	 * 通过保险公司id查询其名下可选险别信息
	 */
	private Map<String, List<Map<String, Object>>> getRiskkindInfoByProvideId(
			String provideId) {
		//打包所有分类险别信息
		Map<String, List<Map<String, Object>>> riskkindInfo = new HashMap<String, List<Map<String,Object>>>();
		//商业险险别列表
		List<Map<String, Object>> businessInsu = new ArrayList<Map<String,Object>>();
		//不计免赔列表
		List<Map<String, Object>> notdeductibleInsu = new ArrayList<Map<String,Object>>();
		//交强险和车船税列表
		List<Map<String, Object>> strongInsu = new ArrayList<Map<String,Object>>();
		INSBRisk risk = new INSBRisk();
		risk.setProvideid(provideId);
		List<INSBRisk> riskList = insbRiskDao.selectList(risk);
		if(riskList!=null && riskList.size()>0 && riskList.get(0)!=null){
			INSBRiskkind riskkind = new INSBRiskkind();
			riskkind.setRiskid(riskList.get(0).getId());
			List<INSBRiskkind> RiskkindList = insbRiskkindDao.selectList(riskkind);
			if(RiskkindList!=null && RiskkindList.size()>0){
				for (int i = 0; i < RiskkindList.size(); i++) {
					INSBRiskkind riskkindTemp = RiskkindList.get(i);
					Map<String, Object> temp = new HashMap<String, Object>();
					if(riskkindTemp!=null){
						if(riskkindTemp.getKindname()!=null){
							temp.put("riskkindname", riskkindTemp.getKindname());
						}
						if(riskkindTemp.getKindcode()!=null){
							temp.put("riskkindcode", riskkindTemp.getKindcode());
						}
						if("是".equals(riskkindTemp.getNotdeductible())){
							notdeductibleInsu.add(temp);
						}else{
							businessInsu.add(temp);
						}
					}
				}
			}
			Map<String, Object> strongInsuTemp1 = new HashMap<String, Object>();
			strongInsuTemp1.put("riskkindname", "交强险");
			strongInsuTemp1.put("riskkindcode", "001");
			Map<String, Object> strongInsuTemp2 = new HashMap<String, Object>();
			strongInsuTemp2.put("riskkindname", "车船税");
			strongInsuTemp2.put("riskkindcode", "002");
			strongInsu.add(strongInsuTemp1);
			strongInsu.add(strongInsuTemp2);
		}
		riskkindInfo.put("businessInsu", businessInsu);
		riskkindInfo.put("notdeductibleInsu", notdeductibleInsu);
		riskkindInfo.put("strongInsu", strongInsu);
		return riskkindInfo;
	}
	 
	@Override
	public PolicyDetailedModel queryOrderDetailedByOId(String orderid,String proid,String type) {
		if(type.equals("orderid")){
			return queryDetailedByOId(orderid,proid);
		}else{
			return queryDetailedByOId2(orderid,proid);
		}
	}

	@Override
	public INSBCarinfo queryCarInfoByTaskid(String taskid) {
		return insbPolicyManageDao.selectCarInfoByTaskId(taskid);
	}

	@Override
	public INSBPerson queryPersonInfoByTaskid(String taskid,String flag,String inscomcode) {
		//insure 被保险人, policy 投保人, carower 车主, shouyiren 受益人, contacts 联系人
		String personid = "";
		if("insure".equals(flag)){
			INSBInsuredhis his = null;
			INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
			insbInsuredhis.setTaskid(taskid);
			insbInsuredhis.setInscomcode(inscomcode);
			List<INSBInsuredhis> insbInsuredhislist = insbInsuredhisDao.selectList(insbInsuredhis);
			if(insbInsuredhislist!=null&&insbInsuredhislist.size()>0){
				his = insbInsuredhislist.get(0);
			}
			if(his!=null){
				personid = his.getPersonid();
			}else{
				INSBInsured insbInsured = insbInsuredDao.selectInsuredByTaskId(taskid);
				personid = insbInsured==null?"":insbInsured.getPersonid();
			}
		}else if("policy".equals(flag)){
			INSBApplicanthis his = null;
			INSBApplicanthis applicanthis = new INSBApplicanthis();
			applicanthis.setTaskid(taskid);
			applicanthis.setInscomcode(inscomcode);
			List<INSBApplicanthis> list = insbApplicanthisDao.selectList(applicanthis);
			if(list!=null&&list.size()>0){
				his = list.get(0);
			}
			if(his!=null){
				personid = his.getPersonid();
			}else{
				INSBApplicant applicant = new INSBApplicant();
				applicant.setTaskid(taskid);
				applicant = insbApplicantDao.selectOne(applicant);
				personid = applicant==null?"":applicant.getPersonid();
			}
		}else if("carower".equals(flag)){
			INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
			carowneinfo.setTaskid(taskid);
			carowneinfo = insbCarowneinfoDao.selectOne(carowneinfo);
			personid = carowneinfo==null?"":carowneinfo.getPersonid();
		}else if("shouyiren".equals(flag)){
			INSBLegalrightclaimhis his = null;
			INSBLegalrightclaimhis aimhis = new INSBLegalrightclaimhis();
			aimhis.setTaskid(taskid);
			aimhis.setInscomcode(inscomcode);
			List<INSBLegalrightclaimhis> aimlist = insbLegalrightclaimhisDao.selectList(aimhis);
			if(aimlist!=null&&aimlist.size()>0){
				his = aimlist.get(0);
			}
			if(his!=null){
				personid = his.getPersonid();
			}else{
				INSBLegalrightclaim aim = new INSBLegalrightclaim();
				aim.setTaskid(taskid);
				aim = insbLegalrightclaimDao.selectOne(aim);
				personid = aim==null?"":aim.getPersonid();
			}
		} else {
			INSBRelationpersonhis his = null;
			INSBRelationpersonhis query = new INSBRelationpersonhis();
			query.setTaskid(taskid);
			query.setInscomcode(inscomcode);
			List<INSBRelationpersonhis> queryList = insbRelationpersonhisDao.selectList(query);

			if (queryList != null && !queryList.isEmpty()) {
				his = queryList.get(0);
			}
			if (his != null) {
				personid = his.getPersonid();
			} else {
				INSBRelationperson person = new INSBRelationperson();
				person.setTaskid(taskid);
				person = insbRelationpersonDao.selectOne(person);
				personid = person == null ? "" : person.getPersonid();
			}
		}
		return insbPersonDao.selectById(personid);
	}

	@Override
	public List<INSBPerson> queryDriverPersonByTaskid(String taskid) {
		INSBSpecifydriver specifydriver = new INSBSpecifydriver();
		specifydriver.setTaskid(taskid);
		List<INSBSpecifydriver> driversList = insbSpecifydriverDao.selectList(specifydriver);
		List<INSBPerson> driverPersons = new ArrayList<INSBPerson>();
		if(null != driversList && driversList.size() > 0){
			for(INSBSpecifydriver driver : driversList){
				INSBPerson person = insbPersonDao.selectById(driver.getPersonid());
				driverPersons.add(person);
			}
		}
		return driverPersons;
	}

	@Override
	public List<PolicyDetailedModel> queryImgInfo(String id) {
		
		return insbPolicyManageDao.queryImgInfo(id);
	}
}
