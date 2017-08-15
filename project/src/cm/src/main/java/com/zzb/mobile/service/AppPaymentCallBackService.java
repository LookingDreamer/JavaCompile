package com.zzb.mobile.service;

import com.zzb.mobile.entity.INSBPayResult;

public interface AppPaymentCallBackService{
	/**
	 *"bizId": "支付号(必须全局唯一)",
	 *"bizTransactionId": "支付流水号",
	 *"insSerialNo": "保险公司提供的支付流水号",
	 *"paySerialNo": "支付公司提供的支付流水号"
	 *"amount": "支付金额",
	 *"transDate": "交易日期:YYYY-MM-DD HH:mm:SS",
	 *"payResult": "支付结果",
	 *"orderState": "订单状态",
	 *"payResultDesc": "支付结果描述"
	 *"nonceStr": "随机字符串"
	 *"sign": "签名串"
	 * @return
	 */
	public String callBack(String bizId,String bizTransactionId,String insSerialNo,String paySerialNo,String amount,
			String transDate,String payResult,String orderState,String payResultDesc,String nonceStr,String sign);
	public INSBPayResult addPayResult(String bizId,String payFlowNo, String insSerialNo,
			String paySerialNo, String amount, String transDate,
			String payResult, String orderState, String payResultDesc,String nonceStr,String sign);
}
