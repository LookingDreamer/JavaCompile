package com.lzgapi.yzt.controller;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.lzgapi.yzt.bean.RegisterOrBecomeAgentBean;
import com.lzgapi.yzt.service.RegisterOrBecomeAgentService;

/**
 * 
 * 
 * @author Andy
 * @since 11:37 2016/1/12
 * @version 1.0
 */
@Controller
@RequestMapping("/registerAgent/*")
public class RegisterOrBecomeAgentController {
	@Resource
	private RegisterOrBecomeAgentService egisterOrBecomeAgentService;

	/**
	 * 掌柜注册开通接口
	 * 
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "getAgentInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAgentInfo(
			@RequestParam(value = "params", defaultValue = "") String model)
			throws ControllerException {
		RegisterOrBecomeAgentBean bean = null;
		if (StringUtil.isNotEmpty(model)) {
			bean = egisterOrBecomeAgentService.getAgentInfo(model);
		}
		return JSONObject.fromObject(bean).toString();
	}
}
