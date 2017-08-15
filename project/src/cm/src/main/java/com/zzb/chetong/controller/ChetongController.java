package com.zzb.chetong.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.chetong.service.ChetongService;
import com.zzb.mobile.model.CommonModel;

@Controller
@RequestMapping("/mobile/basedata/chetong/*")
public class ChetongController {
	@Resource
	ChetongService  ctService;
	@RequestMapping(value="jpCT",method=RequestMethod.GET)
	@ResponseBody
	public CommonModel  jumpingToChetong(@RequestParam String ownername ,@RequestParam String carlicenseno ){
		return ctService.jumpingToChetong(ownername, carlicenseno); 
	} 
	@RequestMapping(value="jpctpolicyId",method=RequestMethod.GET)
	@ResponseBody
	public CommonModel  jumpingToChetong(String policyId){
		return ctService.jumpingToChetong(policyId);
	} 
}
