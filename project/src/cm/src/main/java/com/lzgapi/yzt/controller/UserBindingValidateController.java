package com.lzgapi.yzt.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.StringUtil;
import com.lzgapi.yzt.bean.CanBindUserValidateBean;
import com.lzgapi.yzt.bean.UptoManagerBean;
import com.lzgapi.yzt.bean.UserBindingValidateBean;
import com.lzgapi.yzt.service.UserBindingValidateService;

import net.sf.json.JSONObject;

/**
 * 
 * 
 * @author Andy
 * @since 11:37 2016/1/12
 * @version 1.0
 */
@Controller
@RequestMapping("/userValidate/*")
public class UserBindingValidateController {
	@Resource
	private UserBindingValidateService validateService;

	/**
	 * 用户绑定验证
	 * 
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "getBindingUsers", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getBindingUsers(
			@RequestParam(value = "params", defaultValue = "") String model)
			throws ControllerException {
		UserBindingValidateBean bean = null;
		if (StringUtil.isNotEmpty(model)) {
			bean = validateService.getBindingUserList(model);
		}

		return JSONObject.fromObject(bean).toString();
	}

	/**
	 * 可绑用户验证
	 * 
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getUserValidateResult", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUserValidateResult(
			@RequestParam(value = "params", defaultValue = "") String model)
			throws ControllerException {
		CanBindUserValidateBean bean = null;
		if (StringUtil.isNotEmpty(model)) {
			bean = validateService.getUserValidateResult(model);
		}
		return JSONObject.fromObject(bean).toString();
	}

	/**
	 * 掌柜升级
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/uptoManager", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uptoManager(@RequestParam(value = "params", defaultValue = "") String model)
			throws ControllerException {
		UptoManagerBean bean = null;
		if (StringUtil.isNotEmpty(model)) {
			bean = validateService.uptoManager(model);
		}
		return JSONObject.fromObject(bean).toString();
	}
}
