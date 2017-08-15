package com.zzb.cm.controller.vo;

/**
 * @author liuchao
 *
 */
public class OrderManageVO01 {
	/**
	 * 简单查询车牌号或被保人
	 */
	private String carlicensenoOrinsuredname;
	
	/**
	 * 任务状态
	 */
	private String taskstatus;
	
	/**
	 * 被保人姓名
	 */
	private String insuredname;
	
	/**
	 * 代理人姓名
	 */
	private String agentName;
	
	/**
	 * 用户类型
	 */
	private String usertype;
	
	/**
	 * 车牌号
	 */
	private String carlicenseno;
	
	/**
	 * 代理人工号
	 */
	private String agentNum;
	
	/**
	 * 保险公司code
	 */
	private String inscomcode;
	
	/**
	 * 代理人手机号
	 */
	private String agentphone;
	
	/**
	 * 主流程id
	 */
	private String maininstanceid;
	
	/**
	 * 出单网点
	 */
	private String deptcode;
	
	/**
	 * 时间下限
	 */
	private String taskcreatetimeup;
	
	/**
	 * 时间上限
	 */
	private String taskcreatetimedown;
	/**
	 * 柜台支付标记
	 */
	private String paymentMethod;
	/**
	 * 当前页码
	 */
	private Integer currentpage;
	/**
	 * 配送方式
	 */
	private String deliveryType;

	private String channelinnercode;

	public String getChannelinnercode() {
		return channelinnercode;
	}

	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}

	/**
	 * 每页条数
	 */
	private Integer limit;

	public String getCarlicensenoOrinsuredname() {
		return carlicensenoOrinsuredname;
	}

	public void setCarlicensenoOrinsuredname(String carlicensenoOrinsuredname) {
		this.carlicensenoOrinsuredname = carlicensenoOrinsuredname;
	}

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}

	public String getAgentNum() {
		return agentNum;
	}

	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getAgentphone() {
		return agentphone;
	}

	public void setAgentphone(String agentphone) {
		this.agentphone = agentphone;
	}

	public String getMaininstanceid() {
		return maininstanceid;
	}

	public void setMaininstanceid(String maininstanceid) {
		this.maininstanceid = maininstanceid;
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

	public Integer getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return "OrderManageVO01 [carlicensenoOrinsuredname=" + carlicensenoOrinsuredname + ", taskstatus=" + taskstatus
				+ ", insuredname=" + insuredname + ", agentName=" + agentName + ", usertype=" + usertype
				+ ", carlicenseno=" + carlicenseno + ", agentNum=" + agentNum + ", inscomcode=" + inscomcode
				+ ", agentphone=" + agentphone + ", maininstanceid=" + maininstanceid + ", deptcode=" + deptcode
				+ ", taskcreatetimeup=" + taskcreatetimeup + ", taskcreatetimedown=" + taskcreatetimedown
				+ ", paymentMethod=" + paymentMethod + ", currentpage=" + currentpage + ", deliveryType=" + deliveryType
				+ ", channelinnercode=" + channelinnercode + ", limit=" + limit + "]";
	}


    
}
