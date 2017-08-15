package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSCRole extends BaseEntity implements Identifiable {

	private static final long serialVersionUID = 1L;
	private String rolecode;
	private String rolename;
	private String branchinnerCode;
	private String prop1;
	private String prop2;
	private String status;
	
	//状态表现值 0：停用，1：启用
	private String statusstr;
	
	
	
	public String getStatusstr() {
		return statusstr;
	}
	public void setStatusstr(String statusstr) {
		this.statusstr = statusstr;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getBranchinnerCode() {
		return branchinnerCode;
	}
	public void setBranchinnerCode(String branchinnerCode) {
		this.branchinnerCode = branchinnerCode;
	}
	public String getProp1() {
		return prop1;
	}
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}
	public String getProp2() {
		return prop2;
	}
	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "INSCRole [rolecode=" + rolecode + ", rolename=" + rolename
				+ ", branchinnerCode=" + branchinnerCode + ", prop1=" + prop1
				+ ", prop2=" + prop2 + ", status=" + status + ", statusstr="
				+ statusstr + "]";
	}
	
	
	
}
