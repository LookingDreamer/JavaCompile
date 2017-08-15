package com.zzb.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import com.zzb.mobile.model.ExtendCommonModel;
import com.zzb.mobile.service.AppPermissionService;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.zzb.app.model.bean.TaskAndRisk;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.policyoperat.EditApplicantParam;
import com.zzb.mobile.model.policyoperat.EditCarOwnerInfoParam;
import com.zzb.mobile.model.policyoperat.EditInsuredParam;
import com.zzb.mobile.model.policyoperat.EditLegalrightclaimParam;
import com.zzb.mobile.model.policyoperat.EditPolicyInfoParam;
import com.zzb.mobile.model.policyoperat.PolicyCommitParam;
import com.zzb.mobile.model.policyoperat.PolicyInfoQueryParam;
import com.zzb.mobile.model.policyoperat.PolicyInfoQueryParam02;
import com.zzb.mobile.model.policyoperat.PolicyListQueryParam;
import com.zzb.mobile.model.policyoperat.PolicyitemQueryParam;
import com.zzb.mobile.service.AppMyOrderInfoService;

@Controller
@RequestMapping("/mobile/*")
public class AppMyOrderInfoController {

	@Resource
	private AppMyOrderInfoService appMyOrderInfoService;
	@Resource
	private AppPermissionService appPermissionService;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;

	/**
	 * 刘超s
	 * 提交投保前获得简要的基本信息显示接口  
	 * 接口描述：通过流程实例id和保险公司code查询此任务下的被保人、投保人、权益索赔人简要信息，和添加影像、备注信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getBriefTaskInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getBriefTaskInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getBriefTaskInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getBriefTaskInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 刘超
	 * 提交投保前获得被保人信息接口
	 * 接口描述：通过流程实例id和保险公司code查询此任务下的被保人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getInsuredInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getInsuredInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getInsuredInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getInsuredInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 刘超
	 * 提交投保前修改被保人信息接口
	 * 接口描述：通过流程实例id和保险公司code修改此任务下的被保人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/editInsuredInfoBeforePolicy
	 * @param editInsuredParam processInstanceId 流程实例id, inscomcode 保险公司code, operator 操作员
	 * insuredInfoJSON = {"gender":"被保人性别","name":"被保人姓名","cellphone":"被保人手机号",
	 * "idcardno":"被保人证件号","id":"被保人id","idcardtype":"被保人证件类型","email":"被保人邮箱"} 修改后的被保人信息json字符串
	 */
	@RequestMapping(value = "insured/myTask/editInsuredInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String editInsuredInfoBeforePolicy(@RequestBody EditInsuredParam editInsuredParam)
			throws ControllerException {
		String flag = appMyOrderInfoService.editInsuredInfoBeforePolicy(editInsuredParam.getProcessInstanceId(), 
				editInsuredParam.getInscomcode(), editInsuredParam.getOperator(), 
				editInsuredParam.getInsuredInfoJSON());
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", editInsuredParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", editInsuredParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("修改车主信息成功！");
		}else{
			result.setStatus("fail");
			result.setMessage("修改车主信息失败！");
		}
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 刘超
	 * 提交投保前获得投保人信息接口
	 * 接口描述：通过实例id和保险公司code得到此任务下的投保人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getApplicantInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getApplicantInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getApplicantInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getApplicantInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 刘超
	 * 提交投保前修改投保人信息接口
	 * 接口描述：通过实例id和保险公司code修改此任务下的投保人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/editApplicantInfoBeforePolicy
	 * @param editApplicantParam processInstanceId 流程实例id, inscomcode 保险公司code, operator 操作员, isSameWithInsured 是否同被保人一致
	 * applicantInfoJSON = {"gender":"投保人性别","name":"投保人姓名","cellphone":"投保人手机号",
	 * 	"idcardno":"投保人证件号","idcardtype":"投保人证件类型","email":"投保人邮箱"}修改后的投保人信息
	 */
	@RequestMapping(value = "insured/myTask/editApplicantInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String editApplicantInfoBeforePolicy(@RequestBody EditApplicantParam editApplicantParam)
			throws ControllerException {
		String flag = appMyOrderInfoService.editApplicantInfoBeforePolicy(editApplicantParam.getProcessInstanceId(), 
				editApplicantParam.getInscomcode(), editApplicantParam.getOperator(), 
				editApplicantParam.getApplicantInfoJSON(), editApplicantParam.getIsSameWithInsured());
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", editApplicantParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", editApplicantParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("修改投保人信息成功！");
		}else{
			result.setStatus("fail");
			result.setMessage("修改投保人信息失败！");
		}
		return JSONObject.fromObject(result).toString();
	}

	/**
	 * 刘超
	 * 提交投保前获得权益索赔人信息接口
	 * 接口描述：通过实例id和保险公司code得到此任务下的权益索赔人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getLegalrightclaimInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getLegalrightclaimInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getLegalrightclaimInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getLegalrightclaimInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}

	/**
	 * 刘超
	 * 提交投保前修改权益索赔人信息接口
	 * 接口描述：通过实例id和保险公司code修改此任务下的权益索赔人信息
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/editLegalrightclaimInfoBeforePolicy
	 * @param editLegalrightclaimParam processInstanceId 流程实例id, inscomcode 保险公司code, operator 操作员, isSameWithInsured 是否同被保人一致
	 * legalrightclaimInfoJSON = {"gender":"权益索赔人性别","name":"权益索赔人姓名","cellphone":"权益索赔人手机号",
	 * 	"idcardno":"权益索赔人证件号","idcardtype":"权益索赔人证件类型","email":"权益索赔人邮箱"}修改后的权益索赔人信息
	 */
	@RequestMapping(value = "insured/myTask/editLegalrightclaimInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String editLegalrightclaimInfoBeforePolicy(@RequestBody EditLegalrightclaimParam editLegalrightclaimParam)
			throws ControllerException {
		String flag = appMyOrderInfoService.editLegalrightclaimInfoBeforePolicy(editLegalrightclaimParam.getProcessInstanceId(), 
				editLegalrightclaimParam.getInscomcode(), editLegalrightclaimParam.getOperator(), 
				editLegalrightclaimParam.getLegalrightclaimInfoJSON(), editLegalrightclaimParam.getIsSameWithInsured());
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", editLegalrightclaimParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", editLegalrightclaimParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("修改权益索赔人信息成功！");
		}else{
			result.setStatus("fail");
			result.setMessage("修改权益索赔人信息失败！");
		}
		return JSONObject.fromObject(result).toString();
	}

	/**
	 * 刘超
	 * 提交投保前获得所有投保信息接口
	 * 接口描述：通过实例id得到此任务下的任务全部基本信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getCarTaskInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getCarTaskInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getCarTaskInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getCarTaskInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 刘超
	 * 提交投保接口
	 * 接口描述：通过实例id提交投保推送工作流
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/policySubmit
	 * @param policyCommitParam processInstanceId 流程实例id, inscomcode 保险公司code
	 * agentnum 代理人工号, notice 投保备注(提交投保前一个页面录入，由于此页面并没有生成订单表记录，故推迟到提交投保时录入)
	 * totalproductamount 订单总金额（可以从提交页面直接获取）
	 */
	@RequestMapping(value = "insured/myTask/policySubmit", method = RequestMethod.POST)
	@ResponseBody
	public String policySubmit(@RequestBody PolicyCommitParam policyCommitParam)
			throws ControllerException {
		ExtendCommonModel result = new ExtendCommonModel();
		//权限包
		Map<String,Object> permissionMap = appPermissionService.checkPermission(policyCommitParam.getAgentnum(), policyCommitParam.getProcessInstanceId(), "underwriting");
		if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
			result.setStatus(CommonModel.STATUS_FAIL);
			result.setMessage((String) permissionMap.get("message"));
			return JSONObject.fromObject(result).toString();
		}
		if ((int)permissionMap.get("status") == 1) {
			result.setExtend(permissionMap);
		}
		String flag = appMyOrderInfoService.policySubmit(policyCommitParam.getProcessInstanceId(), 
				policyCommitParam.getInscomcode(), policyCommitParam.getAgentnum(), 
				policyCommitParam.getTotalproductamount(), policyCommitParam.getTotalpaymentamount());

		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", policyCommitParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", policyCommitParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("提交投保成功！");
		}else{
			result.setStatus("fail");
			if(flag!=null){
				result.setMessage(flag);
			}else{
				result.setMessage("提交投保失败！");
			}
		}
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 刘超
	 * 提交修改投保信息接口
	 * 请求方式   POST
	 * 请求地址 /mobile/insured/myTask/editPolicyInfoBeforePolicy
	 * @param editPolicyInfoParam processInstanceId： 流程实例id, inscomcode: 保险公司code operator: 操作员
	 * carproperty: 车辆使用性质,isTransfercar: 0-不是过户车/1-是过户车,property: 所属性质,registdate: 2015-11-11,
	 * busendtime: "2015-11-30",busstarttime: "2015-11-11"
	 */
	@RequestMapping(value = "insured/myTask/editPolicyInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String editPolicyInfoBeforePolicy(@RequestBody EditPolicyInfoParam editPolicyInfoParam) throws ControllerException {
		String operator = editPolicyInfoParam.getOperator();
		if (StringUtil.isEmpty(operator) && StringUtil.isNotEmpty(editPolicyInfoParam.getProcessInstanceId())) {
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(editPolicyInfoParam.getProcessInstanceId());
			insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			operator = insbQuotetotalinfo.getOperator();

			if (StringUtil.isNotEmpty(operator)) {
				editPolicyInfoParam.setOperator(operator);
			}
		}

		String flag = appMyOrderInfoService.editPolicyInfoBeforePolicy(editPolicyInfoParam);
		insbInsuresupplyparamService.updateByTask(editPolicyInfoParam.getSupplyParam(), editPolicyInfoParam.getProcessInstanceId(), editPolicyInfoParam.getInscomcode(), operator);

		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", editPolicyInfoParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", editPolicyInfoParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("修改投保信息成功！");
		}else{
			result.setStatus("fail");
			if(flag!=null){
				result.setMessage(flag);
			}else{
				result.setMessage("修改投保信息失败！");
			}
		}
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 刘超
	 * 查询保单列表信息接口
	 * 接口描述：通过代理人工号查询此代理人之前代理的过得保单列表信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getPolicyitemList
	 * @param policyitemQueryParam agentnum  代理人工号
	 * 通过limit、offset做分页显示，carlicenseno、insuredname做模糊查询
	 */
	@RequestMapping(value = "basedata/myTask/getPolicyitemList", method = RequestMethod.POST)
	@ResponseBody
	public String getPolicyitemList(@RequestBody PolicyitemQueryParam policyitemQueryParam) 
			throws ControllerException {
		return appMyOrderInfoService.getPolicyitemList(policyitemQueryParam.getAgentnum(), 
				policyitemQueryParam.getCarlicenseno(), policyitemQueryParam.getInsuredname(), policyitemQueryParam.getQueryinfo(), 
				policyitemQueryParam.getLimit(), policyitemQueryParam.getOffset(), policyitemQueryParam.getIdcardtype(), 
				policyitemQueryParam.getIdcardno(),policyitemQueryParam.getCode());
	}
	
	/**
	 * 刘超
	 * 查询保单详细信息接口
	 * 接口描述：通过保单id查询保单的详细信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getPolicyitemDetailInfo
	 * @param taskAndRisk processInstanceId  保单id
	 */
	@RequestMapping(value = "basedata/myTask/getPolicyitemDetailInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getPolicyitemDetailInfo(@RequestBody TaskAndRisk taskAndRisk) 
			throws ControllerException {
		return appMyOrderInfoService.getPolicyitemDetailInfo(taskAndRisk.getProcessInstanceId(),taskAndRisk.getPrvid());
	}
	
	/**
	 * 刘超
	 * 查询保单列表信息接口最新
	 * @param policyListQueryParam
	 * agentnum 代理人工号
	 * queryinfo 模糊查询信息
	 * pageSize 每页条数
	 * currentPage 当前页码
	 */
	@RequestMapping(value = "basedata/myTask/getPolicyitemList02", method = RequestMethod.POST)
	@ResponseBody
	public String getPolicyitemList02(@RequestBody PolicyListQueryParam policyListQueryParam) 
			throws ControllerException {
		return appMyOrderInfoService.getPolicyitemList02(policyListQueryParam.getAgentnum(), policyListQueryParam.getQueryinfo(), 
				policyListQueryParam.getQuerytype(), policyListQueryParam.getPageSize(), policyListQueryParam.getCurrentPage());
	}
	
	/**
	 * 刘超
	 * 查询保单详细信息接口
	 * @param policyInfoQueryParam02 policyno 保单号, queryflag 查询标记位（cm、cf）
	 */
	@RequestMapping(value = "basedata/myTask/getPolicyitemDetailInfo02", method = RequestMethod.POST)
	@ResponseBody
	public String getPolicyitemDetailInfo02(@RequestBody PolicyInfoQueryParam02 policyInfoQueryParam02) 
			throws ControllerException {
		return appMyOrderInfoService.getPolicyitemDetailInfo02(policyInfoQueryParam02.getPolicyno(), 
				policyInfoQueryParam02.getQueryflag(), policyInfoQueryParam02.getPolicytype());
	}
	
	/**
	 * 刘超
	 * 提交投保前获得车主信息接口
	 * 接口描述：通过实例id和保险公司code得到此任务下的车主信息
	 * 请求方式 POST
	 * 请求地址 /mobile/basedata/myTask/getCarOwnerInfoBeforePolicy
	 * @param policyInfoQueryParam processInstanceId 流程实例id, inscomcode 保险公司code
	 */
	@RequestMapping(value = "basedata/myTask/getCarOwnerInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String getCarOwnerInfoBeforePolicy(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return appMyOrderInfoService.getCarOwnerInfoBeforePolicy(policyInfoQueryParam.getProcessInstanceId(), 
				policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 刘超
	 * 提交投保前修改车主信息接口
	 * 接口描述：通过实例id和保险公司code修改此任务下的车主信息和发票信息
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/editCarOwnerInfoBeforePolicy
	 * @param editCarOwnerInfoParam processInstanceId 流程实例id, inscomcode 保险公司code, operator 操作员, isSameWithInsured 是否同被保人一致
	 * carOwnerInfoJSON = {"gender":"投保人性别","name":"投保人姓名","cellphone":"投保人手机号",
	 * 	"idcardno":"投保人证件号","idcardtype":"投保人证件类型","email":"投保人邮箱"}修改后的投保人信息
	 */
	@RequestMapping(value = "insured/myTask/editCarOwnerInfoBeforePolicy", method = RequestMethod.POST)
	@ResponseBody
	public String editCarOwnerInfoBeforePolicy(@RequestBody EditCarOwnerInfoParam editCarOwnerInfoParam)
			throws ControllerException {
		String flag = appMyOrderInfoService.editCarOwnerInfoBeforePolicy(editCarOwnerInfoParam.getProcessInstanceId(), 
				editCarOwnerInfoParam.getInscomcode(), editCarOwnerInfoParam.getOperator(), 
				editCarOwnerInfoParam.getCarOwnerInfoJSON(), editCarOwnerInfoParam.getIsSameWithInsured());
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", editCarOwnerInfoParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", editCarOwnerInfoParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("修改被保人信息成功！");
		}else{
			result.setStatus("fail");
			result.setMessage("修改被保人信息失败！");
		}
		return JSONObject.fromObject(result).toString();
	}

	@RequestMapping(value = "insured/myTask/deleteOrder", method = RequestMethod.POST)
	@ResponseBody
	public String deleteOrder(@RequestBody String jsonObject)
			throws ControllerException {
		LogUtil.info("insured/myTask/deleteOrder"+ jsonObject);
		return appMyOrderInfoService.deleteOrder(jsonObject);
	}
}
