package com.zzb.mobile.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.common.MiscConst;
import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.model.ExtendCommonModel;
import com.zzb.mobile.service.AppPermissionService;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.WorkFlowUtil;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.MultiQuoteInfoParam;
import com.zzb.mobile.model.UpdateInsureDateModel;
import com.zzb.mobile.service.INSBMultiQuoteInfoService;
import com.zzb.mobile.util.PayStatus;

@Controller
@RequestMapping("/mobile/*")
public class INSBMultiQuoteInfoController extends BaseController{
	
	@Resource
	private INSBMultiQuoteInfoService insbMultiQuoteInfoService;
	@Resource
	private INSBManualPriceService insbManualPriceService;
	@Resource
	private INSBWorkflowmainService insbWorkflowmainService;
	@Resource
	private INSBOrderpaymentDao orderpaymentDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private AppPermissionService appPermissionService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBAgreementDao insbAgreementDao;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBAgreementService insbAgreementService;
	/*
	 * 获取多方报价列表
	 */
	@RequestMapping(value = "basedata/myTask/getMultiQuoteInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getMultiQuoteInfo(HttpServletRequest request, @RequestHeader(value = "token") String token,
			@RequestBody MultiQuoteInfoParam multiQuoteInfoParam) {
		LogUtil.info("查看任务详情：" + token + ",json=" + JSONObject.fromObject(multiQuoteInfoParam).toString());
		String result = insbMultiQuoteInfoService.getMultiQuoteInfo(request,multiQuoteInfoParam.getProcessInstanceId(),multiQuoteInfoParam.getInscomcode(),null,null);
		LogUtil.info("查看任务详情响应：" + token + ", taskId=" + multiQuoteInfoParam.getProcessInstanceId() + ", 返回");
		return result;
	}

	/*
	 * 获取多方报价列表
	 */
	@RequestMapping(value = "basedata/myTask/getMultiQuoteInfoForChn", method = RequestMethod.POST)
	@ResponseBody
	public String getMultiQuoteInfoForChn(HttpServletRequest request, @RequestHeader(value = "token") String token,
									@RequestBody MultiQuoteInfoParam multiQuoteInfoParam) {
		LogUtil.info("查看任务详情：token=" + token + ",json=" + JSONObject.fromObject(multiQuoteInfoParam).toString());
		String result = insbMultiQuoteInfoService.getMultiQuoteInfo(request, multiQuoteInfoParam.getProcessInstanceId(), multiQuoteInfoParam.getInscomcode(), multiQuoteInfoParam.getChannelId(),multiQuoteInfoParam.getChannelUserId());
		LogUtil.info("查看任务详情响应：" + token + ", taskId=" + multiQuoteInfoParam.getProcessInstanceId() + ", 返回结果result=" + result);
		return result;
	}

	/*
	 * 查看单方历史备注
	 */
	@RequestMapping(value = "basedata/myTask/comment", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel comment(@RequestBody MultiQuoteInfoParam multiQuoteInfoParam) {
		LogUtil.info("查看单方历史备注：" +  "json=" + JSONObject.fromObject(multiQuoteInfoParam).toString());
		List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserComment3(
				multiQuoteInfoParam.getProcessInstanceId(), multiQuoteInfoParam.getInscomcode(), null,null);
		CommonModel resultAll = new CommonModel();
		resultAll.setMessage("查看单方历史备注");
		resultAll.setStatus(CommonModel.STATUS_SUCCESS);
		resultAll.setBody(usercomment);
		LogUtil.info("查看单方历史备注：" + "taskId=" + multiQuoteInfoParam.getProcessInstanceId() + ", 返回结果result="+JSONObject.fromObject(resultAll));
		return resultAll;
	}

	/*
	 * 检查协议核保状态
	 */
	@RequestMapping(value = "basedata/myTask/checkUnderwritestatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel checkUnderwritestatus(@RequestBody MultiQuoteInfoParam multiQuoteInfoParam) {
		CommonModel resultAll = new CommonModel();
		LogUtil.info("检查协议核保状态：" +  "json=" + JSONObject.fromObject(multiQuoteInfoParam).toString());
		if (StringUtil.isEmpty(multiQuoteInfoParam.getProcessInstanceId()) || StringUtil.isEmpty(multiQuoteInfoParam.getInscomcode())) {
			resultAll.setMessage("检查协议核保状态,非法查询条件 instanceid" + multiQuoteInfoParam.getProcessInstanceId() + " inscomcode" + multiQuoteInfoParam.getInscomcode());
			resultAll.setStatus(CommonModel.STATUS_FAIL);
			return resultAll;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", multiQuoteInfoParam.getProcessInstanceId());
		map.put("inscomcode", multiQuoteInfoParam.getInscomcode());
		INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
		if (insbQuoteinfo == null) {
			resultAll.setMessage("检查协议核保状态  instanceid" + multiQuoteInfoParam.getProcessInstanceId() + " inscomcode" + multiQuoteInfoParam.getInscomcode() + " 找不到报价数据");
			resultAll.setStatus(CommonModel.STATUS_FAIL);
			return resultAll;
		}
		int i = insbAgreementService.checkUnderwritestatus(insbQuoteinfo.getAgreementid());
		resultAll.setMessage("检查协议核保状态");
		resultAll.setStatus(CommonModel.STATUS_SUCCESS);
		resultAll.setBody(i);
		return resultAll;
	}
	
	/**
	 * 取消投保
	 */
	@RequestMapping(value = "basedata/myTask/cancelCover", method = RequestMethod.POST)
	@ResponseBody
	public String cancelCover(@RequestBody Map<String, Object> params) {
		String instanceId = (String) params.get("instanceId");
		String mainorsub = (String) params.get("mainorsub");//子流程,就是取消子流程的任务
		CommonModel model = new CommonModel();
		try{
			String str = null;

			INSBWorkflowsub worksub = new INSBWorkflowsub();
			worksub.setInstanceid(instanceId);
			worksub = insbWorkflowsubDao.selectOne(worksub);

			INSBOrderpayment pay = new INSBOrderpayment();
			pay.setTaskid(worksub.getMaininstanceid());
			pay.setSubinstanceid(instanceId);
			pay = orderpaymentDao.selectOne(pay);
			if(pay!=null){
				if(PayStatus.PAYING.equals(pay.getPayresult())){
					model.setMessage("订单正在支付中，不能取消投保");
					model.setStatus("fail");
					return JSONObject.fromObject(model).toString();
				}
			}

			WorkflowFeedbackUtil.setWorkflowFeedback(worksub.getMaininstanceid(), instanceId, worksub.getTaskcode(), "Completed", null,
					WorkflowFeedbackUtil.quote_cancel, "admin");
            WorkflowFeedbackUtil.setWorkflowFeedback(worksub.getMaininstanceid(), instanceId, "37", "Closed", null,
					WorkflowFeedbackUtil.quote_cancel+"#前端取消投保", "admin");

			INSBQuoteinfo quote =  new INSBQuoteinfo();
			quote.setWorkflowinstanceid(instanceId);
			quote = insbQuoteinfoService.queryOne(quote);
			str = insbManualPriceService.refuseUnderwrite2(worksub.getMaininstanceid(),instanceId,quote.getInscomcode(),mainorsub,"front");
			
			if(str==null){
				model.setMessage("已支付不能取消投保");
				model.setStatus("fail");
			}else{
				JSONObject result = JSONObject.fromObject(str);
				if("success".equals(result.getString("status"))){
					model.setMessage("取消投保成功");
					model.setStatus("success");
				}else{
					model.setMessage("取消投保失败");
					model.setStatus("fail");
				}
			}
			return JSONObject.fromObject(model).toString();
		}catch(Exception e ){
			e.printStackTrace();
			model.setMessage("取消投保操作失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
	}
	
	
	/*
	 * 取消报价
	 */
	@RequestMapping(value = "basedata/myTask/cancelQuote", method = RequestMethod.POST)
	@ResponseBody
	public String cancelQuote(@RequestBody Map<String, Object> params) {
		String instanceId = (String) params.get("instanceId");
		String mainorsub = (String) params.get("mainorsub");//走的就是主流程
		CommonModel model = new CommonModel();
		try{
			String str = null;
			INSBWorkflowmain workflowmain = insbWorkflowmainService.selectByInstanceId(instanceId);
			//支付后的节点
			if(!MiscConst.noCancelNodeList.contains(workflowmain.getTaskcode())){
				List<INSBWorkflowsub> subList = workflowsubService.selectSubModelByMainInstanceId(instanceId);
				if (subList != null) {
					for (INSBWorkflowsub sub : subList) {
						WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), sub.getTaskcode(), "Completed", null,
								WorkflowFeedbackUtil.multiQuote_cancel, "admin");
						WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), "37", "Closed", null,
								WorkflowFeedbackUtil.multiQuote_cancel+"#前端取消报价", "admin");
					}
				}

                WorkflowFeedbackUtil.setWorkflowFeedback(instanceId, null, "37", "Closed", null, WorkflowFeedbackUtil.multiQuote_cancel+"#前端取消报价", "admin");
                str = insbManualPriceService.refuseUnderwrite2(instanceId,"","",mainorsub,"front");
			}
			if(str==null){
				model.setMessage("已支付不能取消报价");
				model.setStatus("fail");
			}else{
				JSONObject result = JSONObject.fromObject(str);
				if("success".equals(result.getString("status"))){
					model.setMessage("取消报价成功");
					model.setStatus("success");
				}else{
					model.setMessage("取消报价失败");
					model.setStatus("fail");
				}
			}
			return JSONObject.fromObject(model).toString();
		}catch(Exception e ){
			e.printStackTrace();
			model.setMessage("取消报价操作失败");
			model.setStatus("fail");
			
			return JSONObject.fromObject(model).toString();
		}
	}
	
	@RequestMapping(value = "basedata/myTask/resqquotation", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel resqQuotation(@RequestParam(value="processinstanceid") String processinstanceid,@RequestParam(value="inscomcode") String inscomcode) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if(StringUtil.isEmpty(processinstanceid) || StringUtil.isEmpty(inscomcode)){
				commonModel.setMessage("参数不正确");
				commonModel.setStatus("fail");
				return commonModel;
			}
			//bug 5469【生产环境】[福建平台]前端在9月30号提了单子，前端承保规则限制之后，在10月17号关了人保泉州的供应商协议，但是10.28号还是能走EDI报价（未下掉人保的泉州的EDI能力）
			INSBAgreement agreement = new INSBAgreement();
			agreement.setProviderid(inscomcode);
			agreement.setAgreementstatus("1");//已生效
			List<INSBAgreement> agreementList = insbAgreementDao.selectList(agreement);
			if (agreementList == null || agreementList.isEmpty()) {
				commonModel.setStatus("fail");
				commonModel.setMessage("该保险公司已下架，请选择其他公司投保。");
				return commonModel;
			}
			//权限包
			Map<String,Object> permissionMap = appPermissionService.checkPermission("", processinstanceid, "quote");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}
			if ((int)permissionMap.get("status") == 1) {
				commonModel.setExtend(permissionMap);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", processinstanceid);
			map.put("inscomcode", inscomcode);
			INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if(null == insbQuoteinfo || StringUtil.isEmpty(insbQuoteinfo.getWorkflowinstanceid())){
				commonModel.setMessage("报价信息不存在");
				commonModel.setStatus("fail");
				return commonModel;
			}

			WorkflowFeedbackUtil.setWorkflowFeedback(processinstanceid, insbQuoteinfo.getWorkflowinstanceid(), "52", "Completed", "等待报价请求", "提交报价", "admin");

			String result = WorkFlowUtil.resquestQuotationToWorkflow(insbQuoteinfo.getWorkflowinstanceid(),"等待报价请求",null);
			LogUtil.info("请求报价:"+ processinstanceid + "," + inscomcode + "==result:"+result);

			if(!StringUtil.isEmpty(result)){
				JSONObject object = JSONObject.fromObject(result);
				if(object.containsKey("message") && "success".equals(object.getString("message"))){
					commonModel.setMessage("操作成功");
					commonModel.setStatus("success");
				}else{
					commonModel.setMessage("请求报价失败了");
					commonModel.setStatus("fail");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setMessage("请求报价失败");
			commonModel.setStatus("fail");
		}
		return commonModel;
	}
	/**
	 * 单方修改起保日期，调用工作流重新报价
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "basedata/myTask/updateinsuredate", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateInsureDate(@RequestBody UpdateInsureDateModel model) {
		return insbMultiQuoteInfoService.updateInsureDate(model);
	}
}
