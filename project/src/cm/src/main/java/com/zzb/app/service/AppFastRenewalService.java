package com.zzb.app.service;

public interface AppFastRenewalService {

	/**
	 * 获取供应商列表
	 * @param needVals
	 * @return
	 */
	String getAllProviderList(String needVals);

	/**
	 * 保险配置信息
	 * @param needVals
	 * @return
	 */
	String updateInsureConfigure(String needVals);

	/**
	 * 驾驶人信息
	 * @param needVals
	 * @return
	 */
	String updateDriverInfo(String needVals);

	/**
	 * 支付和快递方式查询
	 * @param needVals
	 * @return
	 */
	String queryPayExpressinfo(String needVals);
	
	/**
	 * 收货地址查询
	 * @param needVals
	 * @return
	 */
	String queryShippingAddressinfo(String needVals);

	/**
	 * 获取任务id
	 * @param needVals
	 * @return
	 */
	String getTaskId(String needVals);

	/**
	 * 获取代理人操作权限
	 * @param mSM_PARAM
	 * @return
	 */
	String queryAgentPowerById(String needVals);

	/**
	 * 绑定代理人id与任务id
	 * @param mSM_PARAM
	 * @return
	 */
	String bindingTaskIdAndAgentId(String needVals);

	/**
	 * 绑定代理人与任务id
	 * @param mSM_PARAM
	 * @return
	 */
	String bindingTaskIdAndProviderIds(String needVals);

	/**
	 * 保存投保地区
	 * @param mSM_PARAM
	 * @return
	 */
	String saveInsuranceArea(String needVals);

	/**
	 * 保存投保日期
	 * @param mSM_PARAM
	 * @return
	 */
	String saveInsuranceDate(String needVals);

	/**
	 * 保存被保人投保人信息
	 * @param needVals
	 * @return
	 */
	String saveInsurancePeopleInfo(String needVals);

	/**
	 * 保存联系人信息
	 * @param mSM_PARAM
	 * @return
	 */
	String saveInsuranceContactsInfo(String needVals);

	/**
	 * 保存车主信息
	 * @param mSM_PARAM
	 * @return
	 */
	String saveDriverOwerInfo(String needVals);

	/**
	 * 保存车辆信息（行驶区域，平均行驶里程，上家商业承保公司ID） 
	 * @param mSM_PARAM
	 * @return
	 */
	String saveOtherCarInfo(String needVals);

	/**
	 * 保存车辆信息（基本信息） 
	 * @param needVals
	 * @return
	 */
	String saveBasicCarInfo(String needVals);

	/**
	 * 修改多方报价状态
	 * @param mSM_PARAM
	 * @return
	 */
	String updateQuoteStatus(String needVals);

	/**
	 * 获取保险配置信息
	 * @return
	 */
	String commonInsureConfig(String plankey);

	/**
	 * 根据选择保险公司获取补充信息交集
	 * @param taskid
	 * @return
	 */
	String supplementInfo(String taskid);

}
