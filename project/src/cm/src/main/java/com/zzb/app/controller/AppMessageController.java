package com.zzb.app.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/appMsg/msg/*")
public class AppMessageController {

	
	/**
	 * 查询代理人账号下未读未删除消息的总数，按跟踪号分组
	 * 
	 * @param proxyId
	 * @return
	 */
	@RequestMapping(value = "/countUnReadByProxyId/{proxyId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> countUnReadByProxyId(@PathVariable String proxyId){

		return null;
	}
	
	/**
	 * 待投保个数提示
	 * 
	 * @param proxyId
	 * @return
	 */
	@RequestMapping(value = "/countUnInsureByProxyId/{proxyId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> countUnInsureByProxyId(@PathVariable String proxyId){

		return null;
	}
	
	/**
	 * 待支付个数提示
	 * 
	 * @param proxyId
	 * @return
	 */
	@RequestMapping(value = "/countUnPayByProxyId/{proxyId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> countUnPayByProxyId(@PathVariable String proxyId){

		return null;
	}
	
	/**
	 * 待续保个数提示
	 * 
	 * @param proxyId
	 * @return
	 */
	@RequestMapping(value = "/countRenewalInsureProxyId/{proxyId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> countRenewalInsureProxyId(@PathVariable String proxyId){
		
		return null;
	}

}
