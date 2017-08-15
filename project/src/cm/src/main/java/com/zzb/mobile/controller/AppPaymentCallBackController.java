package com.zzb.mobile.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.mobile.model.CallBackParam;
import com.zzb.mobile.service.AppPaymentCallBackService;
import com.zzb.mobile.service.INSBPaymentService;

@Controller
@RequestMapping("/mobile/pay/*")
public class AppPaymentCallBackController {

	@Resource
	private AppPaymentCallBackService callbackService;
	@Resource
	private INSBPaymentService insbPaymentService;

	@Resource
	private HttpServletRequest request;

	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String callBack(@RequestBody CallBackParam params) {
		return insbPaymentService.callBack(params);
		// return
		// callbackService.callBack(params.getBizId(),params.getBizTransactionId(),
		// params.getInsSerialNo(),
		// params.getPaySerialNo(), params.getAmount(), params.getTransDate(),
		// params.getPayResult(), params.getOrderState(),
		// params.getPayResultDesc(),params.getNonceStr(),params.getSign());
	}
}
