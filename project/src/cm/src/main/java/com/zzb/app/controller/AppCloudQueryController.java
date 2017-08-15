package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.zzb.app.service.AppCloudQueryService;

@Controller
@RequestMapping("/cloudquery/*")
public class AppCloudQueryController {

	@Resource
	private AppCloudQueryService appCloudQueryService;
	
	/**
	 * 根据商业险或者交强险保单号查询上年投保信息
	 * @param type 类型  0 商业险保单号 1交强险保单号
	 * @param insureno 保单号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querybynumber", method = RequestMethod.POST)
	@ResponseBody
	public String queryByNumber(@RequestParam String type,@RequestParam String insureno)throws ControllerException{
		return appCloudQueryService.queryByNumber(type,insureno);
	}
	
	/**
	 * 根据车架号或者发动机号查询上年投保信息
	 * @param framenumber 车架号
	 * @param enginenumber 发动机号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querybycar", method = RequestMethod.POST)
	@ResponseBody
	public String queryByCarInfo(@RequestParam String framenumber,@RequestParam String enginenumber)throws ControllerException{
		return appCloudQueryService.queryByCarInfo(framenumber,enginenumber);
	}
}
