package com.zzb.mobile.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppEpayInfoService;

import net.sf.json.JSONObject;
@Controller
@RequestMapping("/mobile/basedata/my/*") 


public class AppEpayInfoController extends BaseController{

@Resource
private AppEpayInfoService AppEpayInfoService;


   
@RequestMapping(value="/queryEpayInfos",method=RequestMethod.POST)
@ResponseBody
public CommonModel queryEpayInfos(@RequestBody String jobNum){
	
	return AppEpayInfoService.queryEpayInfos(jobNum);
}

@RequestMapping(value="/writebackName",method=RequestMethod.POST)
@ResponseBody
public CommonModel writebackName(@RequestBody String jobNum)throws Exception{
	
	return AppEpayInfoService.writebackName(jobNum);
}


@RequestMapping(value="/EpayInfos",method=RequestMethod.POST)
@ResponseBody
public CommonModel EpayInfos(@RequestBody JSONObject params) throws Exception{
	
	return AppEpayInfoService.EpayInfos(params);
}

@RequestMapping(value="/submitEpayInfos",method=RequestMethod.POST)
@ResponseBody
public CommonModel submitEpayInfos(@RequestBody JSONObject params){
  

	return AppEpayInfoService.submitEpayInfos(params);
}  

@RequestMapping(value="/getProvinceInfo",method=RequestMethod.POST)
@ResponseBody
public CommonModel getProvinceInfo(){

	return AppEpayInfoService.getProvinceInfo();
}

@RequestMapping(value="/getCityInfo",method=RequestMethod.POST)
@ResponseBody
public CommonModel getCityInfo(@RequestBody String provinceID){

	return AppEpayInfoService.getCityInfo(provinceID);
}


}
