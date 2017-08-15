package com.zzb.chn.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBYoubaolianprvmap extends BaseEntity implements Identifiable { 
	private static final long serialVersionUID = 1L;
 
	private String prvId;
	private Integer supplierId;
	private String supplierName;
	private String prvAccountName;
	private String outPurchaseOrgId;
	private String outPurchaseOrgName;
	
	public String getOutPurchaseOrgId() {
		return outPurchaseOrgId;
	}
	public void setOutPurchaseOrgId(String outPurchaseOrgId) {
		this.outPurchaseOrgId = outPurchaseOrgId;
	}
	public String getOutPurchaseOrgName() {
		return outPurchaseOrgName;
	}
	public void setOutPurchaseOrgName(String outPurchaseOrgName) {
		this.outPurchaseOrgName = outPurchaseOrgName;
	}
	public String getPrvId() {
		return prvId;
	}
	public void setPrvId(String prvId) {
		this.prvId = prvId;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getPrvAccountName() {
		return prvAccountName;
	}
	public void setPrvAccountName(String prvAccountName) {
		this.prvAccountName = prvAccountName;
	}
	
}