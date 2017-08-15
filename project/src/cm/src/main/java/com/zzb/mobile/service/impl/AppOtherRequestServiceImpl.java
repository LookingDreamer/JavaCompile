package com.zzb.mobile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ModelUtil;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBQuoteVerifyService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.AppOtherRequestModel;
import com.zzb.mobile.model.CommerInsuranceBean;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.IsuredQuotationInfo;
import com.zzb.mobile.model.QuotaionAllInfo;
import com.zzb.mobile.model.StrongInsuranceBean;
import com.zzb.mobile.model.WorkFlowRuleInfo;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppOtherRequestService;

@Service
@Transactional
public class AppOtherRequestServiceImpl implements AppOtherRequestService {

	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private INSBFlowerrorDao insbFlowerrorDao;
	@Resource
	private INSBPolicyitemDao isnbPolicyitemDao;
	@Resource
	private INSBQuoteVerifyService insbQuoteVerifyService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	
	@Override
	public CommonModel queryInsuredQuoteInfo(IsuredQuotationInfo model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			List<String> inscomcodes = model.getInscomlist();
			if (null == inscomcodes || inscomcodes.size() <= 0) {
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商列表不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if (null == quotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			INSBAgent insbAgent = insbAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
			if (null == insbAgent) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人信息不存在");
				return commonModel;
			}
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if (null == carinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}

			AppOtherRequestModel requestModel = new AppOtherRequestModel();
			requestModel.setAgentname(insbAgent.getName());
			requestModel.setAgentphone(insbAgent.getMobile());
			requestModel.setCarlisceno(carinfo.getCarlicenseno());
			requestModel.setCarowername(carinfo.getOwnername());
			requestModel.setQuotedate(ModelUtil.conbertToString(quotetotalinfo.getCreatetime()));
			List<QuotaionAllInfo> quotaionAllInfos = new ArrayList<QuotaionAllInfo>();
			boolean f = true;
			for (String pid : inscomcodes) {
				QuotaionAllInfo quotaionAllInfo = new QuotaionAllInfo();
				// 供应商名称
				INSBProvider insbProvider = insbProviderDao.selectById(pid);
				if (null != insbProvider) {
					quotaionAllInfo.setProvname(insbProvider.getPrvshotname());
				}
				if (f) {
					quotaionAllInfo.setSelected(true);
					f = false;
				}

				// 保费 折扣率
				INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
				insbPolicyitem.setTaskid(taskid);
				insbPolicyitem.setInscomcode(pid);
				List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
				if (null != policyitems && policyitems.size() > 0) {
					for (INSBPolicyitem policyitem : policyitems) {
						if ("0".equals(policyitem.getRisktype())) {
							// 折扣率
							quotaionAllInfo.setSydiscountrate(policyitem.getDiscountRate() == null ? 0 : policyitem.getDiscountRate());
							// 保费
							quotaionAllInfo.setSydiscountcharge(policyitem.getDiscountCharge() == null ? 0 : policyitem.getDiscountCharge());
							quotaionAllInfo.setSyDate(DateUtil.toString(policyitem.getStartdate()));
						} else {
							// 折扣率
							quotaionAllInfo.setJqdiscountrate(policyitem.getDiscountRate() == null ? 0 : policyitem.getDiscountRate());
							// 保费
							quotaionAllInfo.setJqdiscountcharge(policyitem.getDiscountCharge() == null ? 0 : policyitem.getDiscountCharge());
							quotaionAllInfo.setJqDate(DateUtil.toString(policyitem.getStartdate()));
						}
						// 总保费
						quotaionAllInfo.setTotalepremium(policyitem.getTotalepremium() == null ? 0 : policyitem.getTotalepremium());
					}
				}
				// 保险配置信息
				// 商业险
				List<CommerInsuranceBean> insuranceBeans = null;
				// 交强险
				List<StrongInsuranceBean> strongInsuranceBeans = null;

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("taskid", taskid);
				map.put("inscomcode", pid);
				List<String> sylist = new ArrayList<String>();
				sylist.add("0");
				map.put("list", sylist);
				List<INSBCarkindprice> syCarkindprices = appInsuredQuoteDao.selectIwantCarkindpriceData(map);
				if (null != syCarkindprices && syCarkindprices.size() > 0) {
					insuranceBeans = new ArrayList<CommerInsuranceBean>();
					for (INSBCarkindprice carkindprice : syCarkindprices) {
						CommerInsuranceBean commerInsuranceBean = new CommerInsuranceBean();
						commerInsuranceBean.setAmount(carkindprice.getAmount());
						commerInsuranceBean.setCountcharge(carkindprice.getDiscountCharge() == null ? 0 : carkindprice.getDiscountCharge());
						commerInsuranceBean.setRiskname(carkindprice.getRiskname());

                        if ("GlassIns".equals(carkindprice.getInskindcode())) {
                            if (carkindprice.getAmount() == 2.0) {
                                commerInsuranceBean.setAmount("进口玻璃");
                            } else {
                                commerInsuranceBean.setAmount("国产玻璃");
                            }
                        }

						// 有不计免赔
						if ("1".equals(carkindprice.getNotdeductible())) {
							StrongInsuranceBean strongInsuranceBean = new StrongInsuranceBean();
							Map<String, Object> ncfmap = new HashMap<String, Object>();
							ncfmap.put("taskid", taskid);
							ncfmap.put("inscomcode", pid);
							//ncfmap.put("inskindtype", "1");
							ncfmap.put("inskindcode", "Ncf" + carkindprice.getInskindcode());
							List<INSBCarkindprice> ncfCarkindprices = appInsuredQuoteDao.selectIwantCarkindpriceData(ncfmap);
							if (null != ncfCarkindprices && ncfCarkindprices.size() > 0) {
								INSBCarkindprice ncfkindprice = ncfCarkindprices.get(0);
								strongInsuranceBean.setAmount("投保");
								strongInsuranceBean.setCountcharge(ncfkindprice.getDiscountCharge() == null ? 0 : ncfkindprice.getDiscountCharge());
								strongInsuranceBean.setRiskname(ncfkindprice.getRiskname());
							}
							commerInsuranceBean.setStrongInsuranceBean(strongInsuranceBean);
						}
						insuranceBeans.add(commerInsuranceBean);
					}

					//特殊处理的险别
					Map<String, String> datamap = new HashMap<String, String>();
					datamap.put("taskid", taskid);
					datamap.put("inscomcode", pid);
					List<INSBCarkindprice> specialrisks = appInsuredQuoteDao.selectSpecialRiskkind(datamap);
					for(INSBCarkindprice carkindprice : specialrisks){
						CommerInsuranceBean commerInsuranceBean = new CommerInsuranceBean();
						commerInsuranceBean.setAmount("投保");
						commerInsuranceBean.setCountcharge(carkindprice.getDiscountCharge() == null ? 0 : carkindprice.getDiscountCharge());
						commerInsuranceBean.setRiskname(carkindprice.getRiskname());
						insuranceBeans.add(commerInsuranceBean);
					}
				}
				quotaionAllInfo.setInsuranceBeans(insuranceBeans);
				
				//交强险
				Map<String, Object> jqmap = new HashMap<String, Object>();
				jqmap.put("taskid", taskid);
				jqmap.put("inscomcode", pid);
				List<String> jqlist = new ArrayList<String>();
				jqlist.add("2");
				jqlist.add("3");
				jqmap.put("list", jqlist);
				List<INSBCarkindprice> jqCarkindprices = appInsuredQuoteDao.selectIwantCarkindpriceData(jqmap);
				if(null != jqCarkindprices && jqCarkindprices.size() > 0){
					strongInsuranceBeans = new ArrayList<StrongInsuranceBean>();
					for(INSBCarkindprice carkindprice : jqCarkindprices){
						StrongInsuranceBean strongInsuranceBean = new StrongInsuranceBean();
						strongInsuranceBean.setAmount(carkindprice.getSelecteditem());
						strongInsuranceBean.setCountcharge(carkindprice.getDiscountCharge()==null?0:carkindprice.getDiscountCharge());
						strongInsuranceBean.setRiskname(carkindprice.getRiskname());
						strongInsuranceBeans.add(strongInsuranceBean);
					}
				}
				quotaionAllInfo.setStrongInsuranceBeans(strongInsuranceBeans);
				
				quotaionAllInfos.add(quotaionAllInfo);
			}
			requestModel.setQuotaionAllInfos(quotaionAllInfos);
			commonModel.setBody(requestModel);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
//	特种车
	public int carsize(String taskid){
		List<INSBCarinfo> carinfolist = insbCarinfoDao.queryBytaskid(taskid);
		return carinfolist.size();
	}
//	商业险
	public int polist(String taskid){
		INSBPolicyitem po = new INSBPolicyitem();
		po.setTaskid(taskid);
		po.setRisktype("0");//商业险
		List<INSBPolicyitem> polsit = isnbPolicyitemDao.selectList(po);
		return polsit.size();
	}
	@Override
	public CommonModel saveWorkFlowRuleInfo(WorkFlowRuleInfo model) {
		CommonModel commonModel = new CommonModel();
		String result = null;
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			String inscomcode = model.getInscomcode();
			if (StringUtil.isEmpty(inscomcode)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if(null == insbQuotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			LogUtil.info("saveWorkFlowRuleInfo"+taskid+"当前供应商="+inscomcode);
			result = appQuotationService.getQuotationValidatedInfo("",taskid,inscomcode);
			LogUtil.info("saveWorkFlowRuleInfo"+taskid+"当前供应商="+inscomcode+"=返回值res="+result);
			//result = "{\"success\":true,\"quotationMode\":0,\"resultMsg\":[\"商业险、交强起保日期不一致需分开提交\"]}";
			if(!StringUtil.isEmpty(result)){
				JSONObject jsonObject = JSONObject.fromObject(result);
				//boolean rSeparate = false;

				//execRuleQuote不受true，false影响
				if(jsonObject.containsKey("execRuleQuote") && jsonObject.getBoolean("execRuleQuote")){
					String execRQErrmsg = "优先执行规则报价";
					if(jsonObject.containsKey("execRuleQuoteCause") && !StringUtil.isEmpty(jsonObject.get("execRuleQuoteCause"))){
						execRQErrmsg = jsonObject.getString("execRuleQuoteCause").replaceAll("@#@", "; ");
					}
					saveFlowerrorToManWork(taskid, inscomcode, execRQErrmsg, "规则校验", "4");
					LogUtil.info("saveFlowerrorToManWork, taskid="+taskid+"当前供应商="+inscomcode+"返回值res="+execRQErrmsg);
				}
				if(jsonObject.containsKey("quotationMode")){
					String resultmessage = jsonObject.get("resultMsg")+"";
					if(!StringUtil.isEmpty(resultmessage)){
						saveFlowerrorToManWork(taskid, inscomcode, resultmessage, "规则校验", jsonObject.get("quotationMode")+"");
						//resultmessage = "规则校验成功";
						LogUtil.info("saveWorkFlowRuleInfo"+taskid+"当前供应商="+inscomcode+"返回值res="+resultmessage);

						//bug-6875，标记报价失败后获取报价途径之前过的承保规则是否：交强和商业险不是同一天起保，交强险和商业险需分开单独提交，不允许混保提交
						/*if (resultmessage.contains("交强险和商业险需分开单独提交")) {
							rSeparate = true;
						}*/
					}
				}

				if(jsonObject.getBoolean("success")){//校验成功了
					commonModel.setBody(true);
				}else{//校验失败了
					//提示性规则:0,转人工规则:1,限制性规则:2,阻断性规则:3,可以执行规则报价:4
					String flag = jsonObject.getString("quotationMode");
					if("2".equals(flag)||"3".equals(flag)){
						commonModel.setBody(false);

						/*if (StringUtil.isNotEmpty(appQuotationService.getThreadLocalVal()) && rSeparate) {
							appQuotationService.setThreadLocalVal(appQuotationService.getThreadLocalVal()+"_"+flag);
						}*/
					}else{
						commonModel.setBody(true);
					}
				}
			}else{
				commonModel.setBody(false);
			}
			//非费改判断是否非该区域
			Map<String, Object> map = appInsuredQuoteService.getChangeFee(insbQuotetotalinfo.getAgentnum(), null);
			boolean isnonfeereform = false;
			if(!map.isEmpty()){
				isnonfeereform = (boolean) map.get("isfeeflag");
			}
			if(isnonfeereform){
				checkNonFeereform(taskid, inscomcode);
			}
			commonModel.setStatus("success");
			commonModel.setMessage("获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	@Override
	public int selectWorkFlowRuleInfoGZ(String taskid, String inscomcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		map.put("firoredi", "4");
		return insbFlowerrorDao.selectMydataOneGZ(map) > 0 ? 0 : 1;
	}
	@Override
	public int selectWorkFlowRuleInfoRG(String taskid, String inscomcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		map.put("firoredi", "4");
		return insbFlowerrorDao.selectMydataOneRG(map) > 0 ? 0 : 1;
	}
	@Override
	public void updateClosedWorkFlowRuleInfoGZ(String taskid, String inscomcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		map.put("firoredi", "4");
		insbFlowerrorDao.updateMydataGZ(map);
	}
	@Override
	public void updateClosedWorkFlowRuleInfoRG(String taskid, String inscomcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		map.put("firoredi", "4");
		insbFlowerrorDao.updateMydataRG(map); 
	}

	@Override
	public int selectWorkFlowRuleInfoFfg(String taskid, String inscomcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		map.put("firoredi", "4");
		return insbFlowerrorDao.selectMydataOneFfg(map) > 0 ? 0 : 1;
	}
	
	/**
	 * 非费改险种校验
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	private void checkNonFeereform(String taskid,String inscomcode){
		String flowname = "非费改险种校验";
		String flowcode = "6";
		INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		LogUtil.info("非费改险种校验checkNonFeereform"+taskid+"当前供应商="+inscomcode+"车牌="+insbCarinfo.getCarlicenseno());
		//新车
		if("新车未上牌".equals(insbCarinfo.getCarlicenseno())){
			boolean res = insbQuoteVerifyService.verifyNewVehicle(taskid, inscomcode);
			LogUtil.info("非费改险种校验checkNonFeereform"+taskid+"当前供应商="+inscomcode+"车牌="+insbCarinfo.getCarlicenseno()+"返回值="+res);
			if(!res){
				saveFlowerrorToManWork(taskid, inscomcode, "非费改规则校验不通过，转人工",flowname,flowcode);
			}
		}else{
			//混保
			if(isMixedInsurance(taskid, inscomcode)){
				int res = insbQuoteVerifyService.verifyCommercial(taskid, inscomcode);
				LogUtil.info("非费改险种校验checkNonFeereform"+taskid+"当前供应商="+inscomcode+"车牌="+insbCarinfo.getCarlicenseno()+"混保返回值="+res);
				if(res == 3){
					saveFlowerrorToManWork(taskid, inscomcode, "需要剔除所有商业险",flowname,flowcode);
				}else if(res == 4){
					saveFlowerrorToManWork(taskid, inscomcode, "需要剔除交强险以及车船税",flowname,flowcode);
				}else if(res == 5){
					saveFlowerrorToManWork(taskid, inscomcode, "校验通过,但指定（自定义车价）小于最低价,需要将车损保额设置为最低价",flowname,flowcode);
				}
			}else{
				//单交强
				boolean res = insbQuoteVerifyService.verifyTraffic(taskid, inscomcode);
				LogUtil.info("非费改险种校验checkNonFeereform"+taskid+"当前供应商="+inscomcode+"车牌="+insbCarinfo.getCarlicenseno()+"单交强返回值="+res);
				if(!res){
					saveFlowerrorToManWork(taskid, inscomcode, "非费改规则校验不通过，转人工",flowname,flowcode);
				}
			}
		}
	}
	/**
	 * 判断是否混保，true 混保，false 单交强
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	private boolean isMixedInsurance(String taskid,String inscomcode){
		boolean result = false;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			if(insbPolicyitems.size() == 2){
				result =  true;
			}else{
				if("1".equals(insbPolicyitems.get(0).getRisktype())){
					result =  false;
				}else{
					result =  true;
				}
			}
		}
		return result;
	}
	
	private void saveFlowerrorToManWork(String taskid,String inscomcode,String errdesc,String flowname,String flowcode){
		LogUtil.info("saveFlowerrorToManWork"+taskid+"供应商="+inscomcode+"描述="+errdesc);
		INSBFlowerror insbFlowerror = new INSBFlowerror();
		insbFlowerror.setTaskid(taskid);
		insbFlowerror.setInscomcode(inscomcode);
		insbFlowerror.setFiroredi("4");
		insbFlowerror.setFlowcode(flowcode);
		INSBFlowerror flowerror = insbFlowerrorDao.selectOne(insbFlowerror);
		if(null == flowerror){
			flowerror = new INSBFlowerror();
			flowerror.setOperator("workflow");
			flowerror.setCreatetime(new Date());
			flowerror.setTaskid(taskid);
			flowerror.setInscomcode(inscomcode);
			flowerror.setFlowcode(flowcode);//规则级别
			flowerror.setFlowname(flowname);
			flowerror.setFiroredi("4");
			flowerror.setTaskstatus("guize");
			flowerror.setErrordesc(errdesc);
			insbFlowerrorDao.insert(flowerror);
		}else{
			flowerror.setOperator("workflow");
			flowerror.setModifytime(new Date());
			flowerror.setTaskid(taskid);
			flowerror.setInscomcode(inscomcode);
			flowerror.setFlowcode(flowcode);//规则级别
			flowerror.setFlowname(flowname);
			flowerror.setFiroredi("4");
			flowerror.setTaskstatus("guize");
			flowerror.setErrordesc(errdesc);
			insbFlowerrorDao.updateById(flowerror);
		}
	}
}