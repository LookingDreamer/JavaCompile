package com.zzb.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.mobile.util.MappingType;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.PayParam;
import com.zzb.mobile.service.AppPaymentService;
import com.zzb.mobile.service.INSBAgentstandbyinfoService;
import com.zzb.mobile.service.INSBPaymentService;
import com.zzb.mobile.service.impl.INSBPaymentServiceImpl;
import com.zzb.mobile.util.PayConfigMappingMgr;
import com.zzb.mobile.util.PayStatus;

@Controller
@RequestMapping("/mobile/basedata/payment/*")
public class AppPaymentServiceController {
	@Resource
	private AppPaymentService payService;
	@Resource
	private INSBPaymentService insbPaymentService;
	@Resource
	private INSBAgentstandbyinfoService agentstandbyinfoService;
	private static Logger logger = Logger.getLogger(AppPaymentServiceController.class);

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public Object view() {
		return PayConfigMappingMgr.getDoc();
	}

	@RequestMapping(value = "/view/{mapType}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Object viewSingle(@PathVariable("mapType") String mapType, @PathVariable("code") String code) {
		return PayConfigMappingMgr.getPayCodeByCmCode(mapType, code);
	}
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel pay(@RequestBody PayParam payParam) {
		JSONObject jsonObj = JSONObject.fromObject(payParam);
		logger.info("支付请求:" + jsonObj.toString());
		CommonModel result = null;
		try{
			result = insbPaymentService.pay(payParam);
		}catch(Exception e){
			e.printStackTrace();
			result = new CommonModel();
			result.setStatus("fail");
			result.setMessage("系统异常，请稍候再试！");
			LogUtil.error("支付时出现异常", e);
		}
		jsonObj = JSONObject.fromObject(result);
		logger.info("支付响应:" + jsonObj.toString());
		return result;
	}

	@RequestMapping(value = "/queryPayResult", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel queryPayResult(@RequestBody PayParam payParam) {
		JSONObject jsonObj = JSONObject.fromObject(payParam);
		logger.info("支付查询请求:" + jsonObj.toString());
		CommonModel result;
		if(payParam.getIsUnderwriteQuote() != null){
			result =  insbPaymentService.queryPayResult(payParam.getBizId(),payParam.getIsUnderwriteQuote());
		}else
			result =  insbPaymentService.queryPayResult(payParam.getBizId(),false);

		jsonObj = JSONObject.fromObject(result);
		logger.info("支付查询响应:bizId=" +payParam.getBizId() + " " + jsonObj.toString());
		return result;
	}

	@RequestMapping(value = "/testPay", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel testPay(@RequestBody PayParam payParam) {
		return payService.testPay(payParam.getBizId());
	}

	@RequestMapping(value = "/toPaying", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel toPaying(@RequestBody PayParam payParam) {
		return payService.updateOrderPayment(payParam.getBizId(), PayStatus.PAYING);
	}

	@RequestMapping(value = "/validateOrderPayment", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel validateOrderPayment(@RequestBody PayParam payParam) {
		return payService.validateOrderPayment(payParam.getBizId());
	}

	/**
	 * 获取快刷类型列表接口
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getQuickBrushTypeList", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getQuickBrushTypeList() throws ControllerException {
		return agentstandbyinfoService.getQuickBrushTypeList();
	}

	/**
	 * 获取快刷类型列表接口
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getQuickBrushType", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getQuickBrushType(@RequestParam(value = "agentId") String agentId) throws ControllerException {
		Map<Object, Object> hashMap = new HashMap<Object, Object>();
		hashMap.put("agentId", agentId);
		return agentstandbyinfoService.getQuickBrushType(hashMap);
	}

	/**
	 * 保存和修改快刷类型的接口
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/SaveOrUpdataQuickBrushType", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel SaveOrUpdataQuickBrushType(@RequestParam(value = "agentId") String agentId,
			@RequestParam(value = "quickBrushType") String quickBrushType) throws ControllerException {
		Map<Object, Object> hashMap = new HashMap<Object, Object>();
		hashMap.put("agentId", agentId);
		hashMap.put("quickBrushType", quickBrushType);
		return agentstandbyinfoService.saveOrUpdataQuickBrushType(hashMap);
	}

	/**
	 * 保存和修改快刷类型的接口
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/paySuccess", method = RequestMethod.GET)
	@ResponseBody
	public String paySuccess(@RequestParam(value = "bizId") String bizId, @RequestParam(value = "payType") String payType) throws ControllerException {
		return payService.paySuccess(bizId, payType);
	}

	/**
	 * 渠道支付
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/channelPay", method = RequestMethod.GET)
	@ResponseBody
	public String channelPay(@RequestParam(value = "taskid") String taskId, @RequestParam(value = "companyid") String companyId)
			throws ControllerException {
		return payService.channelPay(taskId, companyId);
	}

	/**
	 * 渠道跳转收银台接口
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/jumpCashRegister", method = RequestMethod.GET)
	@ResponseBody
	public String jumpCashRegister(@RequestParam(value = "param") String param) throws ControllerException {
		LogUtil.error("进入jumpCashRegister方法，param=" + param);
		String url = PayConfigMappingMgr.getChannelPayUrl() + "?param=" + param;
		LogUtil.error("关闭jumpCashRegister方法，param=" + param + ",url=" + url);
		return url;
	}
}
