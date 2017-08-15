package com.zzb.app.controller;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;

@Controller
@RequestMapping("/webzzbtest/login/*")
public class AppTestController extends BaseController {

	/**
	 * 接口简单说明 请求方式 POST 请求地址 /webzzb/login/login
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String loginTest(
			@RequestParam(value = "channelAccount") String username,
			@RequestParam(value = "channelPassword") String password)
			throws ControllerException {
		System.out.println("username = " + username + "password = " + password);
		String result = "";
		if ("admin".equals(username)) {
			result = "{ \"Text\": \"用户验证通过\", \"Message\": \"用户验证通过\", \"Type\": \"2\", \"result\": { \"entity\": { \"uid\": \"3D2D9464EEEC4E82A7ABE42DFAAF678B\", \"sid\": \"8910609151712365201\", \"token\": \"MTQ0MTUzMDc1Ng==d2Vif291e45c9893333194eeeb7e1f1d32f42ca57cf8\", \"terminal\": \"web\" }, \"jobId\": \"921500009\" }}";
		} else {
			result = "{ username wrong }";
		}
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	/**
	 * TODO 按关键字搜索车型列表
	 * 
	 * @param MSM_PARAM 参数字符串 【key=奥迪A6;count=10;off=1】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST)
	@ResponseBody
	public String queryVehicleByType(@RequestParam String MSM_HEADER,@RequestParam String MSM_PARAM) throws ControllerException {

		System.out.println(MSM_HEADER+"=="+MSM_PARAM);
		return "{}";
	}
	
	@RequestMapping(value = "/access6", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search")
	@ResponseBody
	public String queryProviderList(@RequestParam String MSM_PARAM) throws ControllerException {

		System.out.println("queryProviderList=="+MSM_PARAM);
		return "{test}";
	}
	
	@RequestMapping(value = "/access2", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search")
	@ResponseBody
	public String hejieTest(@RequestParam String MSM_PARAM)
			throws ControllerException {
		System.out.println("hejieTest=="+MSM_PARAM);
		return "{success!}";
	}
	
	@RequestMapping(value = "/access2", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER")
	@ResponseBody
	public String hejieTest2(@RequestParam String MSM_PARAM)
			throws ControllerException {
		System.out.println("hejieTest2=="+MSM_PARAM);
		return "{success!}";
	}
	
}
