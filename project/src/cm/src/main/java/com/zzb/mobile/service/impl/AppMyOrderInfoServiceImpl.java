package com.zzb.mobile.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.common.*;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBPersonHelpService;
import com.zzb.conf.service.INSBOrderlistenpushService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.policyoperat.EditPolicyInfoParam;
import com.zzb.mobile.service.AppMyOrderInfoService;

@Service
@Transactional
public class AppMyOrderInfoServiceImpl extends BaseServiceImpl<INSBOrder>
		implements AppMyOrderInfoService {

	private static String WORKFLOWURL = "";

	static {
		// 读取相关的配置 
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		WORKFLOWURL = resourceBundle.getString("workflow.url");
	}
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBRiskkindDao insbRiskkindDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBUsercommentDao insbUsercommentDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private LzgOrderService lzgOrderService;
	@Resource
	private INSBOrderlistenpushService insbOrderlistenpushService;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;	
	@Resource
	private INSBPersonHelpService insbPersonHelpService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	
	
	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return insbOrderDao;
	}

	/**
	 * 通过证件类型和身份证号返回性别信息 0男1女默认男
	 * */
	public int getGenderByIdtypeAndIdNo(Integer Idtype, String IdNo){
		List<String> even = Arrays.asList(new String[]{"0","2","4","6","8"});
		if(Idtype!=null){
			if(Idtype==0){//身份证
				if(IdNo!=null){
					if(IdNo.length()==15){//第15位代表性别，奇数为男，偶数为女
						if(even.contains(IdNo.substring(14))){
							return 1;
						}
					}else if(IdNo.length()==18){//第17位代表性别，奇数为男，偶数为女
						if(even.contains(IdNo.substring(16,17))){
							return 1;
						}
					}
				}
			}
		}
		return 0;//默认男
	}
	/**
	 * 提交投保前获得简要的基本信息显示接口 （此页面有添加影像和备注待处理）
	 * */
	@Override
	public String getBriefTaskInfoBeforePolicy(String processInstanceId, String inscomcode) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			// 信息查询
			// 得到被保人信息
			INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
			// 得到投保人信息
			INSBPerson applicantPerson = getApplicantInfo(processInstanceId, inscomcode);
			// 得到权益索赔人信息
			INSBPerson legalrightclaimPerson = getLegalrightclaimInfo(processInstanceId, inscomcode);
			// 得到权益索赔人信息
			INSBPerson carOwnerPerson = getCarOwnerInfo(processInstanceId, inscomcode);
			// 响应数据结构组织
//			Map<String, Object> insured = new HashMap<String, Object>();// 被保人信息
//			if(insuredPerson!=null){
//				insured.put("id", insuredPerson.getId());// 被保人id
//				insured.put("name", insuredPerson.getName());// 被保人姓名
//				insured.put("idcardno", insuredPerson.getIdcardno());// 被保人身份证号
//				insured.put("gender", insuredPerson.getGender());// 被保人性别,没有映射字典
//				insured.put("idcardtype", insuredPerson.getIdcardtype());// 被保人证件类型,没有映射字典
//				insured.put("cellphone", insuredPerson.getCellphone());// 被保人手机号
//				insured.put("email", insuredPerson.getEmail());// 被保人邮箱
//			}
			Map<String, Object> insured = new HashMap<String, Object>();// 被保人信息
			if(insuredPerson!=null){
				insured.put("isSameWithInsured",carOwnerPerson.getId().equals(insuredPerson.getId()));// 是否和车主一致
				if (!carOwnerPerson.getId().equals(insuredPerson.getId())) {
					insured.put("id", insuredPerson.getId());// 被保人id
					insured.put("name", insuredPerson.getName());// 被保人姓名
					insured.put("idcardno", insuredPerson.getIdcardno());// 被保人身份证号
					insured.put("gender", insuredPerson.getGender());// 被保人性别,没有映射字典
					insured.put("idcardtype", insuredPerson.getIdcardtype());// 被保人证件类型,没有映射字典
					insured.put("cellphone", insuredPerson.getCellphone());// 被保人手机号
					insured.put("email", insuredPerson.getEmail());// 被保人邮箱
				}
			}else{
				insured.put("isSameWithInsured",false);// 是否和被保人一致
				insured.put("id", "");// 权益索赔人id
				insured.put("name", "");// 权益索赔人姓名
				insured.put("idcardno", "");// 权益索赔人身份证号
				insured.put("gender", "");// 权益索赔人性别,没有映射字典
				insured.put("idcardtype", "");// 权益索赔人证件类型,没有映射字典
				insured.put("cellphone", "");// 权益索赔人手机号
				insured.put("email", "");// 权益索赔人邮箱
			}
			
			
			Map<String, Object> applicant = new HashMap<String, Object>();// 投保人信息
			if(applicantPerson!=null){
				applicant.put("isSameWithInsured",
						carOwnerPerson.getId().equals(applicantPerson.getId()));// 是否和被保人一致
				if (!carOwnerPerson.getId().equals(applicantPerson.getId())) {
					applicant.put("id", applicantPerson.getId());// 投保人id
					applicant.put("name", applicantPerson.getName());// 投保人姓名
					applicant.put("idcardno", applicantPerson.getIdcardno());// 投保人身份证号
					applicant.put("gender", applicantPerson.getGender());// 投保人性别,没有映射字典
					applicant.put("idcardtype", applicantPerson.getIdcardtype());// 投保人证件类型,没有映射字典
					applicant.put("cellphone", applicantPerson.getCellphone());// 投保人手机号
					applicant.put("email", applicantPerson.getEmail());// 投保人邮箱
				}
			}else{
				applicant.put("isSameWithInsured",false);// 是否和被保人一致
				applicant.put("id", "");// 投保人id
				applicant.put("name", "");// 投保人姓名
				applicant.put("idcardno", "");// 投保人身份证号
				applicant.put("gender", "");// 投保人性别,没有映射字典
				applicant.put("idcardtype", "");// 投保人证件类型,没有映射字典
				applicant.put("cellphone", "");// 投保人手机号
				applicant.put("email", "");// 投保人邮箱
			}
			Map<String, Object> legalrightclaim = new HashMap<String, Object>();// 权益索赔人信息
			if(legalrightclaimPerson!=null){
				legalrightclaim.put("isSameWithInsured", 
						carOwnerPerson.getId().equals(legalrightclaimPerson.getId()));// 是否和被保人一致
				if (!carOwnerPerson.getId().equals(legalrightclaimPerson.getId())) {
					legalrightclaim.put("id", legalrightclaimPerson.getId());// 权益索赔人id
					legalrightclaim.put("name", legalrightclaimPerson.getName());// 权益索赔人姓名
					legalrightclaim.put("idcardno", legalrightclaimPerson.getIdcardno());// 权益索赔人身份证号
					legalrightclaim.put("gender", legalrightclaimPerson.getGender());// 权益索赔人性别,没有映射字典
					legalrightclaim.put("idcardtype", legalrightclaimPerson.getIdcardtype());// 权益索赔人证件类型,没有映射字典
					legalrightclaim.put("cellphone", legalrightclaimPerson.getCellphone());// 权益索赔人手机号
					legalrightclaim.put("email", legalrightclaimPerson.getEmail());// 权益索赔人邮箱
				}
			}else{
				legalrightclaim.put("isSameWithInsured",false);// 是否和被保人一致
				legalrightclaim.put("id", "");// 权益索赔人id
				legalrightclaim.put("name", "");// 权益索赔人姓名
				legalrightclaim.put("idcardno", "");// 权益索赔人身份证号
				legalrightclaim.put("gender", "");// 权益索赔人性别,没有映射字典
				legalrightclaim.put("idcardtype", "");// 权益索赔人证件类型,没有映射字典
				legalrightclaim.put("cellphone", "");// 权益索赔人手机号
				legalrightclaim.put("email", "");// 权益索赔人邮箱
			}
			Map<String, Object> carOwnerInfo = new HashMap<String, Object>();// 权益索赔人信息
			if(carOwnerPerson!=null){
				//carOwnerInfo.put("isSameWithInsured",insuredPerson.getId().equals(carOwnerPerson.getId()));// 是否和被保人一致
				//if (!insuredPerson.getId().equals(carOwnerPerson.getId())) {
				carOwnerInfo.put("id", carOwnerPerson.getId());// 权益索赔人id
				carOwnerInfo.put("name", carOwnerPerson.getName());// 权益索赔人姓名
				carOwnerInfo.put("idcardno", carOwnerPerson.getIdcardno());// 权益索赔人身份证号
				carOwnerInfo.put("gender", carOwnerPerson.getGender());// 权益索赔人性别,没有映射字典
				carOwnerInfo.put("idcardtype", carOwnerPerson.getIdcardtype());// 权益索赔人证件类型,没有映射字典
				carOwnerInfo.put("cellphone", carOwnerPerson.getCellphone());// 权益索赔人手机号
				carOwnerInfo.put("email", carOwnerPerson.getEmail());// 权益索赔人邮箱
				//}
			}else{
				//carOwnerInfo.put("isSameWithInsured",false);// 是否和被保人一致
				carOwnerInfo.put("id", "");// 权益索赔人id
				carOwnerInfo.put("name", "");// 权益索赔人姓名
				carOwnerInfo.put("idcardno", "");// 权益索赔人身份证号
				carOwnerInfo.put("gender", "");// 权益索赔人性别,没有映射字典
				carOwnerInfo.put("idcardtype", "");// 权益索赔人证件类型,没有映射字典
				carOwnerInfo.put("cellphone", "");// 权益索赔人手机号
				carOwnerInfo.put("email", "");// 权益索赔人邮箱
			}
			//查询车辆信息
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(inscomcode);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			String carinfoid = carinfo.getId();
			if(carinfohis != null){//统一转换为INSBCarinfo类型，方便后面组织数据
				carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
			}
			//查询车型信息
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			INSBCarmodelinfo carmodelinfo = null;
			if(carmodelinfohis == null){
				carmodelinfo = new INSBCarmodelinfo();
				carmodelinfo.setCarinfoid(carinfoid);
				carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
			}else{
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
			Map<String, Object> carinfoMap = new HashMap<String, Object>();//车辆信息
			if(carinfo!=null){
				carinfoMap.put("property", carinfo.getProperty());//车辆所属性质
				carinfoMap.put("carproperty", carinfo.getCarproperty());//车辆使用性质
				carinfoMap.put("istransfercar", carinfo.getIsTransfercar());//是否过户车
				if("1".equals(carinfo.getIsTransfercar()) && carinfo.getTransferdate()!=null){
					carinfoMap.put("chgOwnerDate", sdf.format(carinfo.getTransferdate()));//过户时间
				}
				if(carinfo.getRegistdate()!=null){
					carinfoMap.put("registdate", sdf.format(carinfo.getRegistdate()));//车辆初登日期
				}
				carinfoMap.put("engineno", ModelUtil.hiddenEngineNo(carinfo.getEngineno()));//发动机号
				carinfoMap.put("vincode", ModelUtil.hiddenVin(carinfo.getVincode()));//车辆识别代码
			}
			if(carmodelinfo!=null){
				carinfoMap.put("standardfullname", carmodelinfo.getStandardfullname());//车型全称
				carinfoMap.put("standardname", carmodelinfo.getStandardname());//车型标准名称
			}
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setTaskid(processInstanceId);
			policyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
			Map<String, Object> policyinfoMap = new HashMap<String, Object>();//投保信息
			if(policyitemList!=null && policyitemList.size()>0){
				for (int i = 0; i < policyitemList.size(); i++) {
					INSBPolicyitem plit = policyitemList.get(i);
					if(plit!=null){
						if("0".equals(plit.getRisktype())){//商业险起保日期
							if(plit.getStartdate()!=null){
								policyinfoMap.put("busstarttime", sdf.format(plit.getStartdate()));//商业险起始日期
							}
							if(plit.getEnddate()!=null){
								policyinfoMap.put("busendtime", sdf.format(plit.getEnddate()));//商业险结束日期
							}
							policyinfoMap.put("syproposalformno", plit.getProposalformno());//商业险投保单号
						}else if("1".equals(plit.getRisktype())){//交强险起保日期
							if(plit.getStartdate()!=null){
								policyinfoMap.put("strstarttime", sdf.format(plit.getStartdate()));//交强险起始日期
							}
							if(plit.getEnddate()!=null){
								policyinfoMap.put("strendtime", sdf.format(plit.getEnddate()));//交强险结束日期
							}
							policyinfoMap.put("jqproposalformno", plit.getProposalformno());//较强险投保单号
						}
					}
				}
			}
			//用户备注
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(processInstanceId);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			INSBWorkflowsub workflowsub = new INSBWorkflowsub();
			workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
			workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
			INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
			subtrack.setInstanceid(workflowsub.getInstanceid());
			subtrack.setTaskcode(workflowsub.getTaskcode());
			subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//子流程轨迹信息
			INSBUsercomment userComment = new INSBUsercomment();
			if (subtrack == null || StringUtil.isEmpty(subtrack.getId())) {
				userComment = null;
			} else {
				userComment.setTrackid(subtrack.getId());//任务轨迹id
				userComment.setTracktype(2);//1：主流程 2：子流程
				userComment = insbUsercommentDao.selectOne(userComment);
			}

			if(userComment!=null){
				body.put("remark", userComment.getCommentcontent());
			}else{
				body.put("remark", "");
			}
			//出单网点
			if(quoteinfo!=null && !StringUtils.isEmpty(quoteinfo.getDeptcode())){
				INSCDept dept = new INSCDept();
				dept.setComcode(quoteinfo.getDeptcode());
				dept = inscDeptDao.selectOne(dept);
				if(dept!=null){
					body.put("comname", dept.getComname());
					body.put("comcode", quoteinfo.getDeptcode());
				}else{
					body.put("comname", "");
					body.put("comcode", quoteinfo.getDeptcode());
				}
			}else{
				body.put("comname", "");
				body.put("comcode", quoteinfo.getDeptcode());
			}
			body.put("insured", insured);
			body.put("applicant", applicant);
			body.put("legalrightclaim", legalrightclaim);
			body.put("carownerinfo", carOwnerInfo);
			body.put("carinfo", carinfoMap);
			body.put("policyinfo", policyinfoMap);
			body.put("inscomcode", inscomcode);// 保险公司code
			body.put("processInstanceId", processInstanceId);// 流程实例id
			//备注类型
			body.put("agentnoti", remarkTypeFromCode());
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询投保信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询投保信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	private Map<String,Object> remarkTypeFromCode(){
		Map<String,Object> map = new HashMap<String, Object>();
		INSCCode code = new INSCCode();
		code.setParentcode("agentnoti");
		code.setCodetype("agentnoti1");
		List<INSCCode> listcode = inscCodeDao.selectList(code);
		if(listcode!=null&&listcode.size()>0){
			for (INSCCode inscCode : listcode) {
//				map.put(inscCode.getCodename(), inscCode.getProp1()==0?Boolean.FALSE:Boolean.TRUE);
				map.put(inscCode.getCodename(), inscCode.getCodevalue());
			}
		}
		return map;
	}
	
	/**
	 * 提交修改投保信息接口
	 * */
	@Override
	public String editPolicyInfoBeforePolicy(EditPolicyInfoParam editPolicyInfoParam) {
		Date date = new Date() ;
		String carproperty = editPolicyInfoParam.getCarproperty();
		String isTransfercar = editPolicyInfoParam.getIsTransfercar();
		String property = editPolicyInfoParam.getProperty();
		String registdate = editPolicyInfoParam.getRegistdate();
		String chgOwnerDate = editPolicyInfoParam.getChgOwnerDate();
		String processInstanceId = editPolicyInfoParam.getProcessInstanceId();
		String inscomcode = editPolicyInfoParam.getInscomcode();
		String operator = editPolicyInfoParam.getOperator();
//		String telephone = editPolicyInfoParam.getTelephone();//被保人电话
//		String email = editPolicyInfoParam.getEmail();//被保人邮箱
		String remark = editPolicyInfoParam.getRemark();//备注信息
		String remarkcode = editPolicyInfoParam.getRemarkcode();
		INSBInvoiceinfo invoice=new INSBInvoiceinfo();
		INSBInvoiceinfo invoiceinfo=editPolicyInfoParam.getInvoiceinfo();
		//插入发票信息
		if(invoiceinfo!=null){
			Map<String,String>paramMap = new HashMap<String,String>();
			paramMap.put("taskid",processInstanceId);
			//paramMap.put("inscomcode",inscomcode);
			paramMap.put("inscomcode",null);
			INSBInvoiceinfo info = insbInvoiceinfoDao.selectByTaskidAndComcode(paramMap);
			if(info==null){
				//增值税专用发票 或 增值税普通发票(需资料)
				if( invoiceinfo.getInvoicetype()==1||invoiceinfo.getInvoicetype()==2){
					invoice.setAccountnumber(invoiceinfo.getAccountnumber());
					invoice.setBankname(invoiceinfo.getBankname());
					invoice.setCreatetime(date);
					invoice.setNoti(invoiceinfo.getNoti());
					invoice.setEmail(invoiceinfo.getEmail());
					invoice.setIdentifynumber(invoiceinfo.getIdentifynumber());
					invoice.setInscomcode(inscomcode);
					invoice.setInvoicetype(invoiceinfo.getInvoicetype());
					invoice.setOperator(operator);
					invoice.setRegisteraddress(invoiceinfo.getRegisteraddress());
					invoice.setTaskid(processInstanceId);
					invoice.setRegisterphone(invoiceinfo.getRegisterphone());
					insbInvoiceinfoDao.insert(invoice);
					//增值税普通发票
				}else if( invoiceinfo.getInvoicetype()==0){
					invoice.setAccountnumber(null);
					invoice.setBankname(null);
					invoice.setNoti(invoiceinfo.getNoti());
					invoice.setCreatetime(date);
					invoice.setEmail(null);
					invoice.setIdentifynumber(null);
					invoice.setInscomcode(inscomcode);
					invoice.setInvoicetype(invoiceinfo.getInvoicetype());
					invoice.setOperator(operator);
					invoice.setRegisteraddress(null);
					invoice.setTaskid(processInstanceId);
					invoice.setRegisterphone(null);
					insbInvoiceinfoDao.insert(invoice);
				}
			}else{
				if(invoiceinfo.getInvoicetype()==1){		
						invoice.setAccountnumber(invoiceinfo.getAccountnumber());
						invoice.setBankname(invoiceinfo.getBankname());
						invoice.setModifytime(date);
						invoice.setEmail(invoiceinfo.getEmail());
						invoice.setIdentifynumber(invoiceinfo.getIdentifynumber());
						invoice.setInscomcode(inscomcode);
						invoice.setInvoicetype(invoiceinfo.getInvoicetype());
						invoice.setNoti(invoiceinfo.getNoti());
						invoice.setOperator(operator);
						invoice.setRegisteraddress(invoiceinfo.getRegisteraddress());
						invoice.setTaskid(processInstanceId);
						invoice.setRegisterphone(invoiceinfo.getRegisterphone());
						insbInvoiceinfoDao.updateByTaskid(invoice);
				}else if(invoiceinfo.getInvoicetype()==0){
						invoice.setAccountnumber(null);
						invoice.setBankname(null);
						invoice.setModifytime(date);
						invoice.setNoti(invoiceinfo.getNoti());
						invoice.setEmail(null);
						invoice.setIdentifynumber(null);
						invoice.setInscomcode(inscomcode);
						invoice.setInvoicetype(invoiceinfo.getInvoicetype());
						invoice.setOperator(operator);
						invoice.setRegisteraddress(null);
						invoice.setTaskid(processInstanceId);
						invoice.setRegisterphone(null);
						insbInvoiceinfoDao.updateByTaskid(invoice);
				}
			}
		}
		//用户备注,不为空的时候保存入库
		if(!StringUtil.isEmpty(remark)){
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(processInstanceId);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			INSBWorkflowsub workflowsub = new INSBWorkflowsub();
			workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
			workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
			INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
			subtrack.setInstanceid(workflowsub.getInstanceid());
			subtrack.setTaskcode(workflowsub.getTaskcode());
			subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//子流程轨迹信息
			INSBUsercomment userComment = new INSBUsercomment();
			if (subtrack == null || StringUtil.isNotEmpty(subtrack.getId())) {
				userComment = null;
			} else {
				userComment.setTrackid(subtrack.getId());//任务轨迹id
				userComment.setTracktype(2);//1：主流程 2：子流程
				userComment = insbUsercommentDao.selectOne(userComment);
			}

			if(userComment!=null){
				userComment.setTrackid(subtrack.getId());//任务轨迹id
				userComment.setTracktype(2);//1：主流程 2：子流程
				userComment.setCommentcontent(remark);//备注信息
				userComment.setCommentcontenttype(StringUtil.isEmpty(remarkcode)?0:Integer.parseInt(remarkcode));//备注内容类型
				userComment.setCommenttype(1);//备注类型
				userComment.setModifytime(date);
				userComment.setOperator(quotetotalinfo.getOperator());
				insbUsercommentDao.updateById(userComment);
			}else{
				userComment = new INSBUsercomment();
				userComment.setTrackid(subtrack.getId());//任务轨迹id
				userComment.setTracktype(2);//1：主流程 2：子流程
				userComment.setCommentcontent(remark);//备注信息
				userComment.setCommentcontenttype(StringUtil.isEmpty(remarkcode)?0:Integer.parseInt(remarkcode));//备注内容类型
				userComment.setCommenttype(1);//备注类型
				userComment.setCreatetime(date);
				userComment.setModifytime(date);
				userComment.setOperator(quotetotalinfo.getOperator());
				insbUsercommentDao.insert(userComment);//添加用户备注信息
			}
		}
		//北京流程,保存被保人手机和邮箱
		if(StringUtil.isNotEmpty(operator)){
			INSBAgent agent = insbAgentDao.selectByJobnum(operator);
			if(agent != null){
				INSCDept agentDept = inscDeptService.getPlatformDept(agent.getDeptid());
				if(agentDept != null && (ConstUtil.PLATFORM_BEIJING_DEPTCODE.equals(agentDept.getComcode()))){
					String telephone = editPolicyInfoParam.getTelephone();//被保人电话
					String email = editPolicyInfoParam.getEmail();//被保人邮箱
					INSBInsured insured = new INSBInsured();
					insured.setTaskid(processInstanceId);
					insured = insbInsuredDao.selectOne(insured);
					//修改被保人电话邮箱信息
					//修改单方表
					INSBInsuredhis insuredhis = new INSBInsuredhis();
					insuredhis.setTaskid(processInstanceId);
					insuredhis.setInscomcode(inscomcode);
					insuredhis = insbInsuredhisDao.selectOne(insuredhis);
					
					if(insuredhis == null){
						// 查询被保人原表信息
						INSBPerson insuredPerson = new INSBPerson();
						insuredPerson = insbPersonDao.selectById(insured.getPersonid());//查询数据库的原始数据
						insuredPerson.setId(null);
						insuredPerson.setCellphone(telephone);
						insuredPerson.setEmail(email);
						insuredPerson.setCreatetime(date);
						insuredPerson.setOperator(operator);
						insbPersonDao.insert(insuredPerson);
						insuredhis = EntityTransformUtil.insured2Insuredhis(insured, inscomcode);
						insuredhis.setCreatetime(date);
						insuredhis.setModifytime(null);
						insuredhis.setOperator(operator);
						insuredhis.setPersonid(insuredPerson.getId());
						insbInsuredhisDao.insert(insuredhis);
					}else{
						INSBPerson insuredPerson = insbPersonDao.selectById(insuredhis.getPersonid());//查询数据库的原始数据
						insuredPerson.setCellphone(telephone);
						insuredPerson.setEmail(email);
						insuredPerson.setOperator(operator);
						insuredPerson.setModifytime(date);
						insbPersonDao.updateById(insuredPerson);
					}
					LogUtil.info("保存被保人电话邮箱(北京流程)成功taskid=" + processInstanceId);
				}
			}
		}
		//修改被保人信息
//		INSBInsured insured = new INSBInsured();
//		insured.setTaskid(processInstanceId);
//		insured = insbInsuredDao.selectOne(insured);
//		INSBInsuredhis insuredhis = new INSBInsuredhis();
//		insuredhis.setTaskid(processInstanceId);
//		insuredhis.setInscomcode(inscomcode);
//		insuredhis = insbInsuredhisDao.selectOne(insuredhis);
//		if(insuredhis == null){
//			// 查询被保人原表信息
//			INSBPerson insuredPerson = new INSBPerson();
//			insuredPerson = insbPersonDao.selectById(insured.getPersonid());//查询数据库的原始数据
//			insuredPerson.setId(null);
//			insuredPerson.setCellphone(telephone);
//			insuredPerson.setEmail(email);
//			insuredPerson.setCreatetime(date);
//			insuredPerson.setOperator(operator);
//			insbPersonDao.insert(insuredPerson);
//			insuredhis = EntityTransformUtil.insured2Insuredhis(insured, inscomcode);
//			insuredhis.setCreatetime(date);
//			insuredhis.setModifytime(null);
//			insuredhis.setOperator(operator);
//			insuredhis.setPersonid(insuredPerson.getId());
//			insbInsuredhisDao.insert(insuredhis);
//		}else{
//			INSBPerson insuredPerson = insbPersonDao.selectById(insuredhis.getPersonid());//查询数据库的原始数据
//			insuredPerson.setCellphone(telephone);
//			insuredPerson.setEmail(email);
//			insuredPerson.setOperator(operator);
//			insuredPerson.setModifytime(date);
//			insbPersonDao.updateById(insuredPerson);
//		}
		//修改车辆信息
		INSBCarinfohis carinfohis = new INSBCarinfohis();
		carinfohis.setTaskid(processInstanceId);
		carinfohis.setInscomcode(inscomcode);
		carinfohis = insbCarinfohisDao.selectOne(carinfohis);
		INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
		if(carinfohis == null){//统一转换为INSBCarinfo类型，方便后面组织数据
			carinfohis = EntityTransformUtil.carinfo2Carinfohis(carinfo, inscomcode);
			carinfohis.setCarproperty(carproperty);
			carinfohis.setIsTransfercar(isTransfercar);
			carinfohis.setTransferdate(String2Date(chgOwnerDate));
			carinfohis.setProperty(property);
			carinfohis.setRegistdate(String2Date(registdate));
			carinfohis.setCreatetime(date);
			carinfohis.setOperator(operator);
			insbCarinfohisDao.insert(carinfohis);
		}else{
			carinfohis.setCarproperty(carproperty);
			carinfohis.setIsTransfercar(isTransfercar);
			carinfohis.setTransferdate(String2Date(chgOwnerDate));
			carinfohis.setProperty(property);
			carinfohis.setRegistdate(String2Date(registdate));
			carinfohis.setModifytime(date);
			carinfohis.setOperator(operator);
			insbCarinfohisDao.updateById(carinfohis);
		}
		String busendtime = editPolicyInfoParam.getBusendtime();
		String busstarttime = editPolicyInfoParam.getBusstarttime();
		String strendtime = editPolicyInfoParam.getStrendtime();
		String strstarttime = editPolicyInfoParam.getStrstarttime();
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				INSBPolicyitem plit = policyitemList.get(i);
				if(plit!=null){
					if("0".equals(plit.getRisktype())){//商业险起保日期
						plit.setStartdate(String2Date(busstarttime));
						plit.setEnddate(String2Date(busendtime));
						insbPolicyitemDao.updateById(plit);
					}
//					else if(i==(policyitemList.size()-1)){//没有找到商业险保单
//						policyitem.setOperator(operator);
//						policyitem.setCreatetime(date);
//						policyitem.setStartdate(String2Date(busstarttime));
//						policyitem.setEnddate(String2Date(busendtime));
//						policyitem.setRisktype("0");
//						policyitem.setCarinfoid(carinfo.getId());
//						INSBPerson temp = null;
//						if(insured!=null){
//							policyitem.setInsuredid(insured.getId());
//							temp = insbPersonDao.selectById(insured.getPersonid());
//							if(temp!=null){
//								policyitem.setInsuredname(temp.getName());
//							}
//						}
//						INSBApplicant applicant = new INSBApplicant();
//						applicant.setTaskid(processInstanceId);
//						applicant = insbApplicantDao.selectOne(applicant);
//						if(applicant!=null){
//							policyitem.setApplicantid(applicant.getId());
//							temp = insbPersonDao.selectById(applicant.getPersonid());
//							if(temp!=null){
//								policyitem.setApplicantname(temp.getName());
//							}
//						}
//						//查询车型信息
//						INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
//						carmodelinfohis.setCarinfoid(carinfo.getId());
//						carmodelinfohis.setInscomcode(inscomcode);
//						carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
//						if(carmodelinfohis == null){
//							INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
//							carmodelinfo.setCarinfoid(carinfo.getId());
//							carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
//							if(carmodelinfo!=null){
//								policyitem.setStandardfullname(carmodelinfo.getStandardfullname());
//							}
//						}else{
//							policyitem.setStandardfullname(carmodelinfohis.getStandardfullname());
//						}
//						INSBCarowneinfo carownerinfo = new INSBCarowneinfo();
//						carownerinfo.setTaskid(processInstanceId);
//						carownerinfo = insbCarowneinfoDao.selectOne(carownerinfo);
//						if(carownerinfo!=null){
//							policyitem.setCarownerid(carownerinfo.getId());
//							temp = insbPersonDao.selectById(carownerinfo.getPersonid());
//							if(temp!=null){
//								policyitem.setCarownername(temp.getName());
//							}
//						}
//						if(quotetotalinfo!=null){
//							policyitem.setAgentnum(quotetotalinfo.getAgentnum());
//							policyitem.setAgentname(quotetotalinfo.getAgentname());
//							policyitem.setTeam(quotetotalinfo.getTeam());
//						}
//						insbPolicyitemDao.insert(policyitem);
//					}
					else if("1".equals(plit.getRisktype())){ //交强险
						plit.setStartdate(String2Date(strstarttime));
						plit.setEnddate(String2Date(strendtime));
						insbPolicyitemDao.updateById(plit);
					}
				}
			}
		}
		return "success";
	}
	/**
	 * String转Date
	 * */
	public Date String2Date(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString="";
		if(dateStr!=null){
			dateString = dateStr.trim();
		}
		if(dateString.length()>10){
			dateString = dateString.substring(0, 10);
		}
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			return date;
		}
		return date;
	}
	
	/**
	 * 提交投保前获得被保人信息接口
	 * */
	@Override
	public String getInsuredInfoBeforePolicy(String processInstanceId, String inscomcode) {
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			// 响应数据结构组织
			Map<String, Object> insured = getInsuredInfoMap(processInstanceId, inscomcode);
			body.put("insured", insured);
			body.put("processInstanceId", processInstanceId);//流程实例id
			body.put("inscomcode", inscomcode);//保险公司code
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询被保人信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询被保人信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	/**
	 * 封装被保人信息
	 * */
	public Map<String, Object> getInsuredInfoMap(String processInstanceId, String inscomcode) {
		// 数据查询
		Map<String, Object> insured = new HashMap<String, Object>();//投保人信息
		// 得到被保人信息
		INSBPerson carperson = getCarOwnerInfo(processInstanceId, inscomcode);
		// 得到投保人信息
		INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
		if(insuredPerson!=null){
			insured.put("isSameWithInsured", carperson.getId().equals(insuredPerson.getId()));//是否和被保人一致
			if(!carperson.getId().equals(insuredPerson.getId())){
				insured.put("id", insuredPerson.getId());//投保人id
				insured.put("name", insuredPerson.getName());//投保人姓名
				insured.put("gender", insuredPerson.getGender());//投保人性别
				insured.put("idcardtype", insuredPerson.getIdcardtype());//投保人证件类型
				insured.put("idcardno", insuredPerson.getIdcardno());//投保人证件号
				insured.put("cellphone", insuredPerson.getCellphone());//投保人手机号
				insured.put("email", insuredPerson.getEmail());//投保人邮箱
			}
		}else{
			insured.put("isSameWithInsured", false);//是否和被保人一致
			insured.put("id", "");//投保人id
			insured.put("name", "");//投保人姓名
			insured.put("gender", "");//投保人性别
			insured.put("idcardtype", "");//投保人证件类型
			insured.put("idcardno", "");//投保人证件号
			insured.put("cellphone", "");//投保人手机号
			insured.put("email", "");//投保人邮箱
		}
		return insured;
	}
	
	/**
	 * 查询被保人信息
	 * */
	public INSBPerson getInsuredInfo(String processInstanceId, String inscomcode) {
		// 查询被保人历史表信息
		INSBInsuredhis insuredhis = new INSBInsuredhis();
		insuredhis.setTaskid(processInstanceId);
		insuredhis.setInscomcode(inscomcode);
		insuredhis = insbInsuredhisDao.selectOne(insuredhis);
		// 得到被保人信息
		INSBPerson insuredPerson = null;
		if(insuredhis == null){
			// 查询被保人原表信息
			INSBInsured insuredMap = new INSBInsured();
			insuredMap.setTaskid(processInstanceId);
			insuredMap = insbInsuredDao.selectOne(insuredMap);
			if(insuredMap!=null){
				insuredPerson = insbPersonDao.selectById(insuredMap.getPersonid());
			}
		}else{
			insuredPerson = insbPersonDao.selectById(insuredhis.getPersonid());
		}
		return insuredPerson;
	}

	/**
	 * 提交投保前修改车主信息接口
	 * */
	@Override
	public String editInsuredInfoBeforePolicy(String processInstanceId, String inscomcode, String operator, 
			String insuredInfoJSON) {
		// 修改被保人信息并返回修改状态
		JSONObject jo = JSONObject.fromObject(insuredInfoJSON);
		INSBPerson pagePerson = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
		// 查询车主表信息  不可能为空
		INSBCarowneinfo carOwnerinfo = new INSBCarowneinfo();
		carOwnerinfo.setTaskid(processInstanceId);
		carOwnerinfo = insbCarowneinfoDao.selectOne(carOwnerinfo);
		if(null != carOwnerinfo){
			INSBPerson insbPerson = insbPersonDao.selectById(carOwnerinfo.getPersonid());
			if(null != insbPerson){
				insbPerson.setName(pagePerson.getName());
				insbPerson.setGender(this.getGenderByIdtypeAndIdNo(pagePerson.getIdcardtype(), pagePerson.getIdcardno()));
				insbPerson.setIdcardtype(pagePerson.getIdcardtype());
				insbPerson.setIdcardno(pagePerson.getIdcardno());
				insbPerson.setCellphone(pagePerson.getCellphone());
				insbPerson.setEmail(pagePerson.getEmail());
				insbPerson.setOperator(operator);
				insbPerson.setModifytime(new Date());
				insbPerson.setTaskid(processInstanceId);
				insbPersonDao.updateById(insbPerson);
			}
		}
		return "success";
	}
	
	
	
	/**
	 * 提交投保前修改被保人信息接口
	 * */
	public String editInsuredInfoBeforePolicyTemp(String processInstanceId, String inscomcode, String operator, 
			String insuredInfoJSON) {
		// 修改被保人信息并返回修改状态
		JSONObject jo = JSONObject.fromObject(insuredInfoJSON);
		INSBPerson pageInsured = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
		Date date = new Date();
		// 查询被保人历史表信息
		INSBInsuredhis insuredhis = new INSBInsuredhis();
		insuredhis.setTaskid(processInstanceId);
		insuredhis.setInscomcode(inscomcode);
		insuredhis = insbInsuredhisDao.selectOne(insuredhis);
		if(insuredhis == null){
			// 查询被保人原表信息
			INSBInsured insuredMap = new INSBInsured();
			insuredMap.setTaskid(processInstanceId);
			insuredMap = insbInsuredDao.selectOne(insuredMap);
			INSBPerson insuredPerson = new INSBPerson();
			if(insuredMap!=null){
				insuredPerson = insbPersonDao.selectById(insuredMap.getPersonid());//查询数据库的原始数据
				insuredPerson.setId(null);
				insuredPerson = setPersonInfo(pageInsured, insuredPerson, operator);
				insuredPerson.setCreatetime(date);
				insuredPerson.setModifytime(null);
				insbPersonDao.insert(insuredPerson);
				insuredhis = EntityTransformUtil.insured2Insuredhis(insuredMap, inscomcode);
				insuredhis.setCreatetime(date);
				insuredhis.setModifytime(null);
				insuredhis.setOperator(operator);
				insuredhis.setPersonid(insuredPerson.getId());
				insbInsuredhisDao.insert(insuredhis);
				//更新与被保人一致的关系人中间表personid
				String result = this.eidtRelationPersonOfSameWithInsured(processInstanceId, inscomcode, operator, 
						insuredMap.getPersonid(), insuredPerson.getId());
				if(!"success".equals(result)){
					throw new RuntimeException("更新关系人信息personid时异常！");
				}
			}else{
				insuredPerson = setPersonInfo(pageInsured, insuredPerson, operator);
				insuredPerson.setTaskid(processInstanceId);
				insuredPerson.setCreatetime(date);
				insbPersonDao.insert(insuredPerson);
				INSBInsured temp = new INSBInsured();
				temp.setCreatetime(date);
				temp.setOperator(operator);
				temp.setTaskid(processInstanceId);
				temp.setPersonid(insuredPerson.getId());
				insbInsuredDao.insert(temp);
			}
		}else{
			INSBPerson insuredPerson = insbPersonDao.selectById(insuredhis.getPersonid());//查询数据库的原始数据
			insuredPerson = setPersonInfo(pageInsured, insuredPerson, operator);
			insuredPerson.setModifytime(date);
			insbPersonDao.updateById(insuredPerson);
		}
		return "success";
	}
	
	/**
	 * 修改被保人时，与被保人一致的人员信息做更新主要是personid
	 */
	public String eidtRelationPersonOfSameWithInsured(String processInstanceId, String inscomcode, String operator, 
			String oldInsuredId, String newInsuredId){
		Date date = new Date();
		// 同步投保人中间表历史表信息
		INSBApplicanthis applicanthis = new INSBApplicanthis();
		applicanthis.setTaskid(processInstanceId);
		applicanthis.setInscomcode(inscomcode);
		applicanthis = insbApplicanthisDao.selectOne(applicanthis);
		if(applicanthis == null){
			// 得到投保人中间表信息
			INSBApplicant applicantMap = new INSBApplicant();
			applicantMap.setTaskid(processInstanceId);
			applicantMap = insbApplicantDao.selectOne(applicantMap);
			if(applicantMap!=null && oldInsuredId.equals(applicantMap.getPersonid())){//和被保人一致
				applicanthis = EntityTransformUtil.applicant2Applicanthis(applicantMap, inscomcode);
				applicanthis.setCreatetime(date);
				applicanthis.setModifytime(null);
				applicanthis.setOperator(operator);
				applicanthis.setPersonid(newInsuredId);
				insbApplicanthisDao.insert(applicanthis);
			}
		}else{
			if(oldInsuredId.equals(applicanthis.getPersonid())){//和被保人一致
				applicanthis.setModifytime(date);
				applicanthis.setOperator(operator);
				applicanthis.setPersonid(newInsuredId);
				insbApplicanthisDao.updateById(applicanthis);
			}
		}
		// 同步权益索赔人中间历史表信息
		INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
		legalrightclaimhis.setTaskid(processInstanceId);
		legalrightclaimhis.setInscomcode(inscomcode);
		legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
		// 得到权益索赔人信息
		if(legalrightclaimhis == null){
			// 得到权益索赔人中间表信息
			INSBLegalrightclaim legalrightclaimMap = new INSBLegalrightclaim();
			legalrightclaimMap.setTaskid(processInstanceId);
			legalrightclaimMap = insbLegalrightclaimDao.selectOne(legalrightclaimMap);
			if(legalrightclaimMap!=null && oldInsuredId.equals(legalrightclaimMap.getPersonid())){//和被保人一致
				legalrightclaimhis = EntityTransformUtil.legalrightclaim2Legalrightclaimhis(legalrightclaimMap, inscomcode);
				legalrightclaimhis.setCreatetime(date);
				legalrightclaimhis.setModifytime(null);
				legalrightclaimhis.setOperator(operator);
				legalrightclaimhis.setPersonid(newInsuredId);
				insbLegalrightclaimhisDao.insert(legalrightclaimhis);
			}
		}else{
			if(oldInsuredId.equals(legalrightclaimhis.getPersonid())){//和被保人一致
				legalrightclaimhis.setModifytime(date);
				legalrightclaimhis.setOperator(operator);
				legalrightclaimhis.setPersonid(newInsuredId);
				insbLegalrightclaimhisDao.updateById(legalrightclaimhis);
			}
		}
		// 同步联系人中间历史表信息
		INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
		relationpersonhis.setTaskid(processInstanceId);
		relationpersonhis.setInscomcode(inscomcode);
		relationpersonhis = insbRelationpersonhisDao.selectOne(relationpersonhis);
		// 得到联系人信息
		if(relationpersonhis == null){
			// 得到联系人中间表信息
			INSBRelationperson relationpersonMap = new INSBRelationperson();
			relationpersonMap.setTaskid(processInstanceId);
			relationpersonMap = insbRelationpersonDao.selectOne(relationpersonMap);
			if(relationpersonMap!=null && oldInsuredId.equals(relationpersonMap.getPersonid())){//和被保人一致
				relationpersonhis = EntityTransformUtil.relationperson2Relationpersonhis(relationpersonMap, inscomcode);
				relationpersonhis.setCreatetime(date);
				relationpersonhis.setModifytime(null);
				relationpersonhis.setOperator(operator);
				relationpersonhis.setPersonid(newInsuredId);
				insbRelationpersonhisDao.insert(relationpersonhis);
			}
		}else{
			if(oldInsuredId.equals(relationpersonhis.getPersonid())){//和被保人一致
				relationpersonhis.setModifytime(date);
				relationpersonhis.setOperator(operator);
				relationpersonhis.setPersonid(newInsuredId);
				insbRelationpersonhisDao.updateById(relationpersonhis);
			}
		}
		return "success";
	}

	/**
	 * 把修改后的关系人信息赋值保存使用
	 * */
	public INSBPerson setPersonInfo(INSBPerson pagePerson, INSBPerson dbPerson, String operator) {
		dbPerson.setName(pagePerson.getName());
		dbPerson.setGender(this.getGenderByIdtypeAndIdNo(pagePerson.getIdcardtype(), pagePerson.getIdcardno()));
		dbPerson.setIdcardtype(pagePerson.getIdcardtype());
		dbPerson.setIdcardno(pagePerson.getIdcardno());
		dbPerson.setCellphone(pagePerson.getCellphone());
		dbPerson.setEmail(pagePerson.getEmail());
		dbPerson.setOperator(operator);
		return dbPerson;
	}
	/**
	 * 把修改后的关系人信息赋值保存使用（没有电话和邮箱）
	 * */
	public INSBPerson setPersonInfo1(INSBPerson pagePerson, INSBPerson dbPerson, String operator) {
		dbPerson.setName(pagePerson.getName());
		dbPerson.setGender(this.getGenderByIdtypeAndIdNo(pagePerson.getIdcardtype(), pagePerson.getIdcardno()));
		dbPerson.setIdcardtype(pagePerson.getIdcardtype());
		dbPerson.setIdcardno(pagePerson.getIdcardno());
		dbPerson.setCellphone(pagePerson.getCellphone());
		dbPerson.setEmail(pagePerson.getEmail());
		dbPerson.setOperator(operator);
		return dbPerson;
	}

	/**
	 * 提交投保前获得投保人信息接口
	 * */
	@Override
	public String getApplicantInfoBeforePolicy(String processInstanceId, String inscomcode) {
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			//组织响应数据
			Map<String, Object> applicant = getApplicantInfoMap(processInstanceId, inscomcode);
			body.put("processInstanceId", processInstanceId);//流程实例id
			body.put("inscomcode", inscomcode);//保险公司code
			body.put("applicant", applicant);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询投保人信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询投保人信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	/**
	 * 封装投保人信息
	 * */
	public Map<String, Object> getApplicantInfoMap(String processInstanceId, String inscomcode) {
		Map<String, Object> applicant = new HashMap<String, Object>();//投保人信息
		// 得到被保人信息
		INSBPerson insuredPerson = getCarOwnerInfo(processInstanceId, inscomcode);
		// 得到投保人信息
		INSBPerson applicantPerson = getApplicantInfo(processInstanceId, inscomcode);
		if(applicantPerson!=null){
			applicant.put("isSameWithInsured", insuredPerson.getId().equals(applicantPerson.getId()));//是否和被保人一致
			if(!insuredPerson.getId().equals(applicantPerson.getId())){
				applicant.put("id", applicantPerson.getId());//投保人id
				applicant.put("name", applicantPerson.getName());//投保人姓名
				applicant.put("gender", applicantPerson.getGender());//投保人性别
				applicant.put("idcardtype", applicantPerson.getIdcardtype());//投保人证件类型
				applicant.put("idcardno", applicantPerson.getIdcardno());//投保人证件号
				applicant.put("cellphone", applicantPerson.getCellphone());//投保人手机号
				applicant.put("email", applicantPerson.getEmail());//投保人邮箱
			}
		}else{
			applicant.put("isSameWithInsured", false);//是否和被保人一致
			applicant.put("id", "");//投保人id
			applicant.put("name", "");//投保人姓名
			applicant.put("gender", "");//投保人性别
			applicant.put("idcardtype", "");//投保人证件类型
			applicant.put("idcardno", "");//投保人证件号
			applicant.put("cellphone", "");//投保人手机号
			applicant.put("email", "");//投保人邮箱
		}
		return applicant;
	}
	
	/**
	 * 封装投保人信息
	 * */
	public Map<String, Object> getApplicantInfoMap02(String processInstanceId, String inscomcode) {
		Map<String, Object> applicant = new HashMap<String, Object>();//投保人信息
		// 得到投保人信息
		INSBPerson applicantPerson = getApplicantInfo(processInstanceId, inscomcode);

		if(applicantPerson!=null){// 得到被保人信息
            INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
			applicant.put("isSameWithInsured", insuredPerson!=null && insuredPerson.getId().equals(applicantPerson.getId()));//是否和被保人一致
			applicant.put("id", applicantPerson.getId());//投保人id
			applicant.put("name", applicantPerson.getName());//投保人姓名
			applicant.put("gender", applicantPerson.getGender());//投保人性别
			applicant.put("idcardtype", applicantPerson.getIdcardtype());//投保人证件类型
			applicant.put("idcardno", applicantPerson.getIdcardno());//投保人证件号
			applicant.put("cellphone", applicantPerson.getCellphone());//投保人手机号
			applicant.put("email", applicantPerson.getEmail());//投保人邮箱
		}else{
			applicant.put("isSameWithInsured", false);//是否和被保人一致
			applicant.put("id", "");//投保人id
			applicant.put("name", "");//投保人姓名
			applicant.put("gender", "");//投保人性别
			applicant.put("idcardtype", "");//投保人证件类型
			applicant.put("idcardno", "");//投保人证件号
			applicant.put("cellphone", "");//投保人手机号
			applicant.put("email", "");//投保人邮箱
		}
		return applicant;
	}
	
	/**
	 * 查询投保人信息
	 * */
	public INSBPerson getApplicantInfo(String processInstanceId, String inscomcode) {
		// 得到投保人中间表历史表信息
		INSBApplicanthis applicanthis = new INSBApplicanthis();
		applicanthis.setTaskid(processInstanceId);
		applicanthis.setInscomcode(inscomcode);
		applicanthis = insbApplicanthisDao.selectOne(applicanthis);
		// 得到投保人信息
		INSBPerson applicantPerson = null;
		if(applicanthis == null){
			// 得到投保人中间表信息
			INSBApplicant applicantMap = new INSBApplicant();
			applicantMap.setTaskid(processInstanceId);
			applicantMap = insbApplicantDao.selectOne(applicantMap);
			if(applicantMap!=null){
				applicantPerson = insbPersonDao.selectById(applicantMap.getPersonid());
			}
		}else{
			applicantPerson = insbPersonDao.selectById(applicanthis.getPersonid());
		}
		return applicantPerson;
	}

	/**
	 * 提交投保前修改投保人信息接口
	 * */
	@Override
	public String editApplicantInfoBeforePolicy(String taskId, String inscomcode, String operator,
			String applicantInfoJSON, boolean isSameWithInsured) {
		/*// 先判断修改前投保人是否同被保人
		// 得到被保人信息
		//INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
		// 得到车主信息
		INSBPerson insuredPerson = getCarOwnerInfo(processInstanceId, inscomcode);
		// 得到投保人中间历史表信息
		INSBPerson applicantPerson = null;
		INSBApplicanthis applicanthis = new INSBApplicanthis();
		applicanthis.setTaskid(processInstanceId);
		applicanthis.setInscomcode(inscomcode);
		applicanthis = insbApplicanthisDao.selectOne(applicanthis);
		INSBApplicant applicantMap = null;
		if(applicanthis == null){
			// 得到投保人中间表信息
			applicantMap = new INSBApplicant();
			applicantMap.setTaskid(processInstanceId);
			applicantMap = insbApplicantDao.selectOne(applicantMap);
			if(applicantMap!=null){
				applicantPerson = insbPersonDao.selectById(applicantMap.getPersonid());
			}
		}else{
			applicantPerson = insbPersonDao.selectById(applicanthis.getPersonid());
		}
		boolean isSameWithInsuredBefore = false;
		if(applicantPerson!=null){
			if(insuredPerson.getId().equals(applicantPerson.getId())){
				isSameWithInsuredBefore = true;//判断修改前是否通被保人
			}
		}
		// 解析修改后的信息json字符串
		INSBPerson pageApplicant = null;
		if(!isSameWithInsured){
			JSONObject jo = JSONObject.fromObject(applicantInfoJSON);
			pageApplicant = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
		}
		// 修改被保人信息并返回修改状态
		Date date = new Date();
		if(isSameWithInsuredBefore){
			if(!isSameWithInsured){//原来相同修改后不同于被保人，新建人员信息，中间历史表记录存在做修改，不存在做添加，重新引用人员id
				pageApplicant.setCreatetime(date);
				pageApplicant.setOperator(operator);
				pageApplicant.setTaskid(processInstanceId);
				pageApplicant.setGender(this.getGenderByIdtypeAndIdNo(
						pageApplicant.getIdcardtype(), pageApplicant.getIdcardno()));
				pageApplicant.setId(null);
				insbPersonDao.insert(pageApplicant);
				if(applicanthis == null){
					applicanthis = EntityTransformUtil.applicant2Applicanthis(applicantMap, inscomcode);
					applicanthis.setCreatetime(date);
					applicanthis.setModifytime(null);
					applicanthis.setOperator(operator);
					applicanthis.setPersonid(pageApplicant.getId());
					insbApplicanthisDao.insert(applicanthis);
				}else{
					applicanthis.setModifytime(date);
					applicanthis.setOperator(operator);
					applicanthis.setPersonid(pageApplicant.getId());
					insbApplicanthisDao.updateById(applicanthis);
				}
			}
		}else{
			if(applicantPerson!=null){
				if(isSameWithInsured){//原来不同，修改后相同于被保人，中间历史表记录存在做修改，不存在做添加，重新引用人员id
					if(applicanthis == null){
						applicanthis = EntityTransformUtil.applicant2Applicanthis(applicantMap, inscomcode);
						applicanthis.setCreatetime(date);
						applicanthis.setModifytime(null);
						applicanthis.setOperator(operator);
						applicanthis.setPersonid(insuredPerson.getId());
						insbApplicanthisDao.insert(applicanthis);
					}else{
						applicanthis.setModifytime(date);
						applicanthis.setOperator(operator);
						applicanthis.setPersonid(insuredPerson.getId());
						insbApplicanthisDao.updateById(applicanthis);
					}
				}else{//原来不同修改后也不同于被保人，中间历史表记录存在则人员信息做修改，不存在做添加且人员信息做添加记录
					applicantPerson = setPersonInfo(pageApplicant, applicantPerson, operator);
					if(applicanthis == null){
						applicantPerson.setCreatetime(date);
						applicantPerson.setModifytime(null);
						applicantPerson.setId(null);
						insbPersonDao.insert(applicantPerson);
						applicanthis = EntityTransformUtil.applicant2Applicanthis(applicantMap, inscomcode);
						applicanthis.setCreatetime(date);
						applicanthis.setModifytime(null);
						applicanthis.setOperator(operator);
						applicanthis.setPersonid(applicantPerson.getId());
						insbApplicanthisDao.insert(applicanthis);
					}else{
						applicantPerson.setModifytime(date);
						insbPersonDao.updateById(applicantPerson);
					}
				}
			}else{
				if(isSameWithInsured){
					INSBApplicant temp = new INSBApplicant();
					temp.setTaskid(processInstanceId);
					temp.setOperator(operator);
					temp.setCreatetime(date);
					temp.setPersonid(insuredPerson.getId());
					insbApplicantDao.insert(temp);
				}else{
					applicantPerson = new INSBPerson();
					applicantPerson = setPersonInfo(pageApplicant, applicantPerson, operator);
					applicantPerson.setTaskid(processInstanceId);
					applicantPerson.setCreatetime(date);
					insbPersonDao.insert(applicantPerson);
					INSBApplicant temp = new INSBApplicant();
					temp.setTaskid(processInstanceId);
					temp.setOperator(operator);
					temp.setCreatetime(date);
					temp.setPersonid(applicantPerson.getId());
					insbApplicantDao.insert(temp);
				}
			}
		}
		return "success";
		*/
		Date date = new Date();			
		//车主人员信息表
		INSBPerson insbPerson = getCarOwnerInfo(taskId, inscomcode);
		if(StringUtil.isEmpty(insbPerson)){
			LogUtil.info("前端提交投保,修改投保人信息,车主人员信息不存在：taskid="+taskId+",inscomcode="+inscomcode+",操作人="+operator+",Json="+applicantInfoJSON);
			return CommonModel.STATUS_FAIL;
		}
		//投保人人员信息表
		INSBPerson insbPersonApplicant = new INSBPerson();
		//投保人表信息,为空的时候创建投保人信息,否则直接获取对应人员信息
		INSBApplicant applicant = new INSBApplicant();
		applicant.setTaskid(taskId);
		applicant = insbApplicantDao.selectOne(applicant);
		if(StringUtil.isEmpty(applicant)){
			insbPersonApplicant=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_1);
		}
		//判断投保人历史表是否有关联记录，没有添加
		INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
        insbApplicanthis.setTaskid(taskId);
        insbApplicanthis.setInscomcode(inscomcode);
        insbApplicanthis = insbApplicanthisDao.selectOne(insbApplicanthis);
        if (StringUtil.isEmpty(insbApplicanthis)) {
        	insbPersonApplicant=insbPersonHelpService.addPersonHisIsNull(applicant,taskId, inscomcode, ConstUtil.STATUS_1);
        }else{
        	insbPersonApplicant=insbPersonDao.selectById(insbApplicanthis.getPersonid());
        }
		//与车主一致
		if(isSameWithInsured){
			insbPersonApplicant=insbPersonHelpService.repairPerson(insbPersonApplicant,insbPerson,operator);
			insbPersonDao.updateById(insbPersonApplicant);	
		}else{
			JSONObject jo = JSONObject.fromObject(applicantInfoJSON);
			INSBPerson insbPersonJson = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
			insbPersonApplicant=insbPersonHelpService.updateINSBPerson(insbPersonApplicant,insbPersonJson,operator,date);	
		}
		//更新保单表
		insbPersonHelpService.updateInsbPolicyitemApplicant(taskId,insbApplicanthis.getId(),insbPersonApplicant.getName());
		return CommonModel.STATUS_SUCCESS;
	}

	/**
	 * 提交投保前获得权益索赔人信息接口
	 * */
	@Override
	public String getLegalrightclaimInfoBeforePolicy(String processInstanceId, String inscomcode) {
		//组织响应数据
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Map<String, Object> legalrightclaim = getLegalrightclaimInfoMap(processInstanceId, inscomcode);
			body.put("inscomcode", inscomcode);//保险公司code
			body.put("processInstanceId", processInstanceId);//流程实例id
			body.put("legalrightclaim", legalrightclaim);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询权益索赔人信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询权益索赔人信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	/**
	 * 封装权益索赔人信息
	 * */
	public Map<String, Object> getLegalrightclaimInfoMap(String processInstanceId, String inscomcode) {
		Map<String, Object> legalrightclaim = new HashMap<String, Object>();//权益索赔人信息
		// 得到被保人信息
		INSBPerson insuredPerson = getCarOwnerInfo(processInstanceId, inscomcode);
		// 得到权益索赔人中间表信息
		INSBPerson legalrightclaimPerson = getLegalrightclaimInfo(processInstanceId, inscomcode);
		if(legalrightclaimPerson!=null){
			legalrightclaim.put("isSameWithInsured", insuredPerson.getId().equals(legalrightclaimPerson.getId()));//是否和被保人一致
			if(!insuredPerson.getId().equals(legalrightclaimPerson.getId())){
				legalrightclaim.put("id", legalrightclaimPerson.getId());//权益索赔人id
				legalrightclaim.put("name", legalrightclaimPerson.getName());//权益索赔人姓名
				legalrightclaim.put("gender", legalrightclaimPerson.getGender());//权益索赔人性别
				legalrightclaim.put("idcardtype", legalrightclaimPerson.getIdcardtype());//权益索赔人证件类型
				legalrightclaim.put("idcardno", legalrightclaimPerson.getIdcardno());//权益索赔人证件号
				legalrightclaim.put("cellphone", legalrightclaimPerson.getCellphone());//权益索赔人手机号
				legalrightclaim.put("email", legalrightclaimPerson.getEmail());//权益索赔人邮箱
			}
		}else{
			legalrightclaim.put("isSameWithInsured", false);//是否和被保人一致
			legalrightclaim.put("id", "");//权益索赔人id
			legalrightclaim.put("name", "");//权益索赔人姓名
			legalrightclaim.put("gender", "");//权益索赔人性别
			legalrightclaim.put("idcardtype", "");//权益索赔人证件类型
			legalrightclaim.put("idcardno", "");//权益索赔人证件号
			legalrightclaim.put("cellphone", "");//权益索赔人手机号
			legalrightclaim.put("email", "");//权益索赔人邮箱
		}
		return legalrightclaim;
	}
	
	/**
	 * 封装权益索赔人信息
	 * */
	public Map<String, Object> getLegalrightclaimInfoMap02(String processInstanceId, String inscomcode) {
		Map<String, Object> legalrightclaim = new HashMap<String, Object>();//权益索赔人信息
		// 得到权益索赔人中间表信息
		INSBPerson legalrightclaimPerson = getLegalrightclaimInfo(processInstanceId, inscomcode);

		if(legalrightclaimPerson!=null){
            // 得到被保人信息
            INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
			legalrightclaim.put("isSameWithInsured", insuredPerson!=null && insuredPerson.getId().equals(legalrightclaimPerson.getId()));//是否和被保人一致
			legalrightclaim.put("id", legalrightclaimPerson.getId());//权益索赔人id
			legalrightclaim.put("name", legalrightclaimPerson.getName());//权益索赔人姓名
			legalrightclaim.put("gender", legalrightclaimPerson.getGender());//权益索赔人性别
			legalrightclaim.put("idcardtype", legalrightclaimPerson.getIdcardtype());//权益索赔人证件类型
			legalrightclaim.put("idcardno", legalrightclaimPerson.getIdcardno());//权益索赔人证件号
			legalrightclaim.put("cellphone", legalrightclaimPerson.getCellphone());//权益索赔人手机号
			legalrightclaim.put("email", legalrightclaimPerson.getEmail());//权益索赔人邮箱
		}else{
			legalrightclaim.put("isSameWithInsured", false);//是否和被保人一致
			legalrightclaim.put("id", "");//权益索赔人id
			legalrightclaim.put("name", "");//权益索赔人姓名
			legalrightclaim.put("gender", "");//权益索赔人性别
			legalrightclaim.put("idcardtype", "");//权益索赔人证件类型
			legalrightclaim.put("idcardno", "");//权益索赔人证件号
			legalrightclaim.put("cellphone", "");//权益索赔人手机号
			legalrightclaim.put("email", "");//权益索赔人邮箱
		}
		return legalrightclaim;
	}
	
	/**
	 * 查询权益索赔人信息
	 * */
	public INSBPerson getLegalrightclaimInfo(String processInstanceId, String inscomcode) {
		// 得到权益索赔人中间历史表信息
		INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
		legalrightclaimhis.setTaskid(processInstanceId);
		legalrightclaimhis.setInscomcode(inscomcode);
		legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
		// 得到权益索赔人信息
		INSBPerson legalrightclaimPerson = null;
		if(legalrightclaimhis == null){
			// 得到权益索赔人中间表信息
			INSBLegalrightclaim legalrightclaimMap = new INSBLegalrightclaim();
			legalrightclaimMap.setTaskid(processInstanceId);
			legalrightclaimMap = insbLegalrightclaimDao.selectOne(legalrightclaimMap);
			if(legalrightclaimMap!=null){
				legalrightclaimPerson = insbPersonDao.selectById(legalrightclaimMap.getPersonid());
			}
		}else{
			legalrightclaimPerson = insbPersonDao.selectById(legalrightclaimhis.getPersonid());
		}
		return legalrightclaimPerson;
	}

	/**
	 * 提交投保前修改权益索赔人信息接口
	 * */
	@Override
	public String editLegalrightclaimInfoBeforePolicy(String taskId, String inscomcode, String operator,
			String legalrightclaimInfoJSON, boolean isSameWithInsured) {
		/*// 先判断修改前权益索赔人是否同被保人
		// 得到被保人信息
		//INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
		// 得到车主信息
		INSBPerson insuredPerson = getCarOwnerInfo(processInstanceId, inscomcode);
		// 得到权益索赔人中间历史表信息
		INSBPerson legalrightclaimPerson = null;
		INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
		legalrightclaimhis.setTaskid(processInstanceId);
		legalrightclaimhis.setInscomcode(inscomcode);
		legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
		INSBLegalrightclaim legalrightclaimMap = null;
		// 查询权益索赔人信息
		if(legalrightclaimhis == null){
			// 得到权益索赔人中间表信息
			legalrightclaimMap = new INSBLegalrightclaim();
			legalrightclaimMap.setTaskid(processInstanceId);
			legalrightclaimMap = insbLegalrightclaimDao.selectOne(legalrightclaimMap);
			if(legalrightclaimMap!=null){
				legalrightclaimPerson = insbPersonDao.selectById(legalrightclaimMap.getPersonid());
			}
		}else{
			legalrightclaimPerson = insbPersonDao.selectById(legalrightclaimhis.getPersonid());
		}
		boolean isSameWithInsuredBefore = false;
		if(legalrightclaimPerson!=null){
			if(insuredPerson.getId().equals(legalrightclaimPerson.getId())){
				isSameWithInsuredBefore = true;//判断修改前是否通被保人
			}
		}
		// 解析修改后的信息json字符串
		INSBPerson pageLegalrightclaim = null;
		if(!isSameWithInsured){
			JSONObject jo = JSONObject.fromObject(legalrightclaimInfoJSON);
			pageLegalrightclaim = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
		}
		// 修改被保人信息并返回修改状态
		Date date = new Date();
		if(isSameWithInsuredBefore){
			if(!isSameWithInsured){//原来相同修改后不同于被保人，新建人员信息，中间历史表记录存在做修改，不存在做添加，重新引用人员id
				pageLegalrightclaim.setCreatetime(date);
				pageLegalrightclaim.setOperator(operator);
				pageLegalrightclaim.setTaskid(processInstanceId);
				pageLegalrightclaim.setGender(this.getGenderByIdtypeAndIdNo(
						pageLegalrightclaim.getIdcardtype(), pageLegalrightclaim.getIdcardno()));
				pageLegalrightclaim.setId(null);
				insbPersonDao.insert(pageLegalrightclaim);
				if(legalrightclaimhis == null){
					legalrightclaimhis = EntityTransformUtil.legalrightclaim2Legalrightclaimhis(legalrightclaimMap, inscomcode);
					legalrightclaimhis.setCreatetime(date);
					legalrightclaimhis.setModifytime(null);
					legalrightclaimhis.setOperator(operator);
					legalrightclaimhis.setPersonid(pageLegalrightclaim.getId());
					insbLegalrightclaimhisDao.insert(legalrightclaimhis);
				}else{
					legalrightclaimhis.setModifytime(date);
					legalrightclaimhis.setOperator(operator);
					legalrightclaimhis.setPersonid(pageLegalrightclaim.getId());
					insbLegalrightclaimhisDao.updateById(legalrightclaimhis);
				}
			}
		}else{
			if(legalrightclaimPerson!=null){
				if(isSameWithInsured){//原来不同，修改后相同于被保人，中间历史表记录存在做修改，不存在做添加，重新引用人员id
					if(legalrightclaimhis == null){
						legalrightclaimhis = EntityTransformUtil.legalrightclaim2Legalrightclaimhis(legalrightclaimMap, inscomcode);
						legalrightclaimhis.setCreatetime(date);
						legalrightclaimhis.setModifytime(null);
						legalrightclaimhis.setOperator(operator);
						legalrightclaimhis.setPersonid(insuredPerson.getId());
						insbLegalrightclaimhisDao.insert(legalrightclaimhis);
					}else{
						legalrightclaimhis.setModifytime(date);
						legalrightclaimhis.setOperator(operator);
						legalrightclaimhis.setPersonid(insuredPerson.getId());
						insbLegalrightclaimhisDao.updateById(legalrightclaimhis);
					}
				}else{//原来不同修改后也不同于被保人，中间历史表记录存在则人员信息做修改，不存在做添加且人员信息做添加记录
					legalrightclaimPerson = setPersonInfo(pageLegalrightclaim, legalrightclaimPerson, operator);
					if(legalrightclaimhis == null){
						legalrightclaimPerson.setCreatetime(date);
						legalrightclaimPerson.setModifytime(null);
						legalrightclaimPerson.setId(null);
						insbPersonDao.insert(legalrightclaimPerson);
						legalrightclaimhis = EntityTransformUtil.legalrightclaim2Legalrightclaimhis(legalrightclaimMap, inscomcode);
						legalrightclaimhis.setCreatetime(date);
						legalrightclaimhis.setModifytime(null);
						legalrightclaimhis.setOperator(operator);
						legalrightclaimhis.setPersonid(legalrightclaimPerson.getId());
						insbLegalrightclaimhisDao.insert(legalrightclaimhis);
					}else{
						legalrightclaimPerson.setModifytime(date);
						insbPersonDao.updateById(legalrightclaimPerson);
					}
				}
			}else{
				if(isSameWithInsured){
					INSBLegalrightclaim temp = new INSBLegalrightclaim();
					temp.setTaskid(processInstanceId);
					temp.setCreatetime(date);
					temp.setOperator(operator);
					temp.setPersonid(insuredPerson.getId());
					insbLegalrightclaimDao.insert(temp);
				}else{
					legalrightclaimPerson = new INSBPerson();
					legalrightclaimPerson = setPersonInfo(pageLegalrightclaim, legalrightclaimPerson, operator);
					legalrightclaimPerson.setTaskid(processInstanceId);
					legalrightclaimPerson.setCreatetime(date);
					insbPersonDao.insert(legalrightclaimPerson);
					INSBLegalrightclaim temp = new INSBLegalrightclaim();
					temp.setTaskid(processInstanceId);
					temp.setCreatetime(date);
					temp.setOperator(operator);
					temp.setPersonid(legalrightclaimPerson.getId());
					insbLegalrightclaimDao.insert(temp);
				}
			}
		}
		return "success";
		*/
		Date date = new Date();			
		//车主人员信息表
		INSBPerson insbPerson = getCarOwnerInfo(taskId, inscomcode);
		if(StringUtil.isEmpty(insbPerson)){
			LogUtil.info("前端提交投保,修改权益人信息,车主人员信息不存在：taskid="+taskId+",inscomcode="+inscomcode+",操作人="+operator+",Json="+legalrightclaimInfoJSON);
			return CommonModel.STATUS_FAIL;
		}
		//权益人人员信息表
		INSBPerson insbPersonLegalrightclaim = new INSBPerson();
		//权益人表信息
		INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
		insbLegalrightclaim.setTaskid(taskId);
		insbLegalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);
		//为空的时候创建权益人信息,否则直接获取对应人员信息
		if(insbLegalrightclaim==null){
			insbPersonLegalrightclaim=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_3);
		}
		//判断权益人历史表是否有关联记录，没有添加
		INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
        insbLegalrightclaimhis.setTaskid(taskId);
        insbLegalrightclaimhis.setInscomcode(inscomcode);
        insbLegalrightclaimhis = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
        if (StringUtil.isEmpty(insbLegalrightclaimhis)) {
        	insbPersonLegalrightclaim=insbPersonHelpService.addPersonHisIsNull(insbLegalrightclaim,taskId, inscomcode, ConstUtil.STATUS_3);
        }else{
        	insbPersonLegalrightclaim=insbPersonDao.selectById(insbLegalrightclaimhis.getPersonid());
        }
		//与车主一致
		if(isSameWithInsured){
			insbPersonLegalrightclaim=insbPersonHelpService.repairPerson(insbPersonLegalrightclaim,insbPerson,operator);
			insbPersonDao.updateById(insbPersonLegalrightclaim);	
		}else{
			JSONObject jo = JSONObject.fromObject(legalrightclaimInfoJSON);
			INSBPerson insbPersonJson = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
			insbPersonLegalrightclaim=insbPersonHelpService.updateINSBPerson(insbPersonLegalrightclaim,insbPersonJson,operator,date);	
		}
		return CommonModel.STATUS_SUCCESS;
	}

	/**
	 *    提交投保前获得所有投保信息接口
	 * */
	@Override
	public String getCarTaskInfoBeforePolicy(String processInstanceId, String inscomcode) {
		//获取投保前所有投保信息由客户确认
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//查询车辆信息
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(inscomcode);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			String carinfoid = carinfo.getId();
			if(carinfohis != null){//统一转换为INSBCarinfo类型，方便后面组织数据
				carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
			}
			//查询车型信息
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			INSBCarmodelinfo carmodelinfo = null;
			if(carmodelinfohis == null){
				carmodelinfo = new INSBCarmodelinfo();
				carmodelinfo.setCarinfoid(carinfoid);
				carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
			}else{
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
			//组织响应数据
			//车辆车型信息
			body.put("inscomcode", inscomcode);//保险公司code
			body.put("processInstanceId", processInstanceId);//流程实例id
			Map<String, Object> carInfo = new HashMap<String, Object>();//车辆信息
			carInfo.put("carlicenseno", carinfo.getCarlicenseno());//车牌号码
			carInfo.put("standardfullname", carmodelinfo.getStandardfullname());//品牌型号
			carInfo.put("cardeploy", carmodelinfo.getCardeploy());//车辆配置
			carInfo.put("ownername", carinfo.getOwnername());//车主姓名
			carInfo.put("engineno", carinfo.getEngineno());//发动机号
			carInfo.put("vincode", carinfo.getVincode());//车辆识别代码
			carInfo.put("drivingarea", carinfo.getDrivingarea());//行驶区域
			carInfo.put("mileage", carinfo.getMileage());//平均行驶里程
			carInfo.put("seat", carmodelinfo.getSeat());//核定载人数
			carInfo.put("unwrtweight", carmodelinfo.getUnwrtweight());//核定载质量
			carInfo.put("fullweight", carmodelinfo.getFullweight());//整备质量
			carInfo.put("registdate", sdf.format(carinfo.getRegistdate()).toString());//车辆初登日期
			body.put("carInfo", carInfo);
			// 获得商业险的交强险的生效日期
			String busPolicyDate = "未指定";
			String strPolicyDate = "未指定";
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setTaskid(processInstanceId);
			policyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
			for (int i = 0; i < policyitemList.size(); i++) {
				INSBPolicyitem plit = policyitemList.get(i);
				if("0".equals(plit.getRisktype())){//商业险起保日期
					if(plit.getStartdate()!=null && plit.getEnddate()!=null){
						busPolicyDate = "自"+sdf.format(plit.getStartdate()).toString()
								+"零时起至"+sdf.format(plit.getEnddate()).toString()+"二十四时止";
					}
				}else if("1".equals(plit.getRisktype())){//交强险起保日期
					if(plit.getStartdate()!=null && plit.getEnddate()!=null){
						strPolicyDate = "自"+sdf.format(plit.getStartdate()).toString()
								+"零时起至"+sdf.format(plit.getEnddate()).toString()+"二十四时止";
					}
				}
			}
			//保险配置报价信息
			//INSBProvider provider = insbProviderDao.queryByPrvcode(inscomcode);
			INSBProvider provider = insbProviderDao.selectById(inscomcode.substring(0,4));
			Map<String, Object> quoteInfo = new HashMap<String, Object>();//保险配置及报价
			if(provider!=null){
				quoteInfo.put("inscompanyName", provider.getPrvshotname());//投保公司名称
			}else{
				quoteInfo.put("inscompanyName", "未知保险公司");//投保公司名称
			}
			//得到保险配置报价信息
			Map<String, Object> carkindpriceInfo = getCarkindpriceInfo(processInstanceId, inscomcode);
			//定义车船税、商业险总保费、交强险保费
			double carTax = (double) carkindpriceInfo.get("carTax");//车船税
			double busTotalAmountprice = (double) carkindpriceInfo.get("busTotalAmountprice");//商业险保费
			double strTotalAmountprice = (double) carkindpriceInfo.get("strTotalAmountprice");//交强险保费
			double discountCarTax = (double) carkindpriceInfo.get("discountCarTax");//折后车船税
			double discountBusTotalAmountprice = (double) carkindpriceInfo.get("discountBusTotalAmountprice");//折后商业险保费
			double discountStrTotalAmountprice = (double) carkindpriceInfo.get("discountStrTotalAmountprice");//折后交强险保费
			Map<String, Object> busQuoteInfo = new HashMap<String, Object>();//商业险信息
			busQuoteInfo.put("busQuoteInfoList", carkindpriceInfo.get("busQuoteInfoList"));
			busQuoteInfo.put("busQuoteAmountPrice", discountBusTotalAmountprice);//商业险保费总额
			quoteInfo.put("busQuoteInfo", busQuoteInfo);
			quoteInfo.put("strQuoteInfoList", carkindpriceInfo.get("strQuoteInfoList"));
			body.put("quoteInfo", quoteInfo);
			//关系人信息
			Map<String, Object> insured = getInsuredInfoMap(processInstanceId, inscomcode);//被保人信息
			body.put("insured", insured);
			Map<String, Object> applicant = getApplicantInfoMap(processInstanceId, inscomcode);//投保人信息
			body.put("applicant", applicant);
			Map<String, Object> legalrightclaim = getLegalrightclaimInfoMap(processInstanceId, inscomcode);//权益索赔人信息
			body.put("legalrightclaim", legalrightclaim);
			Map<String, Object> carownerinfo = getCarOwnerInfoMap(processInstanceId, inscomcode);//车主信息
			body.put("carownerinfo", carownerinfo);
			//发票信息
			Map<String, Object> invoiceinfo =  getInvoiceInfoMap(processInstanceId, inscomcode);//发票信息
			if(invoiceinfo!=null){
				body.put("invoiceinfo", invoiceinfo);
			}
			//其他信息
			Map<String, Object> policyDate = new HashMap<String, Object>();//保险生效日期
			policyDate.put("busPolicyDate", busPolicyDate);//商业险生效日期
			policyDate.put("strPolicyDate", strPolicyDate);//交强险生效日期
			body.put("policyDate", policyDate);
			Map<String, Object> totalAmountprice = new HashMap<String, Object>();//保费合计
			totalAmountprice.put("strTotalAmountprice", strTotalAmountprice);//交强险保费
			totalAmountprice.put("busTotalAmountprice", busTotalAmountprice);//商业险保费
			totalAmountprice.put("carTax", carTax);//车船税
			totalAmountprice.put("total", douFormat(carTax+busTotalAmountprice+strTotalAmountprice));//总计
			totalAmountprice.put("discountStrTotalAmountprice", discountStrTotalAmountprice);//折后交强险保费
			totalAmountprice.put("discountBusTotalAmountprice", discountBusTotalAmountprice);//折后商业险保费
			totalAmountprice.put("discountCarTax", discountCarTax);//折后车船税
			totalAmountprice.put("discountTotal", douFormat(discountCarTax+discountBusTotalAmountprice+discountStrTotalAmountprice));//折后总计
			body.put("totalAmountprice", totalAmountprice);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询投保详细信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询投保详细信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}
	
	/**
	 * 将险别要素已选项json类型数据转换为List<SelectOption>实体类
	 * liuchao
	 * */
	public List<SelectOption> toSelectOptionList(String selectOptionJSON){
		List<SelectOption> selectOptionList = new ArrayList<SelectOption>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(selectOptionJSON);//字符串转换为JSONArray对象
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("VALUE", RisksData.class);//指定复杂对象的类型
			ArrayList<SelectOption> selectList = //现将字符串转换为集合
					(ArrayList<SelectOption>)JSONArray.toCollection(jsonArray, SelectOption.class);
			for (int i = 0; i < selectList.size(); i++) {
				//遍历数组并将元素转换为SelectOption类型
				JSONObject temp = JSONObject.fromObject(selectList.get(i));
				SelectOption selectOption = (SelectOption) JSONObject.toBean(temp, SelectOption.class, classMap);
				selectOptionList.add(selectOption);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectOptionList;
	}
	public Map<String, Double> getAllPremium(String processInstanceId, String inscomcode){
		Map<String,Double> result = new HashMap<String, Double>();
		//折后车船税、折后商业险总保费、折后交强险保费
		double discountPremium = 0d, premium = 0d;
		//查询保险配置报价信息
		INSBCarkindprice carkindprice = new INSBCarkindprice();
		carkindprice.setTaskid(processInstanceId);
		carkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carkindprice);
		for (INSBCarkindprice ckp : carkindpriceList) {
			if(ckp.getDiscountCharge()!=null){
				discountPremium += ckp.getDiscountCharge();//折后保费
				if(ckp.getAmountprice()!=null && ckp.getAmountprice().compareTo(0.0d)>0){
					premium += ckp.getAmountprice();//保费
				}else{
					premium += ckp.getDiscountCharge();//如果没有折前保费则使用折后保费
				}
			}
		}
		result.put("discountPremium", discountPremium);
		result.put("premium", premium);
		return result;
	}
	/**
	 * 通过流程实例id和保险公司code查询车险配置报价信息(获得所有投保信息接口使用)
	 * */
	public Map<String, Object> getCarkindpriceInfo(String processInstanceId, String inscomcode){
		Map<String,Object> result = new HashMap<String, Object>();
		//定义车船税、商业险总保费、交强险保费
		double carTax = 0d, busTotalAmountprice = 0d, strTotalAmountprice = 0d;
		//折后车船税、折后商业险总保费、折后交强险保费
		double discountCarTax = 0d, discountBusTotalAmountprice = 0d, discountStrTotalAmountprice = 0d;
		//查询保险配置报价信息
		INSBCarkindprice carkindprice = new INSBCarkindprice();
		carkindprice.setTaskid(processInstanceId);
		carkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carkindprice);
		INSBRisk risk = new INSBRisk();
		String provideid =insbProviderDao.selectFatherProvider(inscomcode).getId();
		risk.setProvideid(provideid);//根据顶级机构查询险种信息
//		risk.setProvideid(inscomcode);
		List<INSBRisk> riskList = insbRiskDao.selectList(risk);
		List<INSBRiskkind> riskkindList = new ArrayList<INSBRiskkind>();
		for (int i = 0; i < riskList.size(); i++) {
			INSBRiskkind temp = new INSBRiskkind();
			temp.setRiskid(riskList.get(i).getId());
			List<INSBRiskkind> tempRKL = insbRiskkindDao.selectList(temp);
			riskkindList.addAll(tempRKL);
		}
		//组织返回数据
		List<Map<String, Object>> busQuoteInfoList = new ArrayList<Map<String,Object>>();//商业险集合
		List<Map<String, Object>> strQuoteInfoList = new ArrayList<Map<String,Object>>();//交强险车船税集合
		List<INSBCarkindprice> busTemp = new ArrayList<INSBCarkindprice>();//临时存放商业险信息
		List<INSBCarkindprice> busNotdTemp = new ArrayList<INSBCarkindprice>();//临时存放不计免赔信息
		List<SelectOption> SelectOptionList = null;
		for (int i = 0; i < carkindpriceList.size(); i++) {
			INSBCarkindprice ckp = carkindpriceList.get(i);
			if("0".equals(ckp.getInskindtype())){//商业险别
				if(ckp.getDiscountCharge()!=null){
					discountBusTotalAmountprice += ckp.getDiscountCharge();//折后保费
					if(ckp.getAmountprice()!=null && ckp.getAmountprice()!=0.0d){
						busTotalAmountprice += ckp.getAmountprice();//保费
					}else{
						busTotalAmountprice += ckp.getDiscountCharge();//如果没有折前保费则使用折后保费
					}
				}
//				if("PassengerIns".equals(ckp.getInskindcode())){//乘客责任险显示单位元/座
//					Integer seats = getSeats(processInstanceId, inscomcode);
//					if(ckp.getAmount()!=null && seats!=null && seats!=0){
//						ckp.setAmount(douFormat(ckp.getAmount()/seats));//保额
//					}
//				}
				busTemp.add(ckp);
			}else if("1".equals(ckp.getInskindtype())){//商业不计免赔险别
				if(ckp.getDiscountCharge()!=null){
					discountBusTotalAmountprice += ckp.getDiscountCharge();//折后保费
					if(ckp.getAmountprice()!=null && ckp.getAmountprice()!=0.0d){
						busTotalAmountprice += ckp.getAmountprice();//保费
					}else{
						busTotalAmountprice += ckp.getDiscountCharge();//如果没有折前保费则使用折后保费
					}
				}
				busNotdTemp.add(ckp);
			}else if("2".equals(ckp.getInskindtype()) || "3".equals(ckp.getInskindtype())){
				if("2".equals(ckp.getInskindtype())){//交强险
					if(ckp.getDiscountCharge()!=null){
						discountStrTotalAmountprice += ckp.getDiscountCharge();//折后保费
						if(ckp.getAmountprice()!=null && ckp.getAmountprice()!=0.0d){
							strTotalAmountprice = ckp.getAmountprice();//保费
						}else{
							strTotalAmountprice += ckp.getDiscountCharge();//如果没有折前保费则使用折后保费
						}
					}
				}else if("3".equals(ckp.getInskindtype())){//车船税
					if(ckp.getDiscountCharge()!=null){
						discountCarTax += ckp.getDiscountCharge();//折后保费
						if(ckp.getAmountprice()!=null && ckp.getAmountprice()!=0.0d){
							carTax = ckp.getAmountprice();//保费
						}else{
							carTax += ckp.getDiscountCharge();//如果没有折前保费则使用折后保费
						}
					}
				}
//				String amountStr = "";
				Map<String, Object> strTemp = new HashMap<String, Object>();
				strTemp.put("inskindcode", ckp.getInskindcode());//险别编码
				strTemp.put("inskindName", getRiskkindName(ckp.getInskindcode(), riskkindList));//险别名称
//				if(ckp.getSelecteditem()!=null && !("".equals(ckp.getSelecteditem()))){
//					SelectOptionList = toSelectOptionList(ckp.getSelecteditem());
//					if(SelectOptionList!=null && SelectOptionList.size()>0){
//						for (int j = 0; j < SelectOptionList.size(); j++) {
//							if("01".equals(SelectOptionList.get(j).getTYPE())){
//								amountStr += douFormat(ckp.getAmount())+
//										SelectOptionList.get(j).getVALUE().getUNIT()+",";
//							}else{
//								amountStr += SelectOptionList.get(j).getVALUE().getKEY()+
//										SelectOptionList.get(j).getVALUE().getUNIT()+",";
//							}
//						}
//					}else{//直接取保额
//						amountStr = douFormat(ckp.getAmount())+",";
//					}
//				}else{//直接取保额
//					amountStr = douFormat(ckp.getAmount())+",";
//				}
//				if(amountStr.endsWith(",")){
//					amountStr = amountStr.substring(0, amountStr.length()-1);
//				}
//				strTemp.put("amount", amountStr);//保额
				strTemp.put("amount", ckp.getSelecteditem());//保额
				strTemp.put("amountprice", douFormat(ckp.getAmountprice()));//保费或车船税
				strTemp.put("discountCharge", douFormat(ckp.getDiscountCharge()));//折后保费或车船税
				strQuoteInfoList.add(strTemp);
			}
		}
		//调整商业险和不计免赔的顺序
		for (int i = 0; i < busTemp.size(); i++) {
			INSBCarkindprice ckpb = busTemp.get(i);
			String amountStr = "";
			Map<String, Object> btemp = new HashMap<String, Object>();
			btemp.put("inskindcode", ckpb.getInskindcode());//险别编码
			btemp.put("inskindName", getRiskkindName(ckpb.getInskindcode(), riskkindList));//险别名称
			if(ckpb.getSelecteditem()!=null && !("".equals(ckpb.getSelecteditem()))){
				SelectOptionList = toSelectOptionList(ckpb.getSelecteditem());
				if(SelectOptionList!=null && SelectOptionList.size()>0){
					for (int j = 0; j < SelectOptionList.size(); j++) {
						if("01".equals(SelectOptionList.get(j).getTYPE())){
							amountStr += douFormat(ckpb.getAmount())+
									SelectOptionList.get(j).getVALUE().getUNIT()+",";
						}else{
							amountStr += SelectOptionList.get(j).getVALUE().getKEY()+
									SelectOptionList.get(j).getVALUE().getUNIT()+",";
						}
					}
				}else{//直接取保额
					amountStr = douFormat(ckpb.getAmount())+",";
				}
			}else{//直接取保额
				amountStr = douFormat(ckpb.getAmount())+",";
			}
			if(amountStr.endsWith(",")){
				amountStr = amountStr.substring(0, amountStr.length()-1);
			}
			btemp.put("amount", amountStr);//商业险保额
			btemp.put("amountprice", douFormat(ckpb.getAmountprice()));//保费
			btemp.put("discountCharge", douFormat(ckpb.getDiscountCharge()));//折后保费
			busQuoteInfoList.add(btemp);
			for (int j = 0; j < busNotdTemp.size(); j++) {
				INSBCarkindprice ckpn = busNotdTemp.get(j);
				if(ckpn.getPreriskkind()!=null && ckpn.getPreriskkind().contains(ckpb.getInskindcode())){
					Map<String, Object> notemp = new HashMap<String, Object>();
					notemp.put("inskindcode", ckpn.getInskindcode());//险别编码
					notemp.put("inskindName", getRiskkindName(ckpn.getInskindcode(), riskkindList));//险别名称
					notemp.put("amount", "投保");//不计免赔是否投保
					notemp.put("amountprice", douFormat(ckpn.getAmountprice()));//保费
					notemp.put("discountCharge", douFormat(ckpn.getDiscountCharge()));//折后保费
					busQuoteInfoList.add(notemp);
					busNotdTemp.remove(j);
					j--;
				}
			}
		}
		if(busNotdTemp!=null && busNotdTemp.size()>0){
			for (int i = 0; i < busNotdTemp.size(); i++) {
				Map<String, Object> notemp = new HashMap<String, Object>();
				notemp.put("inskindcode", busNotdTemp.get(i).getInskindcode());//险别编码

				notemp.put("inskindName", getRiskkindName(busNotdTemp.get(i).getInskindcode(), riskkindList));//险别名称
				notemp.put("amount", "投保");//不计免赔是否投保
				notemp.put("amountprice", douFormat(busNotdTemp.get(i).getAmountprice()));//保费
				notemp.put("discountCharge", douFormat(busNotdTemp.get(i).getDiscountCharge()));//折后保费
				busQuoteInfoList.add(notemp);
			}
		}
		//特殊险别组织数据
		for (Map<String,Object> map : busQuoteInfoList) {
			INSCCode temp = new INSCCode();
			temp.setCodetype("riskkindconfig");
			temp.setParentcode("riskkindconfig");
			temp.setCodename((String) map.get("inskindcode"));
			INSCCode riskCode = inscCodeDao.selectOne(temp);
			if(riskCode != null){
				INSBSpecialkindconfig temp2 = new INSBSpecialkindconfig();
				temp2.setTaskid(processInstanceId);
				temp2.setInscomcode(inscomcode);
				temp2.setKindcode((String) map.get("inskindcode"));
				List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
				List<INSBSpecialkindconfig> selectList = insbSpecialkindconfigDao.selectList(temp2);
				for (INSBSpecialkindconfig insbSpecialkindconfig : selectList) {
					Map<String, Object> tempMap = new HashMap<String,Object>();
					tempMap.put("codekey", insbSpecialkindconfig.getCodekey());
					if("05".equals(riskCode.getCodevalue())){
						tempMap.put("codevalue", insbSpecialkindconfig.getCodevalue()+"/天");
					}else{
						tempMap.put("codevalue", insbSpecialkindconfig.getCodevalue());
					}
					tempList.add(tempMap);
				}
				map.put("specialType", riskCode.getCodevalue());
				map.put("specialKindList", tempList);
			}else{
				map.put("specialType", "00");
				map.put("specialKindList", new ArrayList<Map<String,Object>>());
			}
		}
		
		result.put("inscomcode", inscomcode);
		result.put("busTotalAmountprice", douFormat(busTotalAmountprice));//商业险保费
		result.put("discountBusTotalAmountprice", douFormat(discountBusTotalAmountprice));//折后商业险保费
		result.put("strTotalAmountprice", douFormat(strTotalAmountprice));//交强险保费
		result.put("discountStrTotalAmountprice", douFormat(discountStrTotalAmountprice));//折后交强险保费
		result.put("carTax", douFormat(carTax));//车船税
		result.put("discountCarTax", douFormat(discountCarTax));//折后车船税
		result.put("busQuoteInfoList", busQuoteInfoList);
		result.put("strQuoteInfoList", strQuoteInfoList);
		return result;
	}
	//截取小数点
	public double douFormat(Double target){
		DecimalFormat df = new DecimalFormat("#0.00");
		double tar = 0.0d;
		if(target!=null){
			tar = target;
		}
		return Double.parseDouble(df.format(tar));
	}
	/**
	 * 根据险种编码得到险别名称(获得所有投保信息接口使用)
	 * */
	public String getRiskkindName(String riskkindcode, List<INSBRiskkind> riskkindList){
		/*String riskkindName = "";
		for (int i = 0; i < riskkindList.size(); i++) {
			if(riskkindcode.equals(riskkindList.get(i).getKindcode())){
				riskkindName = riskkindList.get(i).getKindname();
				break;
			}
		}*/

		INSBRiskkindconfig insbRiskkindconfigs=new INSBRiskkindconfig();
		insbRiskkindconfigs.setRiskkindcode(riskkindcode);
		insbRiskkindconfigs=insbRiskkindconfigDao.selectOne(insbRiskkindconfigs);

		return insbRiskkindconfigs == null ? "" : insbRiskkindconfigs.getShortname();
	}

	/**
	 * 提交投保推送工作流接口
	 * */
	@Override
	public String policySubmit(String processInstanceId, String inscomcode, String agentnum, double totalproductamount, double totalpaymentamount) {
		LogUtil.info("提交核保开始taskid="+processInstanceId+"供应商id="+inscomcode+"代理人编码="+agentnum);
		// 判断是否过期报价周期和起保时间
		Date busStartDate = null, strStartDate = null;
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		for (int i = 0; i < policyitemList.size(); i++) {
			if("0".equals(policyitemList.get(i).getRisktype())){//商业险保单
				busStartDate = policyitemList.get(i).getStartdate();
			}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险保单
				strStartDate = policyitemList.get(i).getStartdate();
			}
		}

		// 判断是否有其他任务已经处于待支付，或者待核保状态了
		List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectSubModelByMainInstanceId(processInstanceId);
		for(INSBWorkflowsub sub:subs){
			if(TaskConst.VERIFYING_16.equals(sub.getTaskcode())||TaskConst.VERIFYING_17.equals(sub.getTaskcode())||TaskConst.VERIFYING_18.equals(sub.getTaskcode())
					||TaskConst.EDI_AUTO_INSURE.equals(sub.getTaskcode())||TaskConst.ELF_AUTO_INSURE.equals(sub.getTaskcode())
					||TaskConst.PAYING_20.equals(sub.getTaskcode())||TaskConst.PAYINGSUCCESS_SECOND_21.equals(sub.getTaskcode())){
				LogUtil.info(sub.getInstanceid()+"subinstanceid已存在提交核保的任务，不可以重复提交！"+sub.getTaskcode());
				return "已存在正在核保中的任务，不可以重复提交！";
			}
		}
		
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(processInstanceId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		quoteinfo.setInscomcode(inscomcode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		//获取子流程
//		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
//		workflowsub.setMaininstanceid(processInstanceId);
//		workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
//		workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
		//获取选择报价轨迹时间
		INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
		subtrack.setMaininstanceid(processInstanceId);
		subtrack.setInstanceid(quoteinfo.getWorkflowinstanceid());
		subtrack.setTaskcode("14");//选择投保
		subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//选择投保子流程轨迹信息

		//报价有效期
		INSBProvider atainteger = insbProviderDao.selectById(inscomcode);
		Date quotesuccessTimes = null;
		if(null == atainteger.getQuotationvalidity()){
			quotesuccessTimes = ModelUtil.gatFastPaydate(busStartDate, strStartDate);
		}else{
			quotesuccessTimes = ModelUtil.gatFastPaydateToNow(busStartDate, strStartDate, subtrack.getCreatetime(), atainteger.getQuotationvalidity());
		}
		Date date = new Date();
		LogUtil.info("报价有效期taskid"+processInstanceId+"到期时间为="+quotesuccessTimes+"供应商id="+inscomcode);

		if(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0){
			return "已超过报价有效期！";
		}else{
			// 报价时历史表数据回写
			// 车辆信息表数据查询
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(inscomcode);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			String carinfoid = carinfo.getId();
			String specifydriverid = carinfo.getSpecifydriver();//车辆信息表里指定驾驶人字段值
			if(carinfohis != null){
				specifydriverid = carinfohis.getSpecifydriver();
			}
			// 指定驾驶人中间表信息回写
//			INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis();
//			specifydriverhis.setTaskid(processInstanceId);
//			specifydriverhis.setInscomcode(inscomcode);
//			List<INSBSpecifydriverhis> sdriverhisList = insbSpecifydriverhisDao.selectList(specifydriverhis);
//			if(sdriverhisList!=null && sdriverhisList.size()>0){
//				INSBSpecifydriver specifydriver = new INSBSpecifydriver();
//				specifydriver.setTaskid(processInstanceId);
//				List<INSBSpecifydriver> sdriverList = insbSpecifydriverDao.selectList(specifydriver);
//				for (int i = 0; i < sdriverhisList.size(); i++) {
//					INSBSpecifydriverhis temphis = sdriverhisList.get(i);
//					String spid = "";//回写后的驾驶人中间表id
//					if(sdriverList.size() > i){//此判断保证能用取到驾驶人原表中i坐标的记录
//						INSBSpecifydriver temp = sdriverList.get(i);
//						spid = temp.getId();
//						temp = EntityTransformUtil.specifydriverhis2Specifydriver(temphis);
//						temp.setId(spid);
//						temp.setCarinfoid(carinfoid);
//						insbSpecifydriverDao.updateById(temp);
//					}else{
//						INSBSpecifydriver temp = EntityTransformUtil.specifydriverhis2Specifydriver(temphis);
//						temp.setCarinfoid(carinfoid);
//						insbSpecifydriverDao.insert(temp);
//						spid = temp.getId();
//					}
//					if(specifydriverid.equals(temphis.getId())){
//						specifydriverid = spid;//如果车辆信息中指定的驾驶人为驾驶人中间历史表的id，需要跟换为回写后驾驶人中间表的id
//					}
//				}
//			}
			// 车辆信息表数据回写
//			if(carinfohis != null){
//				carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
//				carinfo.setId(carinfoid);
//				carinfo.setSpecifydriver(specifydriverid);//把车辆信息中指定驾驶人字段更新为回写后驾驶人中间表的id
//				insbCarinfoDao.updateById(carinfo);
//			}
			// 车型信息数据回写
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
//			if(carmodelinfohis != null){
//				INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
//				carmodelinfo.setCarinfoid(carinfoid);
//				carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
//				String carmodelinfoid = carmodelinfo.getId();
//				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
//				carmodelinfo.setId(carmodelinfoid);
//				insbCarmodelinfoDao.updateById(carmodelinfo);
//			}
			// 被保人信息回写
			INSBInsuredhis insuredhis = new INSBInsuredhis();
			insuredhis.setTaskid(processInstanceId);
			insuredhis.setInscomcode(inscomcode);
			insuredhis = insbInsuredhisDao.selectOne(insuredhis);
//			if(insuredhis != null){
//				INSBInsured insured = new INSBInsured();
//				insured.setTaskid(processInstanceId);
//				insured = insbInsuredDao.selectOne(insured);
//				String insuredid = insured.getId();
//				insured = EntityTransformUtil.insuredhis2Insured(insuredhis);
//				insured.setId(insuredid);
//				insbInsuredDao.updateById(insured);
//			}
			// 投保人信息回写
			INSBApplicanthis applicanthis = new INSBApplicanthis();
			applicanthis.setTaskid(processInstanceId);
			applicanthis.setInscomcode(inscomcode);
			applicanthis = insbApplicanthisDao.selectOne(applicanthis);
//			if(applicanthis != null){
//				INSBApplicant applicant = new INSBApplicant();
//				applicant.setTaskid(processInstanceId);
//				applicant = insbApplicantDao.selectOne(applicant);
//				String applicantid = applicant.getId();
//				applicant = EntityTransformUtil.applicanthis2Applicant(applicanthis);
//				applicant.setId(applicantid);
//				insbApplicantDao.updateById(applicant);
//			}
			// 权益索赔人信息回写
			INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
			legalrightclaimhis.setTaskid(processInstanceId);
			legalrightclaimhis.setInscomcode(inscomcode);
			legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
//			if(legalrightclaimhis != null){
//				INSBLegalrightclaim legalrightclaim = new INSBLegalrightclaim();
//				legalrightclaim.setTaskid(processInstanceId);
//				legalrightclaim = insbLegalrightclaimDao.selectOne(legalrightclaim);
//				String legalrightclaimid = legalrightclaim.getId();
//				legalrightclaim = EntityTransformUtil.legalrightclaimhis2Legalrightclaim(legalrightclaimhis);
//				legalrightclaim.setId(legalrightclaimid);
//				insbLegalrightclaimDao.updateById(legalrightclaim);
//			}
			// 联系人信息回写
			INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
			relationpersonhis.setTaskid(processInstanceId);
			relationpersonhis.setInscomcode(inscomcode);
			relationpersonhis = insbRelationpersonhisDao.selectOne(relationpersonhis);
//			if(relationpersonhis != null){
//				INSBRelationperson relationperson = new INSBRelationperson();
//				relationperson.setTaskid(processInstanceId);
//				relationperson = insbRelationpersonDao.selectOne(relationperson);
//				String relationpersonid = relationperson.getId();
//				relationperson = EntityTransformUtil.relationpersonhis2Relationperson(relationpersonhis);
//				relationperson.setId(relationpersonid);
//				insbRelationpersonDao.updateById(relationperson);
//			}

			// 生成订单
			INSBAgent agent = insbAgentDao.selectByJobnum(agentnum);
			INSBProvider provider = new INSBProvider();
			provider.setPrvcode(inscomcode);
			provider = insbProviderDao.selectOne(provider);
			//根据协议查询出单网点机构编码
//			INSBAgreement agreement = new INSBAgreement();
//			agreement.setProviderid(inscomcode);
//			agreement = insbAgreementDao.selectOne(agreement);
//			INSCDept dept = inscDeptDao.selectById(agreement.getDeptid());
			LogUtil.info("提交核保插入订单数据开始taskid="+processInstanceId+"供应商id="+inscomcode+"操作人="+agent.getName());
			//订单表插入数据
			INSBOrder insbOrder = new INSBOrder();
			insbOrder.setTaskid(processInstanceId);
			insbOrder.setPrvid(inscomcode);
			INSBOrder order = insbOrderDao.selectOne(insbOrder);
			if(null == order){
				order = new INSBOrder();
				order.setOperator(agent.getName());//操作员
				order.setCreatetime(new Date());//创建时间
				order.setTaskid(processInstanceId);//流程实例id
//				UUID uuid = UUID.randomUUID();
//				order.setOrderno(uuid.toString());//订单号
				order.setOrderno(GenerateSequenceUtil.generateSequenceNo(processInstanceId,quoteinfo.getWorkflowinstanceid()));//订单号
//				order.setOrderstatus("1");//订单状态todo
				order.setOrderstatus("0");//0-待投保
				order.setPaymentstatus("0");//支付状态todo
				order.setDeliverystatus("0");//配送状态todo
				order.setBuyway(quoteinfo.getBuybusitype());//销售渠道(01传统02网销03电销)todo
				order.setAgentcode(agentnum);//代理人编码
				order.setAgentname(agent.getName());//代理人姓名
				order.setInputusercode(agent.getAgentcode());//录单人
				Map<String, Double> premiumInfo = this.getAllPremium(processInstanceId, inscomcode);
				order.setTotalproductamount(premiumInfo.get("premium"));//订单标价总金额
				order.setTotalpaymentamount(premiumInfo.get("discountPremium"));//实付金额
				if(premiumInfo.get("premium").compareTo(premiumInfo.get("discountPremium")) > 0){
					order.setTotalpromotionamount(premiumInfo.get("premium")-premiumInfo.get("discountPremium"));//优惠金额
				}
//				order.setDeptcode(dept.getComcode());//出单网点
				order.setDeptcode(quoteinfo.getDeptcode());//出单网点
				order.setCurrency("RMB");
				order.setPrvid(inscomcode);
				insbOrderDao.insert(order);
			}else{
				order.setOperator(agent.getName());//操作员
				order.setModifytime(new Date());//创建时间
				order.setTaskid(processInstanceId);//流程实例id
//				UUID uuid = UUID.randomUUID();
//				order.setOrderno(uuid.toString());//订单号
//				order.setOrderno(GenerateSequenceUtil.generateSequenceNo());//订单号
//				order.setOrderstatus("1");//订单状态todo
//				order.setOrderstatus("0");//0-待投保
//				order.setPaymentstatus("0");//支付状态todo
//				order.setDeliverystatus("0");//配送状态todo
//				order.setBuyway(quoteinfo.getBuybusitype());//销售渠道(01传统02网销03电销)todo
//				order.setAgentcode(agentnum);//代理人编码
//				order.setAgentname(agent.getName());//代理人姓名
//				order.setInputusercode(agent.getAgentcode());//录单人
				Map<String, Double> premiumInfo = this.getAllPremium(processInstanceId, inscomcode);
				order.setTotalproductamount(premiumInfo.get("premium"));//订单标价总金额
				order.setTotalpaymentamount(premiumInfo.get("discountPremium"));//实付金额
				if(premiumInfo.get("premium").compareTo(premiumInfo.get("discountPremium")) > 0){
					order.setTotalpromotionamount(premiumInfo.get("premium")-premiumInfo.get("discountPremium"));//优惠金额
				}
//				order.setDeptcode(dept.getComcode());//出单网点
				order.setDeptcode(quoteinfo.getDeptcode());//出单网点
				order.setCurrency("RMB");
				order.setPrvid(inscomcode);
				insbOrderDao.updateById(order);
			}
			LogUtil.info("INSBOrder|报表数据埋点|"+JSONObject.fromObject(order).toString());
			LogUtil.info("提交核保插入订单数据结束taskid="+processInstanceId+"供应商id="+inscomcode+"订单id="+order.getId()+"操作人="+agent.getName());

//			判断是懒掌柜调用的接口 TODO
			/*20170619 hwc 去除旧版懒掌柜相关代码
			Map<String, String> param = new HashMap<String, String>();
			param.put("taskid", processInstanceId);
			LogUtil.info("==调用懒掌柜订单保存接口--taskid>>"+processInstanceId);
			param.put("taskcode", "1");
			param.put("operationtype", "1");
			INSBOrderlistenpush o = insbOrderlistenpushService.queryOrderListen(param);//order by createtime
			if(o!=null&&!"".equals(o.getLzgid())){
				LogUtil.info("==懒掌柜订单接口lagid-->>"+o.getLzgid());
				lzgOrderService.addOrderData(o,inscomcode,agentnum);
			}*/

			INSBPolicyitem policyBus = null;//商业险保单接收变量
			INSBPolicyitem policyStr = null;//交强险保单接收变量
			// 更新保单表中的orderid字段
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){
					policyBus = policyitemList.get(i);
				}else if("1".equals(policyitemList.get(i).getRisktype())){
					policyStr = policyitemList.get(i);
				}
//				if(policyitemList.get(i).getOrderid()==null || "".equals(policyitemList.get(i).getOrderid())){
//					insbPolicyitemDao.deleteById(policyitemList.get(i).getId());
//				}
			}
			INSBPerson tempPerson = null;
			if(applicanthis!=null){
				tempPerson = insbPersonDao.selectById(applicanthis.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setApplicantname(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setApplicantname(tempPerson.getName());
					}
				}
			}
			if(insuredhis!=null){
				tempPerson = insbPersonDao.selectById(insuredhis.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setInsuredname(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setInsuredname(tempPerson.getName());
					}
				}
			}
			INSBCarowneinfo ownerinfo = insbCarowneinfoDao.selectByTaskId(processInstanceId);
			if(ownerinfo!=null){
				tempPerson = insbPersonDao.selectById(ownerinfo.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setCarownername(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setCarownername(tempPerson.getName());
					}
				}
			}
			if(carmodelinfohis!=null){
				if(policyBus!=null){
					policyBus.setStandardfullname(carmodelinfohis.getStandardfullname());
				}
				if(policyStr!=null){
					policyStr.setStandardfullname(carmodelinfohis.getStandardfullname());
				}
			}
			if(policyBus!=null){
//				policyBus.setId(null);
//				policyBus.setModifytime(null);
				policyBus.setModifytime(date);
//				policyBus.setCreatetime(date);
//				policyBus.setInscomcode(inscomcode);
				policyBus.setOrderid(order.getId());
				//不确定置空的字段
//				policyBus.setTotalepremium(null);//
//				policyBus.setPremium(null);//
//				policyBus.setDiscountCharge(null);//折后保费
//				policyBus.setDiscountRate(null);//折扣率
//				policyBus.setAmount(null);//保额
//				policyBus.setProposalformno(null);//投保单号
//				policyBus.setPolicyno(null);//保单号
//				policyBus.setPolicystatus(null);//保单状态
//				policyBus.setPaynum(null);//支付号
//				policyBus.setCheckcode(null);//校验码
//				insbPolicyitemDao.insert(policyBus);
				insbPolicyitemDao.updateById(policyBus);
			}
			if(policyStr!=null){
//				policyStr.setId(null);
//				policyStr.setModifytime(null);
				policyStr.setModifytime(date);
//				policyStr.setCreatetime(date);
//				policyStr.setInscomcode(inscomcode);
				policyStr.setOrderid(order.getId());
				//不确定置空的字段
//				policyStr.setTotalepremium(null);//
//				policyStr.setPremium(null);//
//				policyStr.setDiscountCharge(null);//折后保费
//				policyStr.setDiscountRate(null);//折扣率
//				policyStr.setAmount(null);//保额
//				policyStr.setProposalformno(null);//投保单号
//				policyStr.setPolicyno(null);//保单号
//				policyStr.setPolicystatus(null);//保单状态
//				policyStr.setPaynum(null);//支付号
//				policyStr.setCheckcode(null);//校验码
//				insbPolicyitemDao.insert(policyStr);
				insbPolicyitemDao.updateById(policyStr);
			}

			// 提交投保推送工作流调用接口
			Map<String, String> params = new HashMap<String, String>();//接口调用参数
			//通过报价信息表得到报价子流程id
			//添加用户备注
//			INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
//			subtrack.setInstanceid(workflowsub.getInstanceid());
//			subtrack.setTaskcode(workflowsub.getTaskcode());
//			subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//子流程轨迹信息
//			INSBUsercomment userComment = new INSBUsercomment();
//			userComment.setCommentcontent(notice);//备注信息
//			userComment.setTrackid(subtrack.getId());//任务轨迹id
//			userComment.setTracktype(1);//轨迹类型0
//			userComment.setCommentcontenttype(1);//备注内容类型
//			userComment.setCommenttype(1);//备注类型
//			userComment.setCreatetime(new Date());
//			insbUsercommentDao.insert(userComment);//添加用户备注信息
			//通过报价子流程id调用接口获取userId和taskId(当前节点id)
			INSBWorkflowsub mainModel = insbWorkflowsubDao.selectByInstanceId(quoteinfo.getWorkflowinstanceid());
			Map<String, Object> datas = new HashMap<String, Object>();//接口调用参数
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("result", "3");//任务结果：1获取报价途径，2报价退回，3选择保价，4终止报价
			data.put("underwriteway",JSONObject.fromObject(workflowmainService.getContracthbType(mainModel.getMaininstanceid(), inscomcode, "", "underwriting")).optString("quotecode"));//获取核保途径
			datas.put("data", data);//数据
			datas.put("taskName", "选择投保");
			datas.put("userid", mainModel.getOperator());//权限用户用户id
			datas.put("incoid", inscomcode);//保险公司code
			datas.put("processinstanceid", quoteinfo.getWorkflowinstanceid());//流程实例id
			params.put("datas", JSONObject.fromObject(datas).toString());

			WorkflowFeedbackUtil.setWorkflowFeedback(processInstanceId, quoteinfo.getWorkflowinstanceid(), "14", "Completed", "选择投保", "提交核保", "admin");
			
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					String doGetResult = HttpClientUtil.doGet(WORKFLOWURL+"/process/completeSubTask", params);//选择保价接口调用
					LogUtil.info("提交核保调用工作流结束taskid="+processInstanceId+"供应商id="+inscomcode+"操作人="+agent.getName()+"返回结果="+doGetResult);
					//得到请求结果json
					//if(doGetResult == null || "fail".equals(JSONObject.fromObject(doGetResult).getString("message"))){//如果推送工作流失败，则抛出异常
					//throw new RuntimeException("工作流提交选择投保失败！");
					//}
				}
			});
            
		}
		return "success";
	}

	/**
	 * 查询保单列表信息接口
	 * */
	@Override
	public String getPolicyitemList(String agentnum, String carlicenseno,
			String insuredname,String queryinfo, Integer limit, Long offset, Integer idcardtype, String idcardno,String code) {
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			// 组织查询参数
			Map<String, Object> queryParams = new HashMap<String, Object>();
			if(queryinfo!=null&&!("".equals(queryinfo))){
				queryParams.put("queryinfo", queryinfo);
			}
			if(agentnum!=null&&!("".equals(agentnum))){
				queryParams.put("agentnum", agentnum);
			}
			if(carlicenseno!=null&&!("".equals(carlicenseno))){
				queryParams.put("carlicenseno", carlicenseno);
			}
			if(insuredname!=null&&!("".equals(insuredname))){
				queryParams.put("insuredname", insuredname);
			}
			if(limit!=null&&!("".equals(limit))){
				queryParams.put("limit", limit);
			}
			if(offset!=null&&!("".equals(offset))){
				queryParams.put("offset", offset);
			}
			if(idcardtype!=null&&!("".equals(idcardtype))){
				queryParams.put("idcardtype", idcardtype);
			}
			if(idcardno!=null&&!("".equals(idcardno))){
				queryParams.put("idcardno", idcardno);
			}
			//组织响应json数据
			//查询保单列表
//			System.out.println(queryParams.toString());
			List<Map<String,Object>> policyitemList = new ArrayList<Map<String,Object>>();
			if("cm".equals(code)){
				Integer limitTemp =(Integer) queryParams.get("limit");
				Long offsetTemp =(Long) queryParams.get("offset");
				queryParams.put("offset", (offsetTemp-1)*limitTemp);
				policyitemList = insbPolicyitemDao.getPolicyitemListByAgentnum(queryParams);//保单列表
				long totals = insbPolicyitemDao.getPolicyitemListTotalsByAgentnum(queryParams);
				if(offset*limit>=totals){
					code = "cif";
				}
			}else{
				List<Map<String, Object>> tempList = getPolicyitemFromCif(agentnum, queryinfo, limit, offset);
				if(tempList!=null){
					policyitemList = tempList;
				}
			}
			body.put("policyitemList", policyitemList);
			body.put("code", code);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询保单列表信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询保单列表信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	private List<Map<String,Object>> getPolicyitemFromCif(String agentnum, String queryinfo, Integer limit, Long offset) {
		List<Map<String, Object>> queryTHisYeardetail = null;
		//组织入参
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("currentPage", offset);
		map.put("jobNum", agentnum);
		map.put("pageSize", limit);
		map.put("carlicenseno", queryinfo);
		map.put("policyno", null);
		queryTHisYeardetail = queryTHisYeardetail(map);
		if(queryTHisYeardetail == null || queryTHisYeardetail.size()<0){
			map.put("carlicenseno", null);
			map.put("policyno",queryinfo);
			queryTHisYeardetail = queryTHisYeardetail(map);
		}
		return queryTHisYeardetail;
	}

	private List<Map<String,Object>> queryTHisYeardetail(Map<String, Object> map) {
		List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		//调用接口
		String resultJson = CloudQueryUtil.queryAgentPolicydetail(JSONObject.fromObject(map).toString());
		//组织回参
		if(resultJson!=null){
			JSONObject jsonObject = JSONObject.fromObject(resultJson);
			if("success".equals((String)jsonObject.get("status"))){
				List<Map<String,String>> list = (List<Map<String,String>>)jsonObject.get("body");
				for (Map<String, String> temp : list) {
					Map<String, Object> tempMap = new HashMap<String,Object>();
					tempMap.put("inscomcode", "");
					tempMap.put("platformCode", "cif");
					tempMap.put("processInstanceId", "");
					tempMap.put("insuredname", temp.get("insured"));
					tempMap.put("carlicenseno", temp.get("carlicenseno"));
					if("交强险".equals(temp.get("insuranceType"))){

							tempMap.put("strenddate", ModelUtil.conbertToString(ModelUtil.conbertStringToNyrDate(temp.get("endDate"))));
							tempMap.put("strstartdate", ModelUtil.conbertToString(ModelUtil.conbertStringToNyrDate(temp.get("startDate"))));

					}else{

							tempMap.put("busenddate", ModelUtil.conbertToString(ModelUtil.conbertStringToNyrDate(temp.get("endDate"))));
							tempMap.put("busstartdate", ModelUtil.conbertToString(ModelUtil.conbertStringToNyrDate(temp.get("startDate"))));

					}
					tempList.add(tempMap);
				}
				return tempList;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 查询保单列表信息接口最新
	 * @param agentnum 代理人工号
	 * @param queryinfo 模糊查询信息
	 * @param pageSize 每页条数
	 * @param currentPage 当前页码
	 * @return
	 */
	@Override
	public String getPolicyitemList02(String agentnum, String queryinfo, String querytype, Integer pageSize, Integer currentPage) {
		CommonModel result = new CommonModel();
		try {
			//调用平台保单列表查询接口
			String re = "";
			if( "cm".equals(querytype) ) {
//				re = this.getPolicyitemList(null, null, null, queryinfo, pageSize, new Long(currentPage), null, null, "cm");
				// 组织查询参数
				Map<String, Object> queryParams = new HashMap<String, Object>();
				if(queryinfo!=null&&!("".equals(queryinfo))){
					queryParams.put("queryinfo", queryinfo);
				}
				if(agentnum!=null&&!("".equals(agentnum))){
					queryParams.put("agentnum", agentnum);
				}else{
					//hxx 20160804 没有代理人数据不进行查询
					result.setStatus("fail");
					result.setMessage("查询保单列表信息失败！");
					JSONObject jsonObject = JSONObject.fromObject(result);
					return jsonObject.toString();
				}
				if(pageSize!=null){
					queryParams.put("limit", pageSize);
				}
				if(currentPage!=null){
					queryParams.put("offset", currentPage);
				}
				//组织响应json数据
				//查询保单列表
				List<Map<String,Object>> policyitemList = new ArrayList<Map<String,Object>>();
				Integer limitTemp =(Integer) queryParams.get("limit");
				Integer offsetTemp =(Integer) queryParams.get("offset");
				queryParams.put("offset", (offsetTemp-1)*limitTemp);
				policyitemList = insbPolicyitemDao.getPolicyitemList(queryParams);//保单列表
//				long totals = insbPolicyitemDao.getPolicyitemListTotalsByAgentnum(queryParams);
				result.setBody(policyitemList);
				result.setStatus("success");
				result.setMessage("操作已成功");
				JSONObject jsonObject = JSONObject.fromObject(result);
				re = jsonObject.toString();
			} else {
				Map<String,Object> paramsMap = new HashMap<String,Object>();
				paramsMap.put("currentPage", currentPage);
				paramsMap.put("jobNum", agentnum);
				paramsMap.put("pageSize", pageSize);
				paramsMap.put("carlicenseno", queryinfo);//模糊查询条件，车牌号或被保人姓名
				paramsMap.put("outCM", "1");
				re = CloudQueryUtil.queryNotEnddateDetail(JSONObject.fromObject(paramsMap).toString());
			}
			return re;
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询保单列表信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}


	/**
	 * 查询保单列表信息接口最新
	 * @param channeluserid 渠道用户uuid
	 * @param queryinfo 模糊查询信息
	 * @param pageSize 每页条数
	 * @param currentPage 当前页码
	 * @return
	 */
	@Override
	public String getPolicyitemList02ForMinizzb(String channeluserid, String queryinfo, String querytype, Integer pageSize, Integer currentPage) {
		CommonModel result = new CommonModel();
		try {
			//调用平台保单列表查询接口
			String re = "";
			querytype = "cm"; //现不支持云查询，若要支持，则要北京云查询提供支持使用渠道用户UUID
			if( "cm".equals(querytype) ) {
//				re = this.getPolicyitemList(null, null, null, queryinfo, pageSize, new Long(currentPage), null, null, "cm");
				// 组织查询参数
				Map<String, Object> queryParams = new HashMap<String, Object>();
				if(queryinfo!=null&&!("".equals(queryinfo))){
					queryParams.put("queryinfo", queryinfo);
				}
//				if(agentnum!=null&&!("".equals(agentnum))){
//					queryParams.put("agentnum", agentnum);
//				}
				if(channeluserid!=null&&!("".equals(channeluserid))){
					queryParams.put("agentid", channeluserid);
				}
				if(pageSize!=null){
					queryParams.put("limit", pageSize);
				}
				if(currentPage!=null){
					queryParams.put("offset", currentPage);
				}
				//组织响应json数据
				//查询保单列表
				List<Map<String,Object>> policyitemList = new ArrayList<Map<String,Object>>();
				Integer limitTemp =(Integer) queryParams.get("limit");
				Integer offsetTemp =(Integer) queryParams.get("offset");
				queryParams.put("offset", (offsetTemp-1)*limitTemp);
//				policyitemList = insbPolicyitemDao.getPolicyitemList(queryParams);//保单列表
				policyitemList = insbPolicyitemDao.getPolicyitemListForMinizzb(queryParams);//保单列表
//				//long totals = insbPolicyitemDao.getPolicyitemListTotalsByAgentnum(queryParams);
				result.setBody(policyitemList);
				result.setStatus("success");
				result.setMessage("操作已成功");
				JSONObject jsonObject = JSONObject.fromObject(result);
				re = jsonObject.toString();
			} else {
				Map<String,Object> paramsMap = new HashMap<String,Object>();
				paramsMap.put("currentPage", currentPage);
//				paramsMap.put("jobNum", agentnum);
				paramsMap.put("agentid", channeluserid);
				paramsMap.put("pageSize", pageSize);
				paramsMap.put("carlicenseno", queryinfo);//模糊查询条件，车牌号或被保人姓名
				paramsMap.put("outCM", "1");
				re = CloudQueryUtil.queryNotEnddateDetail(JSONObject.fromObject(paramsMap).toString());
			}
			return re;
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询保单列表信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	/**
	 * 查询保单详细信息接口最新
	 * @param policyno 保单号
	 * @param queryflag 查询标记位（cm、cf）
	 * @param policytype 1：交强  0：商业
	 * @return json数据
	 */
	@Override
	public String getPolicyitemDetailInfo02(String policyno, String queryflag, String policytype) {
		CommonModel result = new CommonModel();
		try {
			Map<String, Object> body = new HashMap<String, Object>();
			Map<String, Object> baseInfo = new HashMap<String, Object>();
			//根据标记，通过保单号查询保单详细信息
			if("cm".equals(queryflag)){
				//查询保单基本信息
				INSBPolicyitem policyitem = new INSBPolicyitem();
				policyitem.setPolicyno(policyno);
				if( policytype != null && !"".equals(policytype) ) {
					policyitem.setRisktype(policytype);
				}
				List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(policyitem);
				if(policyitems != null && policyitems.size() == 1){
                    policyitem = policyitems.get(0);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					if("0".equals(policyitem.getRisktype())){//商业险
						baseInfo.put("risktype", "商业险");
					}else if("1".equals(policyitem.getRisktype())){//交强险
						baseInfo.put("risktype", "交强险");
					}
					baseInfo.put("policyno", policyitem.getPolicyno());//保单号
					if(policyitem.getStartdate()!=null){
						baseInfo.put("startdate", sdf.format(policyitem.getStartdate()));//商业险生效起始日期
					}
					if(policyitem.getEnddate()!=null){
						baseInfo.put("enddate", sdf.format(policyitem.getEnddate()));//商业险生效截止日期
					}

                    Map<String, Object> insured = new HashMap<>();

                    String processInstanceId = policyitem.getTaskid();
					String prvid = policyitem.getInscomcode();
					INSBOrder queryInsbOrder = new INSBOrder();
					queryInsbOrder.setTaskid(processInstanceId);
					queryInsbOrder.setPrvid(prvid);
					INSBOrder order = insbOrderDao.selectOne(queryInsbOrder);
					if(order!=null){
						INSBOrderpayment orderpayment = new INSBOrderpayment();
						orderpayment.setTaskid(processInstanceId);
						orderpayment.setOrderid(order.getId());
						orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
						if(orderpayment!=null){
							if(orderpayment.getPaychannelid()!=null){
								INSBPaychannel paychannel = insbPaychannelDao.selectById(orderpayment.getPaychannelid());
								if(paychannel!=null){
									baseInfo.put("paymentmethod", paychannel.getPaychannelname());//支付方式
								}else{
									baseInfo.put("paymentmethod", "未知支付方式");//支付方式
								}
							}else{
								baseInfo.put("paymentmethod", "未知支付方式");//支付方式
							}
						}
						Map<String, String> params = new HashMap<String, String>();
						params.put("mainInstanceId", processInstanceId);
						params.put("orderId", order.getId());
						baseInfo.put("deliveryAddress", insbOrderdeliveryDao.getOrderdeliveryAddress(params));//配送地址
                        insured = getInsuredInfoMap(processInstanceId, order.getPrvid());//被保人信息
					}
					baseInfo.put("taskId", processInstanceId);//返回前端任务号去取电子保单
                    baseInfo.put("insuredName", insured.get("name"));//被保人姓名
					INSBProvider provider = insbProviderDao.selectById(prvid);
					if(provider!=null){
						baseInfo.put("prvname", provider.getPrvshotname());//投保公司名称
					}

					INSCDept dept = inscDeptDao.selectByComcode(order.getDeptcode());
					if(dept!=null){
						baseInfo.put("deptAddress", dept.getAddress());//出单地址
					}

					//查询车辆信息
                    Map<String, Object> carInfo = new HashMap<String, Object>();
                    INSBCarmodelinfo carmodelinfo = null;
					INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);

                    if (carinfo != null) {
                        carmodelinfo = new INSBCarmodelinfo();
                        carmodelinfo.setCarinfoid(carinfo.getId());
                        carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);

                        INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
                        carmodelinfohis.setCarinfoid(carinfo.getId());
                        carmodelinfohis.setInscomcode(prvid);
                        carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);

                        if (carmodelinfohis != null) {
                            carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
                        }
                    }

                    INSBCarinfohis carinfohis = new INSBCarinfohis();
                    carinfohis.setTaskid(processInstanceId);
                    carinfohis.setInscomcode(prvid);
                    carinfohis = insbCarinfohisDao.selectCarinfohis(carinfohis);

					if(carinfohis!=null){
						carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
					}

                    if (carinfo != null) {
                        carInfo.put("carlicenseno", carinfo.getCarlicenseno());//车牌号
                        carInfo.put("ownername", carinfo.getOwnername());//车主姓名
                        carInfo.put("engineno", carinfo.getEngineno());//发动机号
                        carInfo.put("vincode", carinfo.getVincode());//车辆识别码
                        carInfo.put("registdate", sdf.format(carinfo.getRegistdate()));//车辆初登日期
                        carInfo.put("property", carinfo.getProperty());//所属性质
                        carInfo.put("carproperty", carinfo.getCarproperty());//车辆性质
                    }
                    if (carmodelinfo != null) {
                        carInfo.put("brandname", carmodelinfo.getBrandname());//车辆品牌
                        carInfo.put("standardname", carmodelinfo.getStandardname());//车辆型号
                        carInfo.put("displacement", carmodelinfo.getDisplacement());//排气量
                    }

					body.put("carInfo", carInfo);

					//查询保险配置报价信息
					Map<String, Object> insureInfo = new HashMap<String, Object>();
					Map<String, Object> carkindpriceInfo = getCarkindpriceInfo(processInstanceId, order.getPrvid());
					if("0".equals(policyitem.getRisktype())){//商业险
						baseInfo.put("totalepremium", carkindpriceInfo.get("discountBusTotalAmountprice"));
						insureInfo.put("quotePriceInfoList", carkindpriceInfo.get("busQuoteInfoList"));
					}else if("1".equals(policyitem.getRisktype())){//交强险
						baseInfo.put("totalepremium", (double) carkindpriceInfo.get("discountStrTotalAmountprice")+
								(double) carkindpriceInfo.get("discountCarTax"));
						insureInfo.put("quotePriceInfoList", carkindpriceInfo.get("strQuoteInfoList"));
					}
					body.put("insureInfo", insureInfo);
					body.put("baseInfo", baseInfo);

					//查询关系人信息
					Map<String, Object> customInfo = new HashMap<String, Object>();
					customInfo.put("insured", insured);
					Map<String, Object> applicant = getApplicantInfoMap02(processInstanceId, order.getPrvid());//投保人信息
					customInfo.put("applicant", applicant);
					Map<String, Object> legalrightclaim = getLegalrightclaimInfoMap02(processInstanceId, order.getPrvid());//权益索赔人信息
					customInfo.put("legalrightclaim", legalrightclaim);
					Map<String, Object> carownerinfo = getCarOwnerInfoMap(processInstanceId, order.getPrvid());//车主信息
					customInfo.put("carownerinfo", carownerinfo);
					body.put("customInfo", customInfo);
					result.setBody(body);
					result.setStatus("success");
					result.setMessage("查询保单详细信息成功！");
					return JSONObject.fromObject(result).toString();
				}else{
					String taskId = "";
					if(policyitems.size()>=2) {
						for (INSBPolicyitem insbPolicyitem1 : policyitems) {//获取主任务号
							taskId = insbPolicyitem1.getTaskid();
							break;
						}
					}
//					result.setStatus("fail");
//					result.setMessage("没有此保单号对应的保单或保单号重复！");
//					return JSONObject.fromObject(result).toString();
					LogUtil.info("policyno："+ policyno +" cm 保单数据不存在，请求大数据查询" );
					//调用cf端接口返回保单详情数据
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("policyno", policyno);
					paramsMap.put("taskid", taskId);
					if( policytype != null && !"".equals(policytype) ) {
						paramsMap.put("policytype", policytype);
					}
//					String test = processInstanceId;
					return CloudQueryUtil.queryPolicyDetail(JSONObject.fromObject(paramsMap).toString());

				}
			}else if("zzb".equals(queryflag) || "HX".equals(queryflag) || "jl".equals(queryflag)){//老掌中宝和核心的数据从平台查询
				//调用cf端接口返回保单详情数据
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("policyno", policyno);
				if( policytype != null && !"".equals(policytype) ) {
					paramsMap.put("policytype", policytype);
				}
				return CloudQueryUtil.queryPolicyDetail(JSONObject.fromObject(paramsMap).toString());
			}else{
				result.setStatus("fail");
				result.setMessage("无效查询标记，不能查询保单详情！");
				return JSONObject.fromObject(result).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询保单详细信息失败！");
			return JSONObject.fromObject(result).toString();
		}
	}
	
	/**
	 * 查询保单详细信息接口
	 * */
	@Override
	public String getPolicyitemDetailInfo(String processInstanceId,String prvid) {
		CommonModel result = new CommonModel();
		try {
			Map<String, Object> body = new HashMap<String, Object>();
			Map<String, Object> baseInfo = new HashMap<String, Object>();
			//组织响应json数据
			//查询保单基本信息
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setTaskid(processInstanceId);
			policyitem.setInscomcode(prvid);
			List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(policyitemList!=null && policyitemList.size()>0){
				String risktype = "";
				for (int i = 0; i < policyitemList.size(); i++) {
					if(policyitemList.get(i)!=null){
						policyitem = policyitemList.get(i);
						if("0".equals(policyitem.getRisktype())){//商业险
							risktype += "、商业险";//保险名称,0-商业险、1-交强险
							baseInfo.put("buspolicyno", policyitem.getPolicyno());//商业险保单号
							if(policyitem.getStartdate()!=null){
								baseInfo.put("busstartdate", sdf.format(policyitem.getStartdate()).toString());//商业险生效起始日期
							}
							if(policyitem.getEnddate()!=null){
								baseInfo.put("busenddate", sdf.format(policyitem.getEnddate()).toString());//商业险生效截止日期
							}
						}else if("1".equals(policyitem.getRisktype())){//交强险
							risktype += "、交强险";
							baseInfo.put("strpolicyno", policyitem.getPolicyno());//交强险保单号
							if(policyitem.getStartdate()!=null){
								baseInfo.put("strstartdate", sdf.format(policyitem.getStartdate()).toString());//交强险生效起始日期
							}
							if(policyitem.getEnddate()!=null){
								baseInfo.put("strenddate", sdf.format(policyitem.getEnddate()).toString());//交强险生效截止日期
							}
						}
					}
				}
				if(risktype.length()>0){
					baseInfo.put("risktype", risktype.substring(1));
				}
			}
			INSBOrder queryInsbOrder = new INSBOrder();
			queryInsbOrder.setTaskid(processInstanceId);
			queryInsbOrder.setPrvid(prvid);
			INSBOrder order = insbOrderDao.selectOne(queryInsbOrder);
			if(order!=null){
//				baseInfo.put("paymentmethod", order.getPaymentmethod());//支付方式
				INSBOrderpayment orderpayment = new INSBOrderpayment();
				orderpayment.setTaskid(processInstanceId);
				orderpayment.setOrderid(order.getId());
				orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
				if(orderpayment!=null){
					if(orderpayment.getPaychannelid()!=null){
						INSBPaychannel paychannel = insbPaychannelDao.selectById(orderpayment.getPaychannelid());
						if(paychannel!=null){
							baseInfo.put("paymentmethod", paychannel.getPaychannelname());//支付方式
						}else{
							baseInfo.put("paymentmethod", "未知支付方式");//支付方式
						}
					}else{
						baseInfo.put("paymentmethod", "未知支付方式");//支付方式
					}
				}
			}
			INSBProvider provider = insbProviderDao.selectById(prvid);
			if(provider!=null){
				baseInfo.put("prvname", provider.getPrvshotname());//投保公司名称
			}
			Map<String, Object> insured = getInsuredInfoMap(processInstanceId, order.getPrvid());//被保人信息
			baseInfo.put("insuredName", insured.get("name"));//被保人姓名
			INSCDept dept = inscDeptDao.selectByComcode(order.getDeptcode());
			if(dept!=null){
				baseInfo.put("deptAddress", dept.getAddress());//出单地址
				baseInfo.put("deptName", dept.getComname());//出单网点全称
			}
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(processInstanceId);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if(quotetotalinfo!=null){
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				quoteinfo.setInscomcode(order.getPrvid());
				quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
				if(quoteinfo!=null){
					baseInfo.put("totalepremium", quoteinfo.getQuotediscountamount());//保费合计
				}
			}
			body.put("baseInfo", baseInfo);
			//查询车辆信息
			INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
			carmodelinfo.setCarinfoid(carinfo.getId());
			carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(prvid);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfo.getId());
			carmodelinfohis.setInscomcode(prvid);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carinfohis!=null){
				carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
			}
			if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
			Map<String, Object> carInfo = new HashMap<String, Object>();
			carInfo.put("carlicenseno", carinfo.getCarlicenseno());//车牌号
			carInfo.put("brandname", carmodelinfo.getBrandname());//车辆品牌
			carInfo.put("standardname", carmodelinfo.getStandardname());//车辆型号
			carInfo.put("ownername", carinfo.getOwnername());//车主姓名
			carInfo.put("engineno", carinfo.getEngineno());//发动机号
			carInfo.put("vincode", carinfo.getVincode());//车辆识别码
			carInfo.put("registdate", sdf.format(carinfo.getRegistdate()).toString());//车辆初登日期
			body.put("carInfo", carInfo);
			//查询保险配置报价信息
			Map<String, Object> insureInfo = new HashMap<String, Object>();
			Map<String, Object> carkindpriceInfo = getCarkindpriceInfo(processInstanceId, order.getPrvid());
			insureInfo.put("busquotePriceInfoList", carkindpriceInfo.get("busQuoteInfoList"));
			insureInfo.put("busquoteTotalPrice", carkindpriceInfo.get("busTotalAmountprice"));//商业险保费总额
			insureInfo.put("busdiscountQuoteTotalPrice", carkindpriceInfo.get("discountBusTotalAmountprice"));//折后商业险保费总额
			insureInfo.put("strquotePriceInfoList", carkindpriceInfo.get("strQuoteInfoList"));
			insureInfo.put("strquoteTotalPrice", (double) carkindpriceInfo.get("strTotalAmountprice")+
					(double) carkindpriceInfo.get("carTax"));//交强险保费总额
			insureInfo.put("strdiscountQuoteTotalPrice", (double) carkindpriceInfo.get("discountStrTotalAmountprice")+
					(double) carkindpriceInfo.get("discountCarTax"));//折后交强险保费总额
			body.put("insureInfo", insureInfo);
			//查询关系人信息
			Map<String, Object> customInfo = new HashMap<String, Object>();
			customInfo.put("insured", insured);
			Map<String, Object> applicant = getApplicantInfoMap(processInstanceId, order.getPrvid());//投保人信息
			customInfo.put("applicant", applicant);
			Map<String, Object> legalrightclaim = getLegalrightclaimInfoMap(processInstanceId, order.getPrvid());//权益索赔人信息
			customInfo.put("legalrightclaim", legalrightclaim);
			body.put("customInfo", customInfo);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询保单详细信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询保单详细信息失败！");
		}
		return JSONObject.fromObject(result).toString();
	}

	/**
	 * 提交投保前获得车主信息接口
	 * */
	@Override
	public String getCarOwnerInfoBeforePolicy(String processInstanceId,
			String inscomcode) {
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			//组织响应数据
			Map<String, Object> carOwnerInfo = getCarOwnerInfoMap(processInstanceId, inscomcode);
			body.put("processInstanceId", processInstanceId);//流程实例id
			body.put("inscomcode", inscomcode);//保险公司code
			body.put("carownerinfo", carOwnerInfo);
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("查询车主信息成功！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus("fail");
			result.setMessage("查询车主信息失败！");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
	}

	/**
	 * 提交投保前修改车主信息接口
	 * */
	public String editCarOwnerInfoBeforePolicyTemp(String processInstanceId,
			String inscomcode, String operator, String carOwnerInfoJSON, boolean isSameWithInsured) {
		// 先判断修改前投保人是否同被保人
		// 得到被保人信息
		INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
		// 查询被保人中间表原表信息
		INSBInsured insuredMap = new INSBInsured();
		insuredMap.setTaskid(processInstanceId);
		insuredMap = insbInsuredDao.selectOne(insuredMap);
		// 查询车主中间表信息
		INSBCarowneinfo carOwnerinfo = new INSBCarowneinfo();
		carOwnerinfo.setTaskid(processInstanceId);
		carOwnerinfo = insbCarowneinfoDao.selectOne(carOwnerinfo);
		// 得到车主信息
		INSBPerson carOwnerPerson = null;
		if(carOwnerinfo != null){
			carOwnerPerson = insbPersonDao.selectById(carOwnerinfo.getPersonid());
		}
		boolean isSameWithInsuredBefore = false;
		if(carOwnerPerson!=null){
			if(insuredPerson.getId().equals(carOwnerPerson.getId())){
				isSameWithInsuredBefore = true;//判断修改前是否通被保人
			}
		}
		// 解析修改后的信息json字符串
		INSBPerson pageCarOwner = null;
		if(!isSameWithInsured){
			JSONObject jo = JSONObject.fromObject(carOwnerInfoJSON);
			pageCarOwner = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
		}
		// 修改车主信息并返回修改状态
		Date date = new Date();
		if(isSameWithInsuredBefore){
			if(!isSameWithInsured){//原来相同修改后不同于被保人，新建人员信息，中间历史表记录存在做修改，不存在做添加，重新引用人员id
				pageCarOwner.setCreatetime(date);
				pageCarOwner.setOperator(operator);
				pageCarOwner.setTaskid(processInstanceId);
				pageCarOwner.setGender(this.getGenderByIdtypeAndIdNo(
						pageCarOwner.getIdcardtype(), pageCarOwner.getIdcardno()));
				pageCarOwner.setId(null);
				insbPersonDao.insert(pageCarOwner);
				if(carOwnerinfo == null){
					carOwnerinfo = new INSBCarowneinfo();
					carOwnerinfo.setTaskid(processInstanceId);
					carOwnerinfo.setCreatetime(date);
					carOwnerinfo.setOperator(operator);
					carOwnerinfo.setPersonid(pageCarOwner.getId());
					insbCarowneinfoDao.insert(carOwnerinfo);
				}else{
					carOwnerinfo.setModifytime(date);
					carOwnerinfo.setOperator(operator);
					carOwnerinfo.setPersonid(pageCarOwner.getId());
					insbCarowneinfoDao.updateById(carOwnerinfo);
				}
			}
		}else{
			if(carOwnerPerson!=null){
				if(isSameWithInsured){//原来不同，修改后相同于被保人，中间历史表记录存在做修改，不存在做添加，重新引用人员id
					if(carOwnerinfo == null){
						carOwnerinfo = new INSBCarowneinfo();
						carOwnerinfo.setCreatetime(date);
						carOwnerinfo.setTaskid(processInstanceId);
						carOwnerinfo.setOperator(operator);
						carOwnerinfo.setPersonid(insuredPerson.getId());
						insbCarowneinfoDao.insert(carOwnerinfo);
					}else{
						carOwnerinfo.setModifytime(date);
						carOwnerinfo.setOperator(operator);
						carOwnerinfo.setPersonid(insuredPerson.getId());
						insbCarowneinfoDao.updateById(carOwnerinfo);
					}
				}else{//原来不同修改后也不同于被保人，中间历史表记录存在则人员信息做修改，不存在做添加且人员信息做添加记录
					if(insuredMap.getPersonid().equals(carOwnerPerson.getId())){//和被保人原表相同需要重新新增人员信息
						pageCarOwner.setCreatetime(date);
						pageCarOwner.setOperator(operator);
						pageCarOwner.setTaskid(processInstanceId);
						pageCarOwner.setGender(this.getGenderByIdtypeAndIdNo(
								pageCarOwner.getIdcardtype(), pageCarOwner.getIdcardno()));
						pageCarOwner.setId(null);
						insbPersonDao.insert(pageCarOwner);
						carOwnerinfo.setModifytime(date);
						carOwnerinfo.setOperator(operator);
						carOwnerinfo.setPersonid(pageCarOwner.getId());
						insbCarowneinfoDao.updateById(carOwnerinfo);
					}else{
						carOwnerPerson = setPersonInfo1(pageCarOwner, carOwnerPerson, operator);
						if(carOwnerinfo == null){
							carOwnerPerson.setCreatetime(date);
							carOwnerPerson.setModifytime(null);
							carOwnerPerson.setId(null);
							insbPersonDao.insert(carOwnerPerson);
							carOwnerinfo = new INSBCarowneinfo();
							carOwnerinfo.setCreatetime(date);
							carOwnerinfo.setTaskid(processInstanceId);
							carOwnerinfo.setOperator(operator);
							carOwnerinfo.setPersonid(carOwnerPerson.getId());
							insbCarowneinfoDao.insert(carOwnerinfo);
						}else{
							carOwnerPerson.setModifytime(date);
							insbPersonDao.updateById(carOwnerPerson);
						}
					}
				}
			}else{
				if(isSameWithInsured){
					INSBCarowneinfo temp = new INSBCarowneinfo();
					temp.setTaskid(processInstanceId);
					temp.setOperator(operator);
					temp.setCreatetime(date);
					temp.setPersonid(insuredPerson.getId());
					insbCarowneinfoDao.insert(temp);
				}else{
					carOwnerPerson = new INSBPerson();
					carOwnerPerson = setPersonInfo1(pageCarOwner, carOwnerPerson, operator);
					carOwnerPerson.setTaskid(processInstanceId);
					carOwnerPerson.setCreatetime(date);
					insbPersonDao.insert(carOwnerPerson);
					INSBCarowneinfo temp = new INSBCarowneinfo();
					temp.setTaskid(processInstanceId);
					temp.setOperator(operator);
					temp.setCreatetime(date);
					temp.setPersonid(insuredPerson.getId());
					insbCarowneinfoDao.insert(temp);
				}
			}
		}
		return "success";
	}
	/**
	 * 查询发票信息
	 * @param processInstanceId
	 * @param inscomcode
	 * @return
	 */
	public Map<String, Object> getInvoiceInfoMap(String processInstanceId, String inscomcode) {
		//查询发票信息
		Map<String,String> params=new HashMap<String,String>();
		params.put("taskid", processInstanceId);
		//params.put("inscomcode", inscomcode);
		params.put("inscomcode", null);
		INSBInvoiceinfo insbInvoiceinfo=insbInvoiceinfoDao.selectByTaskidAndComcode(params);
		Map<String,Object> temp=new HashMap<String,Object>();
		if(insbInvoiceinfo!=null){
				if(insbInvoiceinfo.getInvoicetype()==1){
					temp.put("invoicetype", "增值税发票");
					temp.put("bankname",insbInvoiceinfo.getBankname());
					temp.put("accountnumber",insbInvoiceinfo.getAccountnumber());
					temp.put("identifynumber",insbInvoiceinfo.getIdentifynumber());
					temp.put("registerphone",insbInvoiceinfo.getRegisterphone());
					temp.put("registeraddress",insbInvoiceinfo.getRegisteraddress());
					temp.put("email",insbInvoiceinfo.getEmail());
				}else if(insbInvoiceinfo.getInvoicetype()==0){
					temp.put("invoicetype", "普通发票");
					temp.put("bankname","");
					temp.put("accountnumber","");
					temp.put("identifynumber","");
					temp.put("registerphone","");
					temp.put("registeraddress","");
					temp.put("email","");
				}else{
					return null;
				}
		}
		return temp;
	}
	
	
	/**
	 * 封装车主信息
	 * */
	public Map<String, Object> getCarOwnerInfoMap(String processInstanceId, String inscomcode) {
		Map<String, Object> carOwnerInfo = new HashMap<String, Object>();//投保人信息
		// 得到被保人信息
		//INSBPerson insuredPerson = getInsuredInfo(processInstanceId, inscomcode);
		// 得到车主信息
		INSBPerson carOwnerPerson = getCarOwnerInfo(processInstanceId, inscomcode);
		if(carOwnerPerson!=null){
			//carOwnerInfo.put("isSameWithInsured", insuredPerson.getId().equals(carOwnerPerson.getId()));//是否和被保人一致
			//if(!insuredPerson.getId().equals(carOwnerPerson.getId())){
			carOwnerInfo.put("id", carOwnerPerson.getId());//投保人id
			carOwnerInfo.put("name", carOwnerPerson.getName());//投保人姓名
			carOwnerInfo.put("gender", carOwnerPerson.getGender());//投保人性别
			carOwnerInfo.put("idcardtype", carOwnerPerson.getIdcardtype());//投保人证件类型
			carOwnerInfo.put("idcardno", carOwnerPerson.getIdcardno());//投保人证件号
			carOwnerInfo.put("cellphone", carOwnerPerson.getCellphone());//投保人手机号
			carOwnerInfo.put("email", carOwnerPerson.getEmail());//投保人邮箱
			//}
		}else{
			//carOwnerInfo.put("isSameWithInsured", false);//是否和被保人一致
			carOwnerInfo.put("id", "");//投保人id
			carOwnerInfo.put("name", "");//投保人姓名
			carOwnerInfo.put("gender", "");//投保人性别
			carOwnerInfo.put("idcardtype", "");//投保人证件类型
			carOwnerInfo.put("idcardno", "");//投保人证件号
			carOwnerInfo.put("cellphone", "");//投保人手机号
			carOwnerInfo.put("email", "");//投保人邮箱
		}
		return carOwnerInfo;
	}
	
	/**
	 * 查询车主信息
	 * */
	public INSBPerson getCarOwnerInfo(String processInstanceId, String inscomcode) {
		// 查询车主中间表信息
		INSBCarowneinfo carOwnerinfo = new INSBCarowneinfo();
		carOwnerinfo.setTaskid(processInstanceId);
		carOwnerinfo = insbCarowneinfoDao.selectOne(carOwnerinfo);
		// 得到车主信息
		INSBPerson carOwnerPerson = null;
		if(carOwnerinfo != null){
			carOwnerPerson = insbPersonDao.selectById(carOwnerinfo.getPersonid());
		}
		return carOwnerPerson;
	}

	//得到座位数
	public Integer getSeats(String processInstanceId, String inscomcode){
		//取得车辆信息
		INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carInfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		INSBOrder order = new INSBOrder();
		order.setTaskid(processInstanceId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
		if(order==null){
			//不能查到订单表明是人工报价节点
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carInfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
		 }
		return carmodelinfo.getSeat();
	}

	/**
	 * 提交投保前修改被保人信息接口
	 * */
	@Override
	public String editCarOwnerInfoBeforePolicy(String taskId, String inscomcode, String operator,
			String carOwnerInfoJSON, boolean isSameWithInsured) {
		    // 先判断修改前投保人是否同被保人
			// 得到车主信息
			/*INSBPerson insbcarpPerson = getCarOwnerInfo(processInstanceId, inscomcode);
			if(isSameWithInsured){//被保人与车主相同
				INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
				insbInsuredhis.setTaskid(processInstanceId);
				insbInsuredhis.setInscomcode(inscomcode);
				INSBInsuredhis insuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
				if(null == insuredhis){
					insuredhis = new INSBInsuredhis();
					insuredhis.setOperator(operator);
					insuredhis.setCreatetime(new Date());
					insuredhis.setTaskid(processInstanceId);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(insbcarpPerson.getId());
					insbInsuredhisDao.insert(insuredhis);
				}else{
					insuredhis.setOperator(operator);
					insuredhis.setModifytime(new Date());
					insuredhis.setTaskid(processInstanceId);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(insbcarpPerson.getId());
					insbInsuredhisDao.updateById(insuredhis);
				}
			}else{//不相同
				JSONObject jo = JSONObject.fromObject(carOwnerInfoJSON);
				INSBPerson insuredper = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
				
				INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
				insbInsuredhis.setTaskid(processInstanceId);
				insbInsuredhis.setInscomcode(inscomcode);
				INSBInsuredhis insuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
				if(null == insuredhis){
					INSBPerson insbPerson = new INSBPerson();
					insbPerson.setName(insuredper.getName());
					insbPerson.setGender(this.getGenderByIdtypeAndIdNo(insuredper.getIdcardtype(), insuredper.getIdcardno()));
					insbPerson.setIdcardtype(insuredper.getIdcardtype());
					insbPerson.setIdcardno(insuredper.getIdcardno());
					insbPerson.setCellphone(insuredper.getCellphone());
					insbPerson.setEmail(insuredper.getEmail());
					insbPerson.setOperator(operator);
					insbPerson.setCreatetime(new Date());
					insbPerson.setTaskid(processInstanceId);
					insbPersonDao.insert(insbPerson);
					insuredhis = new INSBInsuredhis();
					insuredhis.setOperator(operator);
					insuredhis.setCreatetime(new Date());
					insuredhis.setTaskid(processInstanceId);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(insbPerson.getId());
					insbInsuredhisDao.insert(insuredhis);
				}else{
					if(insbcarpPerson.getId().equals(insuredhis.getPersonid())){
						//插入新的
						INSBPerson insbPerson = new INSBPerson();
						insbPerson.setName(insuredper.getName());
						insbPerson.setGender(this.getGenderByIdtypeAndIdNo(insuredper.getIdcardtype(), insuredper.getIdcardno()));
						insbPerson.setIdcardtype(insuredper.getIdcardtype());
						insbPerson.setIdcardno(insuredper.getIdcardno());
						insbPerson.setCellphone(insuredper.getCellphone());
						insbPerson.setEmail(insuredper.getEmail());
						insbPerson.setOperator(operator);
						insbPerson.setCreatetime(new Date());
						insbPerson.setTaskid(processInstanceId);
						insbPersonDao.insert(insbPerson);
						insuredhis.setOperator(operator);
						insuredhis.setModifytime(new Date());
						insuredhis.setTaskid(processInstanceId);
						insuredhis.setInscomcode(inscomcode);
						insuredhis.setPersonid(insbPerson.getId());
						insbInsuredhisDao.updateById(insuredhis);
					}else{
						INSBPerson insbPerson = insbPersonDao.selectById(insuredhis.getPersonid());
						if(null == insbPerson){
							insbPerson = new INSBPerson();
							insbPerson.setName(insuredper.getName());
							insbPerson.setGender(this.getGenderByIdtypeAndIdNo(insuredper.getIdcardtype(), insuredper.getIdcardno()));
							insbPerson.setIdcardtype(insuredper.getIdcardtype());
							insbPerson.setIdcardno(insuredper.getIdcardno());
							insbPerson.setCellphone(insuredper.getCellphone());
							insbPerson.setEmail(insuredper.getEmail());
							insbPerson.setOperator(operator);
							insbPerson.setCreatetime(new Date());
							insbPerson.setTaskid(processInstanceId);
							insbPersonDao.insert(insbPerson);
						}else{
							insbPerson.setName(insuredper.getName());
							insbPerson.setGender(this.getGenderByIdtypeAndIdNo(insuredper.getIdcardtype(), insuredper.getIdcardno()));
							insbPerson.setIdcardtype(insuredper.getIdcardtype());
							insbPerson.setIdcardno(insuredper.getIdcardno());
							insbPerson.setCellphone(insuredper.getCellphone());
							insbPerson.setEmail(insuredper.getEmail());
							insbPerson.setOperator(operator);
							insbPerson.setModifytime(new Date());
							insbPerson.setTaskid(processInstanceId);
							insbPersonDao.updateById(insbPerson);
						}
						insuredhis.setOperator(operator);
						insuredhis.setModifytime(new Date());
						insuredhis.setTaskid(processInstanceId);
						insuredhis.setInscomcode(inscomcode);
						insuredhis.setPersonid(insbPerson.getId());
						insbInsuredhisDao.updateById(insuredhis);
					}
				}
			}*/
			Date date = new Date();			
			//车主人员信息表
			INSBPerson insbPerson = getCarOwnerInfo(taskId, inscomcode);
			if(StringUtil.isEmpty(insbPerson)){
				LogUtil.info("前端提交投保,修改被保人信息,车主人员信息不存在：taskid="+taskId+",inscomcode="+inscomcode+",操作人="+operator+",Json="+carOwnerInfoJSON);
				return CommonModel.STATUS_FAIL;
			}
			//被保人人员信息表
			INSBPerson insbPersonInsured = new INSBPerson();
			//被保人表信息,为空的时候创建被保人信息,否则直接获取对应人员信息
			INSBInsured insured = new INSBInsured();
			insured.setTaskid(taskId);
			insured = insbInsuredDao.selectOne(insured);
			if(insured==null){
				insbPersonInsured=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_2);
			}
			//判断被保人历史表是否有关联记录，没有添加
		    INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
            insbInsuredhis.setTaskid(taskId);
            insbInsuredhis.setInscomcode(inscomcode);
            insbInsuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
            if (StringUtil.isEmpty(insbInsuredhis)) {
            	insbPersonInsured=insbPersonHelpService.addPersonHisIsNull(insured,taskId, inscomcode, ConstUtil.STATUS_2);
            }else{
            	insbPersonInsured=insbPersonDao.selectById(insbInsuredhis.getPersonid());
            }
			//与车主一致
			if(isSameWithInsured){
				insbPersonInsured=insbPersonHelpService.repairPerson(insbPersonInsured,insbPerson,operator);
				insbPersonDao.updateById(insbPersonInsured);	
			}else{
				JSONObject jo = JSONObject.fromObject(carOwnerInfoJSON);
				INSBPerson insbPersonJson = (INSBPerson) JSONObject.toBean(jo, INSBPerson.class);//转json为INSBPerson对象
				insbPersonInsured=insbPersonHelpService.updateINSBPerson(insbPersonInsured,insbPersonJson,operator,date);	
			}
			//更新保单表
			insbPersonHelpService.updateInsbPolicyitemInsured(taskId,insbInsuredhis.getId(),insbPersonInsured.getName());
			return CommonModel.STATUS_SUCCESS;
		}

	/**
	 * 541 【1029】CM，提供接口，能够判断订单是否为“已失效订单”，并按不同情况进行不同的删除操作
	 *
	 * @param jsonObject
	 * @return
	 */
	@Override
	public String deleteOrder(String jsonObject) {
		CommonModel commonModel = new CommonModel();
		if (StringUtil.isEmpty(jsonObject)) {
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage("参数不能为空");
			return JSONObject.fromObject(commonModel).toString();
		}
		JSONObject object = JSONObject.fromObject(jsonObject);
		String taskid = object.getString("taskid");
		String operator = object.getString("operator");
		if (StringUtil.isEmpty(taskid)) {
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage("任务号不能为空");
			return JSONObject.fromObject(commonModel).toString();
		}
		List<INSBWorkflowsub> list = insbWorkflowsubDao.selectSubModelByMainInstanceId(taskid);
		boolean allClose = true;
		for (INSBWorkflowsub insbWorkflowsub : list) {
			if (StringUtil.isEmpty(insbWorkflowsub.getTaskcode())) {
				allClose = false;
				continue;
			}
			if (!TaskConst.CLOSE_37.equals(insbWorkflowsub.getTaskcode())) {
				allClose = false;
			}
		}

		if (allClose) {
			insbQuotetotalinfoDao.deleteByTaskid(taskid);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("taskid", taskid);
			map.put("operator", operator);
			insbQuotetotalinfoDao.updateDeleteflagByTaskid(map);
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());
		}
		commonModel.setStatus(CommonModel.STATUS_SUCCESS);
		commonModel.setMessage("操作成功");
		return JSONObject.fromObject(commonModel).toString();
	}
}
