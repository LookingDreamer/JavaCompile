/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.vo;
/**
 * 
 * @version 1.0
 * @author 温德彬
 * @time 2016年7月13日  下午2:10:29
 * @since JDK 1.7
 */
public class ZhangzbInsureVo {
    /**
     * 请求编号
     */
	private String requestNo;
	/**
	 * 保单号
	 */
	private String insureNo;
	
	/**
	 * 保额
	 */
	private String insureFee ;
	/**
	 * 可使用次数
	 */
	private String useNum;
	/**
	 * 支付时间
	 */
	private String payTime;
	/**
	 * 有效期开始
	 */
	private String validBegin;
	/**
	 * 有效期结束
	 */
	private String validEnd;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 关键字
	 */
	private String keywords;
	/**
	 * 服务对象
	 */
	private String serviceObj;
	/**
	 * 代理人信息
	 */
	private AgentInfoVo agentInfo; 
	/**
	 * 客户信息
	 */
	private CustomerInfoVo customerInfo;
	/**  
	 * 获取请求编号  
	 * @return requestNo 请求编号  
	 */
	public String getRequestNo() {
		return requestNo;
	}
	/**  
	 * 设置请求编号  
	 * @param requestNo 请求编号  
	 */
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	/**  
	 * 获取保单号  
	 * @return insureNo 保单号  
	 */
	public String getInsureNo() {
		return insureNo;
	}
	/**  
	 * 设置保单号  
	 * @param insureNo 保单号  
	 */
	public void setInsureNo(String insureNo) {
		this.insureNo = insureNo;
	}
	/**  
	 * 获取可使用次数  
	 * @return useNum 可使用次数  
	 */
	public String getUseNum() {
		return useNum;
	}
	/**  
	 * 设置可使用次数  
	 * @param useNum 可使用次数  
	 */
	public void setUseNum(String useNum) {
		this.useNum = useNum;
	}
	/**  
	 * 获取支付时间  
	 * @return payTime 支付时间  
	 */
	public String getPayTime() {
		return payTime;
	}
	/**  
	 * 设置支付时间  
	 * @param payTime 支付时间  
	 */
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	/**  
	 * 获取有效期开始  
	 * @return validBegin 有效期开始  
	 */
	public String getValidBegin() {
		return validBegin;
	}
	/**  
	 * 设置有效期开始  
	 * @param validBegin 有效期开始  
	 */
	public void setValidBegin(String validBegin) {
		this.validBegin = validBegin;
	}
	/**  
	 * 获取有效期结束  
	 * @return validEnd 有效期结束  
	 */
	public String getValidEnd() {
		return validEnd;
	}
	/**  
	 * 设置有效期结束  
	 * @param validEnd 有效期结束  
	 */
	public void setValidEnd(String validEnd) {
		this.validEnd = validEnd;
	}
	/**  
	 * 获取创建时间  
	 * @return createTime 创建时间  
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**  
	 * 设置创建时间  
	 * @param createTime 创建时间  
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**  
	 * 获取关键字  
	 * @return keywords 关键字  
	 */
	public String getKeywords() {
		return keywords;
	}
	/**  
	 * 设置关键字  
	 * @param keywords 关键字  
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**  
	 * 获取服务对象  
	 * @return serviceObj 服务对象  
	 */
	public String getServiceObj() {
		return serviceObj;
	}
	/**  
	 * 设置服务对象  
	 * @param serviceObj 服务对象  
	 */
	public void setServiceObj(String serviceObj) {
		this.serviceObj = serviceObj;
	}
	/**  
	 * 获取代理人信息  
	 * @return agentInfo 代理人信息  
	 */
	public AgentInfoVo getAgentInfo() {
		return agentInfo;
	}
	/**  
	 * 设置代理人信息  
	 * @param agentInfo 代理人信息  
	 */
	public void setAgentInfo(AgentInfoVo agentInfo) {
		this.agentInfo = agentInfo;
	}
	/**  
	 * 获取客户信息  
	 * @return customerInfo 客户信息  
	 */
	public CustomerInfoVo getCustomerInfo() {
		return customerInfo;
	}
	/**  
	 * 设置客户信息  
	 * @param customerInfo 客户信息  
	 */
	public void setCustomerInfo(CustomerInfoVo customerInfo) {
		this.customerInfo = customerInfo;
	}
	public String getInsureFee() {
		return insureFee;
	}
	public void setInsureFee(String insureFee) {
		this.insureFee = insureFee;
	}
	@Override
	public String toString() {
		return "ZhangzbInsureVo [requestNo=" + requestNo + ", insureNo="
				+ insureNo + ", insureFee=" + insureFee + ", useNum=" + useNum
				+ ", payTime=" + payTime + ", validBegin=" + validBegin
				+ ", validEnd=" + validEnd + ", createTime=" + createTime
				+ ", keywords=" + keywords + ", serviceObj=" + serviceObj
				+ ", agentInfo=" + agentInfo + ", customerInfo=" + customerInfo
				+ "]";
	}
	
	
}
