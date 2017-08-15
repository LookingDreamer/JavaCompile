package com.zzb.mobile.controller;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.StringUtil;
import com.zzb.mobile.model.AppInsuredMyModel;
import com.zzb.mobile.model.AppMypolicsModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CustomInfoModel;
import com.zzb.mobile.model.PersonalInfoModel;
import com.zzb.mobile.service.AppInsuredMyService;

@Controller
@RequestMapping("/mobile/basedata/my/*")
public class AppInsuredMyController extends BaseController {

	@Resource
	private AppInsuredMyService appInsuredMyService;
	
	@RequestMapping(value="/delMyPolicies",method=RequestMethod.POST)
	@ResponseBody
	 public CommonModel delMyPolicies(@RequestBody List<String> ids){
		return appInsuredMyService.delMyPolicies(ids);
	 }
	
	@RequestMapping(value="/getMyPoliciesByPagging",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMyPoliciesByPagging(@RequestBody AppInsuredMyModel model) {
		return appInsuredMyService.getMyPolicies(model.getJobNum(), model.getKeyword(),StringUtil.isEmpty("offset")?0:model.getOffset(),StringUtil.isEmpty("limit")?10:model.getLimit());
		
	}
	
	@RequestMapping(value="/getMyCustomers",method=RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public CommonModel getMyCustomers(@RequestBody AppInsuredMyModel model) {
		return appInsuredMyService.getMyCustomers(model.getJobNum(), model.getCustomerName());
	}
	
	@RequestMapping(value="/getMyCustomerDetail",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public CommonModel getMyCustomerDetail(@RequestBody AppInsuredMyModel model) {
		return appInsuredMyService.getMyCustomerDetail(model.getJobNum(), model.getIdCardType(), model.getIdCardNo(), model.getCustomerName());
	}
	
	@RequestMapping(value="/getMyCustomerCarInfo",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMyCustomerCarInfos(@RequestBody AppInsuredMyModel model) {
		return appInsuredMyService.getMyCustomerCarInfo(model.getJobNum(), model.getIdCardType(),model.getIdCardNo(), model.getCustomerName());
	}
	
	@RequestMapping(value="/getMyCustomerPolicyInfos",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMyCustomerPolicyInfos(@RequestBody AppInsuredMyModel model) {
		return appInsuredMyService.getMyCustomerPolicyInfos(model.getJobNum(), model.getIdCardType(),model.getIdCardNo(),0,10);
	}
	
	@RequestMapping(value="/getMyPersonalInfo",method=RequestMethod.POST)
	@ResponseBody
	public PersonalInfoModel getMyPersonalInfo(@RequestBody String jobNum) {
		return appInsuredMyService.getMyPersonalInfo(jobNum);
			
	}
	
	@RequestMapping(value="/EditMyCustomerInfos",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel EditMyCustomerInfos(@RequestBody JSONObject jsonparams) {
		return appInsuredMyService.EditMyCustomerInfos(jsonparams);
			
	}
	@RequestMapping(value="/delMyCustomerInfos",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel delMyCustomerInfos(@RequestBody String id){
		
		return appInsuredMyService.delMyCustomerInfos(id);
	}
	
}
