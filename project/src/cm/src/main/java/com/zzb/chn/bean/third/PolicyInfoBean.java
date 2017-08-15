package com.zzb.chn.bean.third;

public class PolicyInfoBean {
	private String policyIds;
	private Integer supplierId;
	private String supplierName;
	private Double sumBIIntegral;
	private Double sumCIIntegral;
	private String prvAccountName;
	
	public String getPrvAccountName() {
		return prvAccountName;
	}
	public void setPrvAccountName(String prvAccountName) {
		this.prvAccountName = prvAccountName;
	}
	public String getPolicyIds() {
		return policyIds;
	}
	public void setPolicyIds(String policyIds) {
		this.policyIds = policyIds;
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
	public Double getSumBIIntegral() {
		return sumBIIntegral;
	}
	public void setSumBIIntegral(Double sumBIIntegral) {
		this.sumBIIntegral = sumBIIntegral;
	}
	public Double getSumCIIntegral() {
		return sumCIIntegral;
	}
	public void setSumCIIntegral(Double sumCIIntegral) {
		this.sumCIIntegral = sumCIIntegral;
	}
	
}
