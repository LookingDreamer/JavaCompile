package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSHOrder extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单号
	 */
	private String orderno;

	/**
	 * 订单状态与insccode相关联
	 */
	private String orderstatus;

	/**
	 * 支付状态与insccode相关联
	 */
	private String paymentstatus;

	/**
	 * 配送状态与insccode相关联
	 */
	private String deliverystatus;

	/**
	 * 销售渠道与insccode相关联
	 */
	private String buyway;

	/**
	 * 该订单的代理人编码
	 */
	private String agentcode;

	/**
	 * 该订单的代理人姓名
	 */
	private String agentname;

	/**
	 * 录单人
	 */
	private String inputusercode;

	/**
	 * 币种编码
	 */
	private String currency;

	/**
	 * 配送费（总）
	 */
	private Double totaldeliveryamount;

	/**
	 * 实付金额：配送费 + 订单项实付总额
	 */
	private Double totalpaymentamount;

	/**
	 * 产品标价总金额
	 */
	private Double totalproductamount;

	/**
	 * 优惠金额：产品标价总金额 - (实付金额 - 配送费)
	 */
	private Double totalpromotionamount;

	/**
	 * 出单网点
	 */
	private String deptcode;

	/**
	 * 字典表
	 */
	private String paymentmethod;

	/**
	 * 供应商
	 */
	private String prvid;

	/**
	 * 精灵或edi，robot-精灵，edi-EDI
	 */
	private String fairyoredi;

	/**
	 * 流程节点，A-报价，B-核保回写，D-承保回写
	 */
	private String nodecode;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public String getDeliverystatus() {
		return deliverystatus;
	}

	public void setDeliverystatus(String deliverystatus) {
		this.deliverystatus = deliverystatus;
	}

	public String getBuyway() {
		return buyway;
	}

	public void setBuyway(String buyway) {
		this.buyway = buyway;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getInputusercode() {
		return inputusercode;
	}

	public void setInputusercode(String inputusercode) {
		this.inputusercode = inputusercode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTotaldeliveryamount() {
		return totaldeliveryamount;
	}

	public void setTotaldeliveryamount(Double totaldeliveryamount) {
		this.totaldeliveryamount = totaldeliveryamount;
	}

	public Double getTotalpaymentamount() {
		return totalpaymentamount;
	}

	public void setTotalpaymentamount(Double totalpaymentamount) {
		this.totalpaymentamount = totalpaymentamount;
	}

	public Double getTotalproductamount() {
		return totalproductamount;
	}

	public void setTotalproductamount(Double totalproductamount) {
		this.totalproductamount = totalproductamount;
	}

	public Double getTotalpromotionamount() {
		return totalpromotionamount;
	}

	public void setTotalpromotionamount(Double totalpromotionamount) {
		this.totalpromotionamount = totalpromotionamount;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getPaymentmethod() {
		return paymentmethod;
	}

	public void setPaymentmethod(String paymentmethod) {
		this.paymentmethod = paymentmethod;
	}

	public String getPrvid() {
		return prvid;
	}

	public void setPrvid(String prvid) {
		this.prvid = prvid;
	}

	public String getFairyoredi() {
		return fairyoredi;
	}

	public void setFairyoredi(String fairyoredi) {
		this.fairyoredi = fairyoredi;
	}

	public String getNodecode() {
		return nodecode;
	}

	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}

}