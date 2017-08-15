package com.zzb.model;

public class OrderQueryModel {

	/**
	 * 被保人姓名
	 */
	private String insuredname;
	/**
	 * 供应商id
	 */
	private String providerid;

	private String providername;

	/**
	 * 车牌号码
	 */
	private String carnum;
	/**
	 * 业务跟踪号
	 */
	private String taskid;
	/**
	 * 代理人工号
	 */
	private String agentcode;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 订单状态
	 */
	private String orderstatus;
	/**
	 * 用户类型
	 */
	private String usertype;
	/**
	 * 业务完成关闭者（业管code  user表userCode）
	 */
	private String shutter;
	/**
	 * 任务创建开始时间
	 */
	private String startdate;
	/**
	 * 任务创建结束时间
	 */
	private String enddate;
	/**
	 * 车牌号，或者被保人姓名
	 */
	private String carnumorcname;
	/**
	 * 当前页
	 */
	private int currentPage = 0;
	/**
	 * 父节点code集合
	 */
	private String parentcodes;
	/**
	 * 订单支付询价号追踪号
	 */
	private String paymentTransaction;
	/**
	 * 机构code
	 */
	private String comcode;
	/**
	 * 用户code
	 */
	private String userCode;

	private String channelinnercode;

	private String channelName;

	private String miniagentcode;

	public String getMiniagentcode() {
		return miniagentcode;
	}

	public void setMiniagentcode(String miniagentcode) {
		this.miniagentcode = miniagentcode;
	}

	public String getChannelinnercode() {
		return channelinnercode;
	}

	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getCarnumorcname() {
		return carnumorcname;
	}
	public void setCarnumorcname(String carnumorcname) {
		this.carnumorcname = carnumorcname;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	public String getCarnum() {
		return carnum;
	}
	public void setCarnum(String carnum) {
		this.carnum = carnum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAgentcode() {
		return agentcode;
	}
	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getShutter() {
		return shutter;
	}
	public void setShutter(String shutter) {
		this.shutter = shutter;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getParentcodes() {
		return parentcodes;
	}
	public void setParentcodes(String parentcodes) {
		this.parentcodes = parentcodes;
	}
	public String getPaymentTransaction() {
		return paymentTransaction;
	}
	public void setPaymentTransaction(String paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}
	public String getComcode() {
		return comcode;
	}
	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getProvidername() {
		return providername;
	}

	public void setProvidername(String providername) {
		this.providername = providername;
	}
}
