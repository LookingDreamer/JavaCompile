package com.zzb.mobile.model;
/*
 * 作为传递到INSBMultiQuoteInfoController 中的参数，包含三个属性
 */
public class MultiQuoteInfoParam {
	private String processInstanceId;         //实际任务流程编号
	private String channelId;                  //渠道ID
	private String channelUserId;             //渠道用户ID
	private int limit;                        //每页显示数量
	private long offset;                      //起始量
	
	private String inscomcode;
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public MultiQuoteInfoParam(){}

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
