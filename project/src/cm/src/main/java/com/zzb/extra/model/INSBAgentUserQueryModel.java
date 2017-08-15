package com.zzb.extra.model;


import com.zzb.model.INSBAgentQueryModel;

public class INSBAgentUserQueryModel extends INSBAgentQueryModel {
	private String rolecode;
	/**
	 * 权限id
	 */
	private String rid;
	/**
	 * 权限名称
	 */
	private String rname;
	

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}
	
	
	
}
