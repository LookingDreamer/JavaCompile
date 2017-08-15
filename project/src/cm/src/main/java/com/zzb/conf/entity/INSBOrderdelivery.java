package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBOrderdelivery extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单id
	 */
	private String orderid;

	/**
	 * 出单网点
	 */
	private String deptcode;

	/**
	 * 收件人姓名
	 */
	private String recipientname;

	/**
	 * 收件人手机号
	 */
	private String recipientmobilephone;

	/**
	 * 邮编
	 */
	private String zip;

	/**
	 * 收件人省份
	 */
	private String recipientprovince;

	/**
	 * 收件人城市
	 */
	private String recipientcity;

	/**
	 * 收件人地区
	 */
	private String recipientarea;

	/**
	 * 收件人详细地址
	 */
	private String recipientaddress;

	/**
	 * 签收时间-工作日/休息日/任何时间
	 */
	private String receiveday;

	/**
	 * 签收时间-早/中/晚/全天候
	 */
	private String receivetime;

	/**
	 * 是否需要发票isInvoice
	 */
	private String isinvoice;

	/**
	 * 发票抬头
	 */
	private String invoicetitle;

	/**
	 * 配送方式
	 */
	private String delivetype;

	/**
	 * 是否保险公司配送
	 */
	private String isinsurecodelive;

	/**
	 * 是否到付
	 */
	private String isfreightcollect;

	/**
	 * 配送费
	 */
	private Double fee;

	/**
	 * 运单号traceNumber
	 */
	private String tracenumber;

	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 供应商名称
	 */
	private String providername;
	
	/**
	 * 配送方
	 */
	private String deliveryside;
	
	/**
	 * 物流公司
	 */
	private String logisticscompany;
	
	/**
	 * 配送地址表id
	 */
	private String deliveryaddressid;


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

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getRecipientname() {
		return recipientname;
	}

	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}

	public String getRecipientmobilephone() {
		return recipientmobilephone;
	}

	public void setRecipientmobilephone(String recipientmobilephone) {
		this.recipientmobilephone = recipientmobilephone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getRecipientprovince() {
		return recipientprovince;
	}

	public void setRecipientprovince(String recipientprovince) {
		this.recipientprovince = recipientprovince;
	}

	public String getRecipientcity() {
		return recipientcity;
	}

	public void setRecipientcity(String recipientcity) {
		this.recipientcity = recipientcity;
	}

	public String getRecipientarea() {
		return recipientarea;
	}

	public void setRecipientarea(String recipientarea) {
		this.recipientarea = recipientarea;
	}

	public String getRecipientaddress() {
		return recipientaddress;
	}

	public void setRecipientaddress(String recipientaddress) {
		this.recipientaddress = recipientaddress;
	}

	public String getReceiveday() {
		return receiveday;
	}

	public void setReceiveday(String receiveday) {
		this.receiveday = receiveday;
	}

	public String getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}

	public String getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(String isinvoice) {
		this.isinvoice = isinvoice;
	}

	public String getInvoicetitle() {
		return invoicetitle;
	}

	public void setInvoicetitle(String invoicetitle) {
		this.invoicetitle = invoicetitle;
	}

	public String getDelivetype() {
		return delivetype;
	}

	public void setDelivetype(String delivetype) {
		this.delivetype = delivetype;
	}

	public String getIsinsurecodelive() {
		return isinsurecodelive;
	}

	public void setIsinsurecodelive(String isinsurecodelive) {
		this.isinsurecodelive = isinsurecodelive;
	}

	public String getIsfreightcollect() {
		return isfreightcollect;
	}

	public void setIsfreightcollect(String isfreightcollect) {
		this.isfreightcollect = isfreightcollect;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getTracenumber() {
		return tracenumber;
	}

	public void setTracenumber(String tracenumber) {
		this.tracenumber = tracenumber;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getProvidername() {
		return providername;
	}

	public void setProvidername(String providername) {
		this.providername = providername;
	}

	public String getDeliveryside() {
		return deliveryside;
	}

	public void setDeliveryside(String deliveryside) {
		this.deliveryside = deliveryside;
	}

	public String getLogisticscompany() {
		return logisticscompany;
	}

	public void setLogisticscompany(String logisticscompany) {
		this.logisticscompany = logisticscompany;
	}

	public String getDeliveryaddressid() {
		return deliveryaddressid;
	}

	public void setDeliveryaddressid(String deliveryaddressid) {
		this.deliveryaddressid = deliveryaddressid;
	}

}