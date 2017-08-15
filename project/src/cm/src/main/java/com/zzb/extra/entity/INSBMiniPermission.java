package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBMiniPermission extends BaseEntity implements Identifiable {
	
	private static final long serialVersionUID = 1L;
	
	
	private String permissionname;
	
	private String perdesc;
	
	private String url;
	
	private String percode;
	
	private Integer perindex;

	public String getPermissionname() {
		return permissionname;
	}

	public void setPermissionname(String permissionname) {
		this.permissionname = permissionname;
	}

	public String getPerdesc() {
		return perdesc;
	}

	public void setPerdesc(String perdesc) {
		this.perdesc = perdesc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPercode() {
		return percode;
	}

	public void setPercode(String percode) {
		this.percode = percode;
	}

	public Integer getPerindex() {
		return perindex;
	}

	public void setPerindex(Integer perindex) {
		this.perindex = perindex;
	}

	
	
}
