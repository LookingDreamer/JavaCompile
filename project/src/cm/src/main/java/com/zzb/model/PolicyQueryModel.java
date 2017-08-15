package com.zzb.model;

import java.util.List;

public class PolicyQueryModel {

	/**
	 * 保单id
	 */
	private String policyid;
	/**
	 * 订单号
	 */
	private String orderid;
	/**
	 * 车牌号
	 */
	private String carnum;
	/**
	 * 客户姓名
	 */
	private String custname;
	/**
	 * 所属机构id
	 */
	private String deptid;
	
	private List<String> deptids;
	/**
	 * 供应商id
	 */
	private String providerid;
	/**
	 * 保单状态
	 */
	private String policystatus;

	/**
	 * 任务创建开始时间
	 */
	private String startdate;
	
	/**
	 * 任务创建结束时间
	 */
	private String enddate;

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	private String agentnum;
	private String agentname;


	public String getAgentnum() {
		return agentnum;
	}

	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}

	public String getPolicyid() {
		return policyid;
	}
	public void setPolicyid(String policyid) {
		this.policyid = policyid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getCarnum() {
		return carnum;
	}
	public void setCarnum(String carnum) {
		this.carnum = carnum;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	public String getPolicystatus() {
		return policystatus;
	}
	public void setPolicystatus(String policystatus) {
		this.policystatus = policystatus;
	}

	public List<String> getDeptids() {
		return deptids;
	}

	public void setDeptids(List<String> deptids) {
		this.deptids = deptids;
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
	
}
