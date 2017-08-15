package com.zzb.mobile.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBPayResult extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 支付号
	 */
	private String bizId;
	/**
	 * 支付流水号
	 */
	private String payflowno;
	/**
	 * 保险公司提供的支付流水号
	 */
	private String insSerialNo;
	/**
	 * 支付公司提供的支付流水号
	 */
	private String paySerialNo;
	/**
	 * 支付金额
	 */
	private Double amount;
	/**
	 * 交易日期
	 */
	private Date transDate;
	/**
	 *  0-正常 1-未推送 2 -已推送
	 */
	private Integer status;
	/**
	 * 支付结果
	 */
	private String payResult;
	/**
	 * 订单状态
	 */
	private String orderState;
	/**
	 * 支付结果描述
	 */
	private String payResultDesc;
	/**
	 * 随机字符串
	 */
	private String nonceStr;
	/**
	 * 签名串
	 */
	private String sign;
	
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPayflowno() {
		return payflowno;
	}
	public void setPayflowno(String payflowno) {
		this.payflowno = payflowno;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getInsSerialNo() {
		return insSerialNo;
	}
	public void setInsSerialNo(String insSerialNo) {
		this.insSerialNo = insSerialNo;
	}
	public String getPaySerialNo() {
		return paySerialNo;
	}
	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getPayResultDesc() {
		return payResultDesc;
	}
	public void setPayResultDesc(String payResultDesc) {
		this.payResultDesc = payResultDesc;
	}
	@Override
	public String toString() {
		return "INSBPayzzb [bizId=" + bizId + ", insSerialNo=" + insSerialNo
				+ ", paySerialNo=" + paySerialNo + ", amount=" + amount
				+ ", transDate=" + transDate + ", payResult=" + payResult
				+ ", orderState=" + orderState + ", payResultDesc="
				+ payResultDesc + "]";
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
}
