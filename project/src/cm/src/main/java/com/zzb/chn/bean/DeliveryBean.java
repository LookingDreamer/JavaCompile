package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DeliveryBean extends PersonBean {
	private String province;
	private String city;
	private String area;
	private String address;
	private String zip;
	private String receiveDay;
	private String receiveTime;
	private String isInvoice;
	private String invoiceTitle;
	private String noti;
	private String deliveryType; //1-快递 2-电子保单
	private String deliverySide;
	private String outDept;
	private String isFreightCollect;
	private String fee;
	private String expressNumber;
	private String expressCompanyId;
	private String expressCompanyName;
	private String expressNo;

	private String jpElecPolicyFilePath; //交强险电子保单地址
	private String syElecPolicyFilePath; //商业险电子保单地址
	
	public String getJpElecPolicyFilePath() {
		return jpElecPolicyFilePath;
	}

	public void setJpElecPolicyFilePath(String jpElecPolicyFilePath) {
		this.jpElecPolicyFilePath = jpElecPolicyFilePath;
	}

	public String getSyElecPolicyFilePath() {
		return syElecPolicyFilePath;
	}

	public void setSyElecPolicyFilePath(String syElecPolicyFilePath) {
		this.syElecPolicyFilePath = syElecPolicyFilePath;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getReceiveDay() {
		return receiveDay;
	}

	public void setReceiveDay(String receiveDay) {
		this.receiveDay = receiveDay;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}

	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDeliverySide() {
		return deliverySide;
	}

	public void setDeliverySide(String deliverySide) {
		this.deliverySide = deliverySide;
	}

	public String getIsFreightCollect() {
		return isFreightCollect;
	}

	public void setIsFreightCollect(String isFreightCollect) {
		this.isFreightCollect = isFreightCollect;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public String getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(String expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public String getOutDept() {
		return outDept;
	}

	public void setOutDept(String outDept) {
		this.outDept = outDept;
	}
	
}
