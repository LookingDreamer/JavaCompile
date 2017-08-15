package com.zzb.app.service;

public interface AppOrderInfoService {
	
	/**
	 * 获取“多方报价基本信息”
	 * @param multiQuoteId（多方报价ID） 
	 * @return
	 */
	public String getMultiQuoteInfo(String multiQuoteId);
	

	/**
	 * 获取“多方报价信息订单” 分页查询1
	 * 根据订单状态筛选订单时会发此请求
	 * @param uuid
	 * @param licensePlate
	 * @param max
	 * @param offset
	 * @param insuredName
	 * @param status
	 * @param dateCreatedStart
	 * @return
	 */
	public String getMultiQuoteInstanceList(String uuid, String licensePlate
			, String max
			, String offset
			, String insuredName
			, String status
			, String dateCreatedStart);
	
	/**
	 * 获取“多方报价信息订单” 分页查询2
	 * 点击订单，查看订单详情（初审中、初审退回、关闭状态）时会发此请求
	 * @param uuid
	 * @param multiQuoteId
	 * @param licensePlate
	 * @return
	 */
	public String getMultiQuoteInstanceList(String uuid, String multiQuoteId, String licensePlate);
	
	
	/**
	 * 更新出单网点信息
	 * @param enquiryId
	 * @param content
	 * @return
	 */
	public String updateDept(String enquiryId, String content);
	
	
	/**
	 * 保存投保人信息
	 * @param multiQuoteId
	 * @return
	 */
	public String saveApplicant(String multiQuoteId);
	
	/**
	 * unknown
	 * @param multiQuoteId
	 * @return
	 */
	public String getPaymentList(String payMethod, String bizTransactionId);
	
}
