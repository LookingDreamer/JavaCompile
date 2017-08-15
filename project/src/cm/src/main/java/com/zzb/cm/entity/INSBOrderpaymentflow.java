package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBOrderpaymentflow extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单表id
	 */
	private String orderid;

	/**
	 * 支付渠道ID
	 */
	private String paychannelid;

	/**
	 * 支付平台生成的交易跟踪号
	 */
	private String tradeno;

	/**
	 * 保险公司生成的支付跟踪号
	 */
	private String insurecono;

	/**
	 * 支付链接
	 */
	private String payurl;

	/**
	 * 手机支付链接
	 */
	private String paywapurl;

	/**
	 * 电脑支付链接
	 */
	private String paypcurl;

	/**
	 * 支付金额amount
	 */
	private Double amount;

	/**
	 * 支付完成时间
	 */
	private Date paytime;

	/**
	 * 支付结果
	 */
	private String payresult;

	/**
	 * 货币
	 */
	private String currencycode;

	/**
	 * 收款商户号
	 */
	private String merchantid;

	/**
	 * 收款商户名称
	 */
	private String merchantname;

	/**
	 * 支付方式是否允许修改allowChangePayStyle
	 */
	private String allowchangepaystyle;

	/**
	 * 支付手续费
	 */
	private Double paypoundage;

	/**
	 * 支付询价号
	 */
	private String paymentransaction;

	/**
	 * 支付流水号
	 */
	private String payflowno;

	/**
	 * 支付平台生成的订单号
	 */
	private String payorderno;

	/**
	 * 
	 */
	private String md5;

	/**
	 * 
	 */
	private String subinstanceid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getPaychannelid() {
		return paychannelid;
	}

	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getInsurecono() {
		return insurecono;
	}

	public void setInsurecono(String insurecono) {
		this.insurecono = insurecono;
	}

	public String getPayurl() {
		return payurl;
	}

	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}

	public String getPaywapurl() {
		return paywapurl;
	}

	public void setPaywapurl(String paywapurl) {
		this.paywapurl = paywapurl;
	}

	public String getPaypcurl() {
		return paypcurl;
	}

	public void setPaypcurl(String paypcurl) {
		this.paypcurl = paypcurl;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public String getPayresult() {
		return payresult;
	}

	public void setPayresult(String payresult) {
		this.payresult = payresult;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMerchantname() {
		return merchantname;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public String getAllowchangepaystyle() {
		return allowchangepaystyle;
	}

	public void setAllowchangepaystyle(String allowchangepaystyle) {
		this.allowchangepaystyle = allowchangepaystyle;
	}

	public Double getPaypoundage() {
		return paypoundage;
	}

	public void setPaypoundage(Double paypoundage) {
		this.paypoundage = paypoundage;
	}

	public String getPaymentransaction() {
		return paymentransaction;
	}

	public void setPaymentransaction(String paymentransaction) {
		this.paymentransaction = paymentransaction;
	}

	public String getPayflowno() {
		return payflowno;
	}

	public void setPayflowno(String payflowno) {
		this.payflowno = payflowno;
	}

	public String getPayorderno() {
		return payorderno;
	}

	public void setPayorderno(String payorderno) {
		this.payorderno = payorderno;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSubinstanceid() {
		return subinstanceid;
	}

	public void setSubinstanceid(String subinstanceid) {
		this.subinstanceid = subinstanceid;
	}

}