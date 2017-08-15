package com.zzb.mobile.service;

import java.util.Date;

import net.sf.json.JSONObject;

import com.zzb.chn.bean.PayInfo;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.mobile.model.CommonModel;

public interface AppPaymentService {
	/** 
	 * 支付
	 * @param payInfoJson
	 * @return
	 */
	public CommonModel pay(String jobNum,JSONObject payInfoJson); 
	
	/**
	 * 支付
	 * @param payInfoJson
	 * @return
	 */
	public CommonModel pay(String jobNum,JSONObject payInfoJson,String password,String cardInfoId);
	/**
	 * 
	 * @param jobNum
	 * @param payInfoJson
	 * @param password
	 * @return
	 */
	public CommonModel pay(String jobNum,JSONObject payInfoJson,String password);
	/**
	 * 
	 * @param bizId -流水号
	 * @param password -密码
	 * @return
	 */
	public CommonModel testPay(String bizId);

	/**
	 * 查询支付是否成功
	 * bizId -支付ID(全局唯一)
	 * @return
	 */
	public CommonModel queryPayResult(String bizId);
	
	
	/**
	 * 查询支付是否成功
	 * bizId -支付ID(全局唯一)
	 * @return
	 */
	public CommonModel queryPayResultFromPayPlat(String bizId);
	/**
	 * 获取支付方式
	 * @return
	 */
	public CommonModel getPaymentChannel();
	/**
	 * 获取支付银行信息
	 * @param jobNum
	 * @return
	 */
	public CommonModel getPaymentBankInfo(String jobNum);
	
	
	public CommonModel completeTask(String bizId);
	
	
	public INSBOrderpayment updateOrderPayment(String bizId,String payFlowNo,String payOrderNo,
			String paySerialNo, String payResult, Date trasDate) ;
	public void updateOrder(String bizId,String orderStatus,String paymentStatus);
	/**
	 * 
	 * @param bizId
	 * @param payResult 02成功/06支付中/05支付失败/
	 */
	public CommonModel updateOrderPayment(String bizId,String payResult);

	public CommonModel validateOrderPayment(String bizId);
 
	public String paySuccess(String bizId, String payType);

	public String channelPay(String taskId, String companyId);

	public PayInfo verify(PayInfo param);
	/**
	 * 渠道支付
	 * @param payInfo
	 * @return
	 */
	public PayInfo pay(PayInfo payInfo, QuoteBean quoteBean);
}
