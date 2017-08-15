package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.entity.INSBInsuredhis;

public interface INSBInsuredhisService extends BaseService<INSBInsuredhis> {
	/**
	 * CM支付或二支工作台申请验证码
	 * @param user
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public String cmApplyPin(INSCUser user, String taskid, String inscomcode);
	
	/**
	 * CM支付或二支工作台保存被保人身份证信息
	 */
	public String saveIDCardInfo(INSCUser user, String taskid, String inscomcode, String name, String idcardno,
			String sex, String nation, String birthday, String address, String authority, String expdate, String cellphone, String expstartdate, String expenddate, String email);
	
}