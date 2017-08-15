package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

@SuppressWarnings("serial")
public class INSCRoleMenu extends BaseEntity implements Identifiable {

	/**
	 * 
	 */

	private String roleid;  
	private String menuid;  
	private String status;
	
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "INSCRoleMenu [roleid=" + roleid + ", menuid=" + menuid
				+ ", status=" + status + "]";
	} 

}
