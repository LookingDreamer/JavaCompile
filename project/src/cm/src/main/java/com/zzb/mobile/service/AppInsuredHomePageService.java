package com.zzb.mobile.service;

import com.zzb.mobile.model.CommonModel;

/**
 * @author yuzhang
 * 
 */
public interface AppInsuredHomePageService {
	/**
	 * 获取代理人已询价未投保的订单个数
	 * @param jobNum -代理人工号
	 * @return
	 */
	public CommonModel getTobePolicyNum(String userid);
	
	/**
	 * 获取xx代理人经办的已投保未支付的订单个数          (有保单号&&支付状态“未支付”)
	 * @param jobNum -代理人工号
	 * @return
	 */
	public CommonModel getTobePaymentOrderNum(String userid);
	/**
	 * 获取代理人xx代理过过的所有快到期未投保的保单个数     (快到续保)
	 * @param jobNum -代理人工号
	 * @return
	 */
	public CommonModel renewalRemindNum(String userid );
}
