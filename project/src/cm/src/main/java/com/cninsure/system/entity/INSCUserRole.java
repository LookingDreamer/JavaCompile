package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

@SuppressWarnings("serial")
public class INSCUserRole extends BaseEntity implements Identifiable {

	/**
	 * 
	 */

	private String userid;
	private String roleid;
	private String status;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "INSCUserRole [userid=" + userid + ", roleid=" + roleid
				+ ", status=" + status + "]";
	}  

	
	
}
