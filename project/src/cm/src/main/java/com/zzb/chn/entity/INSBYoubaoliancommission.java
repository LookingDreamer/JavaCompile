package com.zzb.chn.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBYoubaoliancommission extends BaseEntity implements Identifiable { 
	private static final long serialVersionUID = 1L;
 
	private String checkedId;
	private Integer checkedState;
	private Boolean ret;
	private Integer code;
	private String msg;
	private Float salesmanSettleFee;
	private Float platformSettleFee;
	private Float supplierSettleFee;
	private String supplierAccount;
	private String platformAccount;
	private Float biSettlePoint;
	private Float ciSettlePoint;
	private Float vvTaxSettlePoint;
	private String taskId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCheckedId() {
		return checkedId;
	}
	public void setCheckedId(String checkedId) {
		this.checkedId = checkedId;
	}
	public Integer getCheckedState() {
		return checkedState;
	}
	public void setCheckedState(Integer checkedState) {
		this.checkedState = checkedState;
	}
	public Boolean getRet() {
		return ret;
	}
	public void setRet(Boolean ret) {
		this.ret = ret;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Float getSalesmanSettleFee() {
		return salesmanSettleFee;
	}
	public void setSalesmanSettleFee(Float salesmanSettleFee) {
		this.salesmanSettleFee = salesmanSettleFee;
	}
	public Float getPlatformSettleFee() {
		return platformSettleFee;
	}
	public void setPlatformSettleFee(Float platformSettleFee) {
		this.platformSettleFee = platformSettleFee;
	}
	public Float getSupplierSettleFee() {
		return supplierSettleFee;
	}
	public void setSupplierSettleFee(Float supplierSettleFee) {
		this.supplierSettleFee = supplierSettleFee;
	}
	public String getSupplierAccount() {
		return supplierAccount;
	}
	public void setSupplierAccount(String supplierAccount) {
		this.supplierAccount = supplierAccount;
	}
	public String getPlatformAccount() {
		return platformAccount;
	}
	public void setPlatformAccount(String platformAccount) {
		this.platformAccount = platformAccount;
	}
	public Float getBiSettlePoint() {
		return biSettlePoint;
	}
	public void setBiSettlePoint(Float biSettlePoint) {
		this.biSettlePoint = biSettlePoint;
	}
	public Float getCiSettlePoint() {
		return ciSettlePoint;
	}
	public void setCiSettlePoint(Float ciSettlePoint) {
		this.ciSettlePoint = ciSettlePoint;
	}
	public Float getVvTaxSettlePoint() {
		return vvTaxSettlePoint;
	}
	public void setVvTaxSettlePoint(Float vvTaxSettlePoint) {
		this.vvTaxSettlePoint = vvTaxSettlePoint;
	}
	
}