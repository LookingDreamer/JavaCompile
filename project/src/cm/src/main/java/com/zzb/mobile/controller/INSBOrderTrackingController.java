package com.zzb.mobile.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.model.OrderTrackingParam;
import com.zzb.mobile.service.INSBOrderTrackingService;

@Controller
@RequestMapping("/mobile/*")
public class INSBOrderTrackingController extends BaseController{
	@Resource
	private INSBOrderTrackingService insbOrderTrackingService;
	
	@RequestMapping(value="basedata/myTask/getOrderTracking",method=RequestMethod.POST)
	@ResponseBody
	public String getOrderTracking(@RequestBody OrderTrackingParam orderTrackingParam){
		return insbOrderTrackingService.showOrderTracking(orderTrackingParam.getProcessInstanceId(),orderTrackingParam.getPrvCode(),orderTrackingParam.getSubInstanceId());
	}
	
}
