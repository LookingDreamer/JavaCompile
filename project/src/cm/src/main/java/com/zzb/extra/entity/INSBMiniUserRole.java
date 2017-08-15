package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBMiniUserRole extends BaseEntity implements Identifiable {
	
	private static final long serialVersionUID = 1L;
	
	private String miniuserid;
	
	private String roleid;

	public String getMiniuserid() {
		return miniuserid;
	}

	public void setMiniuserid(String miniuserid) {
		this.miniuserid = miniuserid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	
	
	
}
