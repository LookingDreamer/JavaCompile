package com.zzb.mobile.model;

import java.util.Map;

import com.zzb.cm.entity.INSBInvoiceinfo;

/**
 * 投保信息bean
 * 
 * @author qiu
 *
 */
public class AppInsureinfoBean {
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 被保人
	 */
	private AppPerson passiveInsurePersion;
	/**
	 * 投保人
	 */
	private AppPerson insurePersion;
	/**
	 * 车主
	 */
	private AppPerson carowerinfo;
	/**
	 * 商业险期间
	 */
	private AppBusiness businessInsureddate;
	/**
	 * 交强期间
	 */
	private AppBusiness trafficInsureddate;
	/**
	 * 车辆信息
	 */
	private AppCarInfo carinfo;
	/**
	 * email
	 */
	private String email;
    /**
     * 电话
     */
    private String tel;
    /**
     * 上年保险公司id
     */
    private String lastComId;
    /**
     * 行驶区域
     */
    private String drivingRegion;
    /**
     * 当前操作标志，0 选择投保   1 核保退回修改信息 2 报价退回
     */
    private String flowflag;
    /**
     * 报价公司id
     */
    private String inscomcode;
    /**
     * 权益索赔人
     */
    private AppPerson legalrightclaim;
    /**
     * 发票信息
     */
    private INSBInvoiceinfo invoiceinfo; 
    /**
     * 备注类型
     */
    private Map<String, Object> remartsmap;
    /**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 备注类型 字典表 agentnoti
	 */
	private String remarkcode;
	
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
	public Map<String, Object> getRemartsmap() {
		return remartsmap;
	}
	public void setRemartsmap(Map<String, Object> remartsmap) {
		this.remartsmap = remartsmap;
	}
	public INSBInvoiceinfo getInvoiceinfo() {
		return invoiceinfo;
	}
	public void setInvoiceinfo(INSBInvoiceinfo invoiceinfo) {
		this.invoiceinfo = invoiceinfo;
	}
	public AppPerson getLegalrightclaim() {
		return legalrightclaim;
	}
	public void setLegalrightclaim(AppPerson legalrightclaim) {
		this.legalrightclaim = legalrightclaim;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getFlowflag() {
		return flowflag;
	}
	public void setFlowflag(String flowflag) {
		this.flowflag = flowflag;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public AppPerson getPassiveInsurePersion() {
		return passiveInsurePersion;
	}
	public void setPassiveInsurePersion(AppPerson passiveInsurePersion) {
		this.passiveInsurePersion = passiveInsurePersion;
	}
	public AppPerson getInsurePersion() {
		return insurePersion;
	}
	public void setInsurePersion(AppPerson insurePersion) {
		this.insurePersion = insurePersion;
	}
	public AppPerson getCarowerinfo() {
		return carowerinfo;
	}
	public void setCarowerinfo(AppPerson carowerinfo) {
		this.carowerinfo = carowerinfo;
	}
	public AppBusiness getBusinessInsureddate() {
		return businessInsureddate;
	}
	public void setBusinessInsureddate(AppBusiness businessInsureddate) {
		this.businessInsureddate = businessInsureddate;
	}
	public AppBusiness getTrafficInsureddate() {
		return trafficInsureddate;
	}
	public void setTrafficInsureddate(AppBusiness trafficInsureddate) {
		this.trafficInsureddate = trafficInsureddate;
	}
	public AppCarInfo getCarinfo() {
		return carinfo;
	}
	public void setCarinfo(AppCarInfo carinfo) {
		this.carinfo = carinfo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
