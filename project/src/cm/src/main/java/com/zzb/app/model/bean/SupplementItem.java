package com.zzb.app.model.bean;

public class SupplementItem {
	/**
	 * 供应商ID
	 */
	private String supplierId;
	
	/**
	 * 网点ID
	 */
	private String deptId; 

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
}
