package com.zzb.mobile.model;

/*
 *作为INSBDeliveryMethodController中的getDeliveryInfo方法传递的参数类型 
 */
public class DeliveryMethodParam {
	/*
	 * 任务流程id
	 * 订单id
	 * 渠道ID
	 * 渠道用户ID
	 */
	private String processInstanceId;
	private String orderno;
	private String channelId;
	private String channelUserId;
	public DeliveryMethodParam(){}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelUserId() {
		return channelUserId;
	}

	public void setChannelUserId(String channelUserId) {
		this.channelUserId = channelUserId;
	}
}
