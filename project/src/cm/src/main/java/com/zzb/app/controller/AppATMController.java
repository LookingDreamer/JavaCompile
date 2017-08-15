package com.zzb.app.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.app.service.AppAgentService;

@Controller
@RequestMapping("/appMsg/msg/*")
public class AppATMController {

	
	@Resource
	private AppAgentService appAgentService;
	/**
	 * 获取代理人信息
	 * 
	 * @param idType 类型
	 * @param uuid 代理人id
	 * @return
	 */
	@RequestMapping(value = "/online/{idType}/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> queryAgentByParam(@PathVariable String idType,@PathVariable String uuid){
		Map<String,Object> result = appAgentService.queryByAgentId(idType,uuid);
		return result;
	}
}
