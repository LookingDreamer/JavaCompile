package com.zzb.app.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.app.service.AppCodeService;
import com.zzb.model.AppCodeModel;

@Controller
@RequestMapping("/mobile/basedata*")
public class AppCodeQueryController {

	@Resource
	private AppCodeService codeService;
	
	/**
	 * 初始化前端app页面字典表字段
	 * 
	 * @param String types ="itemtype,orderstatus,orderStatus" 
	 * @return json形式  List<AppCodeModel>   model包括  codeName,codeValue,codeType  三个字段
	 */
	@RequestMapping(value="/initcodetypes",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCodeModel>  initAppCode(  String types){
		return codeService.queryByCodetype(types);
	} 
	
}
