package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.zzb.app.service.QuotewayService;

@Controller
@RequestMapping("/quoteway/*")
public class QuoteWayController {

	@Resource
	private QuotewayService quotewayService;

	
	/**
	 * 方法描述：根据代理人的工号返回权限类型以及对应的地址code
	 * 请求方式 POST
	 * 请求地址cm/quoteway/getpermissionsadd
	 * @param jobnum 代理人工号  必须的参数
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getpermissionsadd", method = RequestMethod.POST)
	@ResponseBody
	public String getpermissionsadd(@RequestParam(value="jobnum")String jobnum) throws ControllerException{
		return quotewayService.getpermissionsadd(jobnum,false);
	}
}
