package com.zzb.mobile.service;

import javax.servlet.http.HttpServletRequest;

import com.zzb.extra.controller.vo.TempjobnumMap2Jobnum;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.model.usercenter.CXReturnModel;

public interface AppLoginService {
	/**
	 * 用户登陆认证，输入用户名密码，自动加载用户权限等等。
	 * @param userCode -工号、手机号等等
	 * @param password -密码
	 * @return
	 */
	public CommonModel login(String userCode,String password,String openid, String clienttype);
	
	/**
	 * 懒掌柜引流登录
	 * @param userCode
	 * @param password
	 * @param openid
	 * @param lzgid
	 * @return
	 */
	public CommonModel loginFromLzg(String userCode,String password,String openid,String lzgid,String requirementid);
	
	public CommonModel loginByOpenId(String openid);

	public CommonModel loginByChannelTempjobnum(TempjobnumMap2Jobnum tempjobnumMap2Jobnum);
	
	/**
	 * 查找密码，输入账号自动发送验证码
	 * @param account -工号、手机号等
	 * @return
	 */
	public CommonModel findPassWord(String account);
	/**
	 * 校验码验证
	 * @param phone -手机号
	 * @param vercode -校验码
	 * @param uuid -验证图片uuid
	 * @return
	 */
	public CommonModel validationCode(HttpServletRequest request,String phone,String vercode,String uuid);
	/**
	 * 修改密码
	 * @param phoneNo -手机号
	 * @param newPassword -新密码
	 * @return
	 */
	public CommonModel updatePassWord(String phoneNo,String newPassword);
	/**
	 * 退出登陆
	 * @param token
	 * @return
	 */
	public CommonModel logout(String token);

	/**
	 * 懒掌柜登陆
	 * @param token 令牌
	 * @param account 账号
	 * @return
	 */
	public CommonModelforlzglogin lzgLogin(String token, String account);

	public CommonModel oauthLogin(String code);

	public CommonModel loginWithOutPwd(String account);
	public CommonModel userLoginGetKey(String account, String userid);
	public CommonModel userLogin(String account,String token);
	public CommonModel loginByAgentCodeForChn(String agentCode,String clienttype);
	public CommonModel loginByAgentCodeForLzg(String agentCode,String userCookie,String clienttype);
	public CommonModel loginForUserCenter(CXReturnModel cxReturnModel);
}
