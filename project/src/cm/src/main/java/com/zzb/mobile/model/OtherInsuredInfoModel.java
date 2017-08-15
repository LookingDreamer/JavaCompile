package com.zzb.mobile.model;

import java.util.List;

public class OtherInsuredInfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 上家商业承保公司ID
	 */
	private String lastComId;
	/**
	 * 行驶区域, 1:出入境、0:境内、2:省内、3:场内、4:固定线路
	 */
	private String drivingRegion;
	/**
	 * 补充信息
	 */
	private List<SupplementInfo> supplementInfos;
	
	/**
	 * 被保人姓名
	 */
	private String name;
	/**
	 * 被保人证件类型
	 */
	private String certificateType;
	/**
	 * 被保人证件号码
	 */
	private String certNumber;
	/**
	 * 与被保人一致 0 一致 1 不一致
	 */
	private String sameinsured;
	/**
	 * 车主证件是否与被保人一致 0 一致 1 不一致
	 */
	private String cardnum;
	/**
	 * 商业险开始时间
	 */
	private String sriskstartdate;
	/**
	 * 商业险结束时间
	 */
	private String sriskenddate;
	/**
	 * 交强险开始时间
	 */
	private String jriskstartdate;
	/**
	 * 交强险结束时间
	 */
	private String jriskenddate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertNumber() {
		return certNumber;
	}
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}
	public String getSameinsured() {
		return sameinsured;
	}
	public void setSameinsured(String sameinsured) {
		this.sameinsured = sameinsured;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getSriskstartdate() {
		return sriskstartdate;
	}
	public void setSriskstartdate(String sriskstartdate) {
		this.sriskstartdate = sriskstartdate;
	}
	public String getSriskenddate() {
		return sriskenddate;
	}
	public void setSriskenddate(String sriskenddate) {
		this.sriskenddate = sriskenddate;
	}
	public String getJriskstartdate() {
		return jriskstartdate;
	}
	public void setJriskstartdate(String jriskstartdate) {
		this.jriskstartdate = jriskstartdate;
	}
	public String getJriskenddate() {
		return jriskenddate;
	}
	public void setJriskenddate(String jriskenddate) {
		this.jriskenddate = jriskenddate;
	}
	public List<SupplementInfo> getSupplementInfos() {
		return supplementInfos;
	}
	public void setSupplementInfos(List<SupplementInfo> supplementInfos) {
		this.supplementInfos = supplementInfos;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastComId() {
		return lastComId;
	}
	public void setLastComId(String lastComId) {
		this.lastComId = lastComId;
	}
	public String getDrivingRegion() {
		return drivingRegion;
	}
	public void setDrivingRegion(String drivingRegion) {
		this.drivingRegion = drivingRegion;
	}
	
}
