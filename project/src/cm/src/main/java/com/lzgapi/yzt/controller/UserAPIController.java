package com.lzgapi.yzt.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.lzgapi.yzt.model.UserRegisterModel;
import com.lzgapi.yzt.service.UserAPIService;
import com.zzb.mobile.model.CommonModel;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/lzgapi/user/*")
public class UserAPIController extends BaseController {
	@Resource
	private UserAPIService userAPIService;
	
	@Resource
    private HttpServletResponse response;
	
	/**
	 * 接口描述：代理人注册信息
	 * 请求地址   /lzgapi/user/register
	 * @param regInfoJSON 注册信息
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public UserRegisterModel register(@RequestParam String params)
			throws ControllerException {
		return userAPIService.register(params);
	}
	
	/**
	 * 接口描述： 代理人自动注册
	 * 请求地址   /lzgapi/user/registerAuto
	 * @param regInfoJSON 注册信息
	 */
	@RequestMapping(value = "/registerAuto", method = RequestMethod.POST)
	@ResponseBody
	public UserRegisterModel registerAuto(@RequestParam(value="token") String token)
			throws ControllerException {
		return userAPIService.registerAuto(token);
	}
	
	/**
	 * 接口描述：主页进入接口
	 * 请求地址   /lzgapi/user/getIndex
	 * @param regInfoJSON 注册信息
	 */
	@RequestMapping(value = "/getIndex", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getIndex(@RequestParam(value="token") String token, 
			@RequestParam(value="lzgOtherUserId") String lzgOtherUserId, 
			@RequestParam(value="lzgManagerId") String lzgManagerId) throws ControllerException {
		CommonModel model = userAPIService.getIndex(token,lzgOtherUserId,lzgManagerId);
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	
}
