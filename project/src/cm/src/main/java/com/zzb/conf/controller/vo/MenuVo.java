package com.zzb.conf.controller.vo;

public class MenuVo {

	private String id;
	private String activeUrl;
	private String roleCode;

	public MenuVo() {
	}

	public MenuVo(String id, String activeUrl, String roleCode) {
		super();
		this.id = id;
		this.activeUrl = activeUrl;
		this.roleCode = roleCode;
	}

	@Override
	public String toString() {
		return "MenuVo [id=" + id + ", activeUrl=" + activeUrl + ", roleCode=" + roleCode + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActiveUrl() {
		return activeUrl;
	}

	public void setActiveUrl(String activeUrl) {
		this.activeUrl = activeUrl;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

}
