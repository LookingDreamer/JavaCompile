package com.zzb.mobile.service;

import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.mobile.entity.INSBPayResult;
import com.zzb.mobile.model.CallBackParam;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.PayParam;

import java.util.Date;

public interface INSBPaymentService {
	/**
	 * 支付
	 *
	 * @param payParam
	 * @return
	 */
	public CommonModel pay(PayParam payParam);

	/**
	 * 查询支付是否成功 bizId -支付ID(全局唯一)
	 *
	 * @return
	 */
	public CommonModel queryPayResult(String bizId);


	/**
	 * 精灵支付查询结果
	 * 查询支付是否成功 bizId -支付ID(全局唯一)
	 * sequence :第几次调查询支付结果接口,取值为0,1,2,3
	 * 0：表示非平安二维码的支付结果查询，1表示客户第一次点击“支付完成”第一次支付结果查询，2,3依次类推
	 * @return
	 */
	public CommonModel queryPayResult(String bizId,  Boolean isUnderwriteQuote);

	/**
	 * 查询支付是否成功 bizId -支付ID(全局唯一)
	 *
	 * @return
	 */
	public CommonModel queryPayResultFromPayPlat(String payFlowNo);

	public CommonModel completeTask(INSBOrder insbOrder, INSBOrderpayment insbOrderpayment);

	public String callBack(CallBackParam params);

	/**
	 * bug-1545 cm：392915 这个有效期过了，还可以支付。
	 * 当前是否过了报价有效期
	 * @param processInstanceId
	 * @param inscomcode
	 * @return
	 */
	public boolean isExpired(String processInstanceId, String inscomcode);

	/**
	 * 获取支付有效期
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public Date getPayTime(Object taskid, Object inscomcode);

}
