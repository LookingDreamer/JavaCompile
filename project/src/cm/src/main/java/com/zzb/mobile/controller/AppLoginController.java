package com.zzb.mobile.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.*;
import com.zzb.mobile.model.usercenter.CXReturnModel;
import com.zzb.mobile.service.AppLoginService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/mobile/login/*")
public class AppLoginController extends BaseController{
	
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppLoginService appLoginService;
	
	@Resource
    private HttpServletResponse response;

	@Resource
    private HttpServletRequest request;
	/**
	 * 接口简单说明
	 * 请求方式	POST
	 * 请求地址	/mobile/login/login/
	 * @param username  用户名
	 * @param password 	用户密码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel login(@RequestBody LoginPram loginPram, @RequestHeader(value = "X-CLIENT-TYPE", required = false) String clienttype) throws ControllerException{
		if (StringUtils.isEmpty(loginPram.getClienttype())) {
			loginPram.setClienttype(clienttype);
		}

		CommonModel model = appLoginService.login(loginPram.getAccount(),loginPram.getPassword(),loginPram.getOpenid(), loginPram.getClienttype());
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	/**
	 * 懒掌柜引流登录
	 * 请求方式	POST
	 * 请求地址	/mobile/login/loginlzg/
	 * @param username  用户名
	 * @param password 	用户密码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/loginlzg",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel loginlzg(@RequestBody LoginPram loginPram) throws ControllerException{
		CommonModel model = appLoginService.loginFromLzg(loginPram.getAccount(),loginPram.getPassword(),
				loginPram.getOpenid(),loginPram.getLzgid(),loginPram.getRequirementid());
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	/**
	 * 接口简单说明
	 * 请求方式	POST
	 * 请求地址	/mobile/login/loginByOpenid/
	 * @param loginByOpenid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/loginByOpenid",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel loginByOpenid(@RequestBody LoginPram loginPram) throws ControllerException{
		CommonModel model = appLoginService.loginByOpenId(loginPram.getOpenid());
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	/**
	* 接口简单说明
	* 请求方式	POST
	* 请求地址	/mobile/login/logout/
	 * @param token
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel logout(@RequestBody UserParam token) throws ControllerException{
		
		return appLoginService.logout(token.getToken());
	}
	/**
	 * 查找密码 传入手机号或者工号，自动发送验证码
	 * 请求方式	POST
	 * 请求地址	/mobile/login/findPassWord
	 * @param account -手机号/工号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/findPassWord",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel findPassWord(@RequestBody UserParam account) throws ControllerException{
		return appLoginService.findPassWord(account.getAccount());
	}
	/**
	 * 查找密码 传入手机号或者工号，自动发送验证码
	 * 请求地址	/mobile/login/validateCode
	 * @param account -手机号/工号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/validateCode",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel validateCode(@RequestBody ValidateCodeParam codeParam) throws ControllerException{
		return appLoginService.validationCode(request,codeParam.getPhone(),codeParam.getCode(),codeParam.getUuid());
	}
	/**
	 * 修改密码 
	 * 请求地址	/mobile/login/changePassword
	 * @param account -手机号/工号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/updatePassWord",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updatePassWord(@RequestBody ModifyPasswdParam passwdParam) throws ControllerException{
		return appLoginService.updatePassWord(passwdParam.getPhone(),passwdParam.getNewPassword());
	}
	
	/**
	 * 登陆状态返回 
	 * 请求地址	/mobile/login/loginstatus
	 * @param status 状态码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/loginstatus",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel loginStatus(@RequestParam(value="status") String status) throws ControllerException{
		CommonModel commonModel = new CommonModel();
		commonModel.setStatus(status);
		if("1000".equals(status)){
			commonModel.setMessage("登录失败了");
		}else if("2000".equals(status)){
			commonModel.setMessage("请重新登录");
		}else if("3000".equals(status)){
			commonModel.setMessage("超时了，重新登录");
		}else{
			commonModel.setMessage("非法请求");
		}
		return commonModel;
	}
	/**
	 * 懒掌柜登陆
	 * 请求地址	/mobile/login/lzglogin
	 * @param token 令牌
	 * @param account 账号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/lzglogin",method=RequestMethod.POST)
	@ResponseBody
	public CommonModelforlzglogin lzgLogin(@RequestBody UserParam account) throws ControllerException{
		return appLoginService.lzgLogin(account.getToken(),account.getAccount());
	}
	
	/**
	 * 快速登陆
	 * 请求地址	/mobile/login/oauthLogin
	 * @param token 令牌
	 * @param account 账号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/oauthLogin",method=RequestMethod.GET)
	@ResponseBody
	public CommonModel oauthLogin(@RequestParam(value="code") String code) throws ControllerException{
		return appLoginService.oauthLogin(code);
	}
	@RequestMapping(value="/userLogin",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel userLogin(@RequestBody UserParam param) throws ControllerException{
		LogUtil.info("userLogin:" + JsonUtil.getJsonString(param));
		return appLoginService.userLogin(param.getAccount(),param.getToken());
	}

	@RequestMapping(value="/illegalInput")
	@ResponseBody
	public CommonModel illegalInput() throws ControllerException{
		CommonModel commonModel = new CommonModel();
		commonModel.setStatus(CommonModel.STATUS_FAIL);
		commonModel.setMessage("非法输入");
		return commonModel;
	}

	/**
	 * 接口简单说明
	 * 请求方式	POST
	 * 请求地址	/mobile/login/loginByAgentCodeForChn/
	 * @param account  用户名
	 * @param clienttype  客户端类型
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/loginByAgentCodeForChn",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel loginByAgentCodeForChn(@RequestBody LoginPram loginPram, @RequestHeader(value = "X-CLIENT-TYPE", required = false) String clienttype) throws ControllerException{
		if (StringUtils.isEmpty(loginPram.getClienttype())) {
			loginPram.setClienttype(clienttype);
		}

		CommonModel model = appLoginService.loginByAgentCodeForChn(loginPram.getAccount(), loginPram.getClienttype());
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}

	/**
	 * 接口简单说明 统一用户中心请求
	 * 请求方式	POST
	 * 请求地址	/mobile/login/loginForUserCenter/
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/loginForUserCenter",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel loginForUserCenter(@RequestBody CXReturnModel data, @RequestHeader(value = "X-CLIENT-TYPE", required = false) String clienttype) throws ControllerException{
		CommonModel model = appLoginService.loginForUserCenter(data);
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	
}
