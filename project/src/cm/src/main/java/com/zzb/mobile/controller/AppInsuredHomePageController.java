package com.zzb.mobile.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuredHomePagePram;
import com.zzb.mobile.service.AppInsuredHomePageService;

@Controller
@RequestMapping("/mobile/insured/homepage/*")
public class AppInsuredHomePageController extends BaseController{

@Resource	
private AppInsuredHomePageService appInsuredHomePageService;	

@RequestMapping(value = "/getTobePolicyNum", method = RequestMethod.POST)
@ResponseBody
public CommonModel getTobePolicyNum(@RequestBody InsuredHomePagePram pram)throws ControllerException{

	return appInsuredHomePageService.getTobePolicyNum(pram.getJobNum());

}

@RequestMapping(value = "getTobePaymentOrderNum", method = RequestMethod.POST)
@ResponseBody
public CommonModel getTobePaymentOrderNum(@RequestBody InsuredHomePagePram pram)throws ControllerException{
	
	return appInsuredHomePageService.getTobePaymentOrderNum(pram.getJobNum());
		
}

@RequestMapping(value = "/renewalRemindNum", method = RequestMethod.POST)
@ResponseBody
public CommonModel renewalRemindNum(@RequestBody InsuredHomePagePram pram )throws ControllerException{
	
	return appInsuredHomePageService.renewalRemindNum(pram.getJobNum());
		
}	

}
