package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.Insbpaymentpassword;
import com.zzb.mobile.model.CommonModel;

public interface InsbpaymentpasswordService extends BaseService<Insbpaymentpassword> {

	/**
	 * 登录密码验证
	 */
	public CommonModel passwordValiadate(String jobNum,String logpwd);
		
   /**
    * 支付密码设置
    */
	public CommonModel paypwdSetting(String jobNum,String paypwd);
	
	
	   /**
	    * 支付密码设置
	    */
	public CommonModel hasPayPassword(String jobNum);	

}
