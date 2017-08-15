package com.zzb.cm.Interface.entity.cif.model;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class Insured extends BaseEntity implements Identifiable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String insurename;//被保人姓名
	String insureno;//被保人证件号
	String insurecertificate;//被保人证件类型
	String policyno;//保单号
	String insuremobile;//被保人手机号
	String vehicleno;//车牌号
	String insured_source;//来源
	
	public String getInsured_source() {
		return insured_source;
	}
	public void setInsured_source(String insured_source) {
		this.insured_source = insured_source;
	}
	public String getInsurename() {
		return insurename;
	}
	public void setInsurename(String insurename) {
		this.insurename = insurename;
	}
	public String getInsureno() {
		return insureno;
	}
	public void setInsureno(String insureno) {
		this.insureno = insureno;
	}
	public String getInsurecertificate() {
		return insurecertificate;
	}
	public void setInsurecertificate(String insurecertificate) {
		this.insurecertificate = insurecertificate;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getInsuremobile() {
		return insuremobile;
	}
	public void setInsuremobile(String insuremobile) {
		this.insuremobile = insuremobile;
	}
	

}
