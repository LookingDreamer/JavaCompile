package com.zzb.chn.service;

import com.zzb.chn.bean.PayInfo;
import com.zzb.chn.bean.QuoteBean;

public interface CHNPaymentService {
	String CHANNEL_PAYMENT_MODULE = "cm:zzb:channel:payment";
	int PAYURL_VALIDTIME_SECONDS = 900; //15分钟

	PayInfo verify(PayInfo payInfo);
	/**
	 * 查询支付结果
	 * @param bizTransactionId
	 * @return
	 */
	PayInfo queryResult(String bizTransactionId);
	/**
	 * 支付
	 * @param payInfo
	 * @param quoteBean
	 * @return
	 */
	PayInfo pay(PayInfo payInfo, QuoteBean quoteBean);
	QuoteBean callOk(QuoteBean quoteBean) throws Exception;
	/**
	 * 退款
	 * 核保失败时执行全额退款
	 * 核保成功待补差额时执行全额退款
	 * 核保成功可退差额时执行部分退款
	 * @param quoteBean
	 * @return
	 */
	public String refund(QuoteBean quoteBean);
	/**
	 * 支付结果回调
	 * 如果是报价后的支付结果回调并且已全额付清，则推核保工作流
	 * 如果是核保后的支付结果回调并且已全额付清，则推支付工作流
	 * @param params
	 * @return
	 */
	public String callback(PayInfo params);
	
	//新流程渠道垫付推核保接口
	public QuoteBean channelPayPolicy(QuoteBean quoteBean) throws Exception;
	//提供给cm验证码成功的时候调用去推新流程的二支
	public boolean chnPinCodeSecondPay(String taskId, String prvId) throws Exception;
} 
 