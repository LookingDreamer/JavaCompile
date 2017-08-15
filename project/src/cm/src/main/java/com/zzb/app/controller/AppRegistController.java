package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.app.model.AppRegistModel;
import com.zzb.app.service.AppRegistService;

@Controller
@RequestMapping("/online/*")
public class AppRegistController extends BaseController{
	
	@Resource
	private AppRegistService appRegistService;
	
	
	/**
	 * 找回密码--发送验证码
	 * 请求方式	POST
	 * 请求地址	/online/findpws/
	 * @param   account 手机号(帐号/工号)
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/findpws",method=RequestMethod.POST)
	@ResponseBody
	public String findpws(@RequestParam(value="account") String account) throws ControllerException{
		return appRegistService.findpws(account);
	}
	
	/**
	 * 校验忘记密码验证码(身份证)
	 * 请求方式	POST
	 * 请求地址	/online/validator/
	 * @param   phone  手机号
	 * @param   code   验证码
	 * @param   idno   身份证号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/validator",method=RequestMethod.POST)
	@ResponseBody
	public String validatorFindPws(@RequestParam(value="phone") String phone,@RequestParam(value="code") String code,@RequestParam(value="idno") String idno) throws ControllerException {
		return appRegistService.validatorFindPws(phone, code, idno);
	}
	
	/**
	 * 修改代理人登录密码
	 * 请求方式	POST
	 * 请求地址	/online/updatepwd/
	 * @param phone
	 * @param newpwd
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/updatepwd",method=RequestMethod.POST)
	@ResponseBody
	public String updatepwd(@RequestParam(value="phone") String phone,@RequestParam(value="newpwd") String newpwd) throws ControllerException {
		return appRegistService.updatepwd(phone,newpwd);
	}
	
	/**
	 * 注册--发送手机验证码  同时手机号重复性校验
	 * 请求方式	POST
	 * 请求地址	/online/validation/
	 * @param phoneNum
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/validation",method=RequestMethod.POST)
	@ResponseBody
	public String RegistvalidationCode(@RequestParam(value="phone") String phoneNum) throws ControllerException{
		return appRegistService.RegistvalidationCode(phoneNum);
		
	}
	
	/**
	 * 注册--提交注册信息
	 * 请求方式	POST
	 * 请求地址	/online/register/
	 * @param registModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public String register(@ModelAttribute() AppRegistModel registModel) throws ControllerException{
		return appRegistService.register(registModel);
	}
	
	/**
	 * 会员认证  上传身份证银行卡证件照片
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/Authentication",method=RequestMethod.POST)
	@ResponseBody
	public String Authentication() throws ControllerException{
		return null;
	}
	
	/**
	 * 
	 * 绑定已有工号
	 * @param JobNumOrIdCard  工号或者身份证
	 * @param jobNumPassword  工号原密码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/bindingJobNum",method=RequestMethod.POST)
	@ResponseBody
	public String BindingJobNum(@RequestParam(value="JobNumOrIdCard") String JobNumOrIdCard,@RequestParam(value="jobNumPassword") String jobNumPassword) throws ControllerException{
		return appRegistService.BindingJobNum(JobNumOrIdCard,jobNumPassword);
	}
	
	/**
	 * 未读消息个数提醒
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/newsnum",method=RequestMethod.POST)
	@ResponseBody
	public String newsnum() throws ControllerException{
		return null;
	}
	

	
	/**
	 * 首页  欢迎姓名
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getAgentName",method=RequestMethod.POST)
	@ResponseBody
	public String getAgentName(String token) throws ControllerException{
		return appRegistService.getAgentName(token);
	}
	

}
