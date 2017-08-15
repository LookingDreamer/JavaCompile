package com.zzb.mobile.model;
/*
 * 作为传递到INSBMyOrderController中的参数类型，完成调用
 * 它是将数据封装在这个类中来完成数据传递及调用的
 */
public class MyOrderParam {
	private String agentnum;               //代理人编号
	private String carlicenseno;           //车牌号
	private String insuredname;            //被保人姓名
	private String orderstatus;           //订单状态
	private int limit;                    //每页中的数量
	private long offset;                  //起始量
	private String taskcreatetimeup;
	private String taskcreatetimedown;
	private String purchaserid;
	private String purchaserchannel;

	public String getPurchaserid() {
		return purchaserid;
	}

	public void setPurchaserid(String purchaserid) {
		this.purchaserid = purchaserid;
	}

	public String getPurchaserchannel() {
		return purchaserchannel;
	}

	public void setPurchaserchannel(String purchaserchannel) {
		this.purchaserchannel = purchaserchannel;
	}

	public String getTaskcreatetimeup() {
		return taskcreatetimeup;
	}
	public void setTaskcreatetimeup(String taskcreatetimeup) {
		this.taskcreatetimeup = taskcreatetimeup;
	}
	public String getTaskcreatetimedown() {
		return taskcreatetimedown;
	}
	public void setTaskcreatetimedown(String taskcreatetimedown) {
		this.taskcreatetimedown = taskcreatetimedown;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getCarlicenseno() {
		return carlicenseno;
	}
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
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
	public MyOrderParam(){}
}
