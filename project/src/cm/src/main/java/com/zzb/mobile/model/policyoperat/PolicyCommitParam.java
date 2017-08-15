package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 提交投保操作接口使用
 */

public class PolicyCommitParam {

	/**
	 * 流程实例id
	 */
	private String processInstanceId;
	/**
	 * 保险公司code
	 */
	private String inscomcode;
	/**
	 * 代理人工号
	 */
	private String agentnum;
	/**
	 * 订单总金额（可以从提交页面直接获取）
	 */
	private double totalproductamount;
	/**
	 * 实付金额（可以从提交页面直接获取）
	 */
	private double totalpaymentamount;
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public double getTotalproductamount() {
		return totalproductamount;
	}
	public void setTotalproductamount(double totalproductamount) {
		this.totalproductamount = totalproductamount;
	}
	public double getTotalpaymentamount() {
		return totalpaymentamount;
	}
	public void setTotalpaymentamount(double totalpaymentamount) {
		this.totalpaymentamount = totalpaymentamount;
	}
}
