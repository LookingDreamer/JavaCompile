package com.zzb.mobile.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbpaymentpasswordModel;
import com.zzb.mobile.service.InsbpaymentpasswordService;

@Controller
@RequestMapping("/mobile/basedata/my/*")

public class InsbpaymentpasswordController extends BaseController {

	@Resource
	private InsbpaymentpasswordService insbpaymentpasswordService;
	
	
	@RequestMapping(value="/passwordValiadate",method = RequestMethod.POST)
	@ResponseBody
	public CommonModel passwordValiadate(@RequestBody InsbpaymentpasswordModel model) {
		
		return insbpaymentpasswordService.passwordValiadate(model.getJobNum(), model.getLogpwd());
	
	}	
	
	@RequestMapping(value="/paypwdSetting",method = RequestMethod.POST)
	@ResponseBody
	public CommonModel paypwdSetting(@RequestBody InsbpaymentpasswordModel model) {
		
		return insbpaymentpasswordService.paypwdSetting(model.getJobNum(), model.getPaypwd());
	
	}
	
	@RequestMapping(value="/hasPayPassword",method = RequestMethod.POST)
	@ResponseBody
	public CommonModel hasPayPassword(@RequestBody InsbpaymentpasswordModel model) {
		
		return insbpaymentpasswordService.hasPayPassword(model.getJobNum());
	
	}	
	
	
}
