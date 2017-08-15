package com.zzb.cm.Interface.entity.cif.model;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class TaxpayerInfo extends BaseEntity implements Identifiable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String taxPersonCert;//证件号
	String personName;//姓名
	String policyno;//保单号
	String source;//来源
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTaxPersonCert() {
		return taxPersonCert;
	}
	public void setTaxPersonCert(String taxPersonCert) {
		this.taxPersonCert = taxPersonCert;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	
}
