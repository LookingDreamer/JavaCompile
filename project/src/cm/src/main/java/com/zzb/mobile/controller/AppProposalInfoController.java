package com.zzb.mobile.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.zzb.mobile.model.policyoperat.PolicyInfoQueryParam;
import com.zzb.mobile.service.AppProposalInfoService;

@Controller
@RequestMapping("/mobile/insured/myTask/proposal/*")
public class AppProposalInfoController {

	@Resource
	private AppProposalInfoService AppProposalInfoService;

	/**
	 * 多方报价 查看投保信息
	 * 接口描述：通过任务id获取此任务下的车辆信息、车型信息、被保人信息、投保人信息、订单信息及其他信息
	 * 请求方式 get
	 * 请求地址/mobile/insured/myTask/proposal/getProposalInfo
	 * @param policyInfoQueryParam
	 */
	@RequestMapping(value = "/getProposalInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getProposalInfo(@RequestBody PolicyInfoQueryParam policyInfoQueryParam)
			throws ControllerException {
		return AppProposalInfoService.getProposalInfo(policyInfoQueryParam.getProcessInstanceId(),policyInfoQueryParam.getInscomcode());
	}
	
	/**
	 * 多方报价 修改投保信息
	 * 接口描述：通过传入的修改后的投保信息，去修改此任务下的投保信息，包括车辆信息、车型信息、被保人信息、投保人信息、订单信息及其他信息；
	 * 请求方式  post
	 * 请求地址/mobile/insured/myTask/proposal/updateProposalInfo
	 * @param proposalInfo 投保信息json
	 */
	@RequestMapping(value = "/updateProposalInfo", method = RequestMethod.POST)
	@ResponseBody
	public String updateProposalInfo(@RequestBody String proposalInfo)
			throws ControllerException {
		return AppProposalInfoService.updateProposalInfo(proposalInfo);
	}
	
	
}
