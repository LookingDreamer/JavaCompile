package com.zzb.mobile.model;
/*
 * 作为作为INSBDeliveryMethodController中的addNewAddress方法 传递的参数类型 
 */
public class DeliveryAddressInfoParam {
	private String recipientname;
	/*
	 * 姓名
	 */
	private String recipientmobilephone;
	/*
	 * 联系电话
	 */
	private String province;
	/*
	 * 省
	 */
	private String city;
	/*
	 * 市
	 */
	private String area;
	/*
	 * 区
	 */
	private String recipientaddress;
	/*
	 * 详细地址
	 */
	private String zipCode;
	/*
	 * 邮编
	 */
	private String processInstanceId;
	/*
	 * 实例流程id
	 */
	private String agentnum;
	/*
	 * 操作员
	 */
	private String addressId;
	/*
	 * 在deliveryaddress表中的id，标记新地址的编号
	 */
	private String orderno;
	/*
	 * 订单号
	 */
	private String delivetytype;
	/*
	 * 配送方式
	 */
	private String channelId;
	/**
	 * 渠道ID
	 */
	private String channelUserId;

	/**
	 * 渠道用户ID
	 */
	public DeliveryAddressInfoParam(){}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
	public String getRecipientname() {
		return recipientname;
	}
	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}
	public String getRecipientmobilephone() {
		return recipientmobilephone;
	}
	public void setRecipientmobilephone(String recipientmobilephone) {
		this.recipientmobilephone = recipientmobilephone;
	}
	public String getRecipientaddress() {
		return recipientaddress;
	}
	public void setRecipientaddress(String recipientaddress) {
		this.recipientaddress = recipientaddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getDelivetytype() {
		return delivetytype;
	}
	public void setDelivetytype(String delivetytype) {
		this.delivetytype = delivetytype;
	}

	public String getChannelUserId() {
		return channelUserId;
	}

	public void setChannelUserId(String channelUserId) {
		this.channelUserId = channelUserId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
