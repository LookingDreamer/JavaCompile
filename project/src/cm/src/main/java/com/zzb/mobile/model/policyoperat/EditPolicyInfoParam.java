package com.zzb.mobile.model.policyoperat;

import com.zzb.cm.entity.INSBInvoiceinfo;

import java.util.List;

/**
 * liu
 * 修改投保人信息接口使用
 */

public class EditPolicyInfoParam {

	/**
	 * 流程实例id
	 */
	private String processInstanceId;
	/**
	 * 保险公司code
	 */
	private String inscomcode;
	/**
	 * 操作员姓名
	 */
	private String operator;
	/**
	 * 车辆使用性质
	 */
	private String carproperty;
	/**
	 * 是否过户车
	 */
	private String isTransfercar;
	/**
	 * 车辆所属性质
	 */
	private String property;
	/**
	 * 车辆初登日期
	 */
	private String registdate;
	/**
	 * 商业险起始日期
	 */
	private String busstarttime;
	/**
	 * 商业险结束日期
	 */
	private String busendtime;
	/*
	 * 交强险起始日期
	 */
	private String strstarttime;
	/*
	 * 交强险结束日期
	 */
	private String strendtime;
	/**
	 * 被保人电话
	 */
	private String telephone;
	/**
	 * 被保人邮箱
	 */
	private String email;
	/**
	 * 备注信息
	 */
	private String remark;
	/**
	 * 备注类型 字典表 agentnoti
	 */
	private String remarkcode;
	/**
	 * 过户时间
	 */
	private String chgOwnerDate;
	/**
	  * 发票信息
	  */
	private INSBInvoiceinfo invoiceinfo;

	private List<String> supplyParam;

	public INSBInvoiceinfo getInvoiceinfo() {
		return invoiceinfo;
	}
	public void setInvoiceinfo(INSBInvoiceinfo invoiceinfo) {
		this.invoiceinfo = invoiceinfo;
	}
	public String getStrstarttime() {
		return strstarttime;
	}
	public void setStrstarttime(String strstarttime) {
		this.strstarttime = strstarttime;
	}
	public String getStrendtime() {
		return strendtime;
	}
	public void setStrendtime(String strendtime) {
		this.strendtime = strendtime;
	}
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCarproperty() {
		return carproperty;
	}
	public void setCarproperty(String carproperty) {
		this.carproperty = carproperty;
	}
	public String getIsTransfercar() {
		return isTransfercar;
	}
	public void setIsTransfercar(String isTransfercar) {
		this.isTransfercar = isTransfercar;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getRegistdate() {
		return registdate;
	}
	public void setRegistdate(String registdate) {
		this.registdate = registdate;
	}
	public String getBusstarttime() {
		return busstarttime;
	}
	public void setBusstarttime(String busstarttime) {
		this.busstarttime = busstarttime;
	}
	public String getBusendtime() {
		return busendtime;
	}
	public void setBusendtime(String busendtime) {
		this.busendtime = busendtime;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String editPolicyInfoParam() {
		return remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemarkcode() {
		return remarkcode;
	}
	public void setRemarkcode(String remarkcode) {
		this.remarkcode = remarkcode;
	}
	public String getChgOwnerDate() {
		return chgOwnerDate;
	}
	public void setChgOwnerDate(String chgOwnerDate) {
		this.chgOwnerDate = chgOwnerDate;
	}

	public List<String> getSupplyParam() {
		return supplyParam;
	}

	public void setSupplyParam(List<String> supplyParam) {
		this.supplyParam = supplyParam;
	}
}
