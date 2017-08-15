package com.zzb.mobile.model;

import java.util.List;

public class InsuredConfigModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 商业险
	 */
	private List<BusinessRisks> businessRisks;
	/**
	 * 交强险
	 */
	private List<StrongRisk> strongRisk;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 投保车价
	 */
	private Double policycarprice;
	/**
	 * 被保人证件号
	 */
	private String certNumber;
	/**
	 * 选择的保险配置的key值
	 */
	private String plankey;
	/**
	 * 身份证正面照 id
	 */
	private String fileid;
	/**
	 * 证件类型
	 */
	private String certificateType;
	/**
	 *  新增设备险  新增设备
	 */
	private List<NewEquipmentInsBean> equipmentInsBeans;
	/**
	 * 修理期间费用补偿险 天数
	 */
	private String compensationdays;
	/**
	 * 商业险开始时间
	 */
	private String systartdate;
	/**
	 * 交强险开始时间
	 */
	private String jqstartdate;
	/**
	 * 备注类型 字典表 agentnoti
	 */
	private String remarkcode;
	
	public String getRemarkcode() {
		return remarkcode;
	}
	public void setRemarkcode(String remarkcode) {
		this.remarkcode = remarkcode;
	}
	public String getSystartdate() {
		return systartdate;
	}
	public void setSystartdate(String systartdate) {
		this.systartdate = systartdate;
	}
	public String getJqstartdate() {
		return jqstartdate;
	}
	public void setJqstartdate(String jqstartdate) {
		this.jqstartdate = jqstartdate;
	}
	public List<NewEquipmentInsBean> getEquipmentInsBeans() {
		return equipmentInsBeans;
	}
	public void setEquipmentInsBeans(List<NewEquipmentInsBean> equipmentInsBeans) {
		this.equipmentInsBeans = equipmentInsBeans;
	}
	public String getCompensationdays() {
		return compensationdays;
	}
	public void setCompensationdays(String compensationdays) {
		this.compensationdays = compensationdays;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getPlankey() {
		return plankey;
	}
	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}
	public String getCertNumber() {
		return certNumber;
	}
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}
	public Double getPolicycarprice() {
		return policycarprice;
	}
	public void setPolicycarprice(Double policycarprice) {
		this.policycarprice = policycarprice;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public List<BusinessRisks> getBusinessRisks() {
		return businessRisks;
	}
	public void setBusinessRisks(List<BusinessRisks> businessRisks) {
		this.businessRisks = businessRisks;
	}
	public List<StrongRisk> getStrongRisk() {
		return strongRisk;
	}
	public void setStrongRisk(List<StrongRisk> strongRisk) {
		this.strongRisk = strongRisk;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
