package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBPermission extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 权限名称
	 */
	private String permissionname;

	/**
	 * 路径
	 */
	private String permissionpath;
	
	/**
	 *是否为试用权限 1 试用  2 正常 
	 */
	private Integer istry;

	/**
	 * 权限唯一标识code
	 */
	private String percode;
	public String getPermissionname() {
		return permissionname;
	}

	public void setPermissionname(String permissionname) {
		this.permissionname = permissionname;
	}

	public String getPermissionpath() {
		return permissionpath;
	}

	public void setPermissionpath(String permissionpath) {
		this.permissionpath = permissionpath;
	}
	
	

	public Integer getIstry() {
		return istry;
	}

	public void setIstry(Integer istry) {
		this.istry = istry;
	}

	public String getPercode() {
		return percode;
	}
	
	public void setPercode(String percode) {
		this.percode = percode;
	}

	@Override
	public String toString() {
		return "INSBPermission [permissionname=" + permissionname
				+ ", permissionpath=" + permissionpath + ", istry=" + istry
				+ ", percode=" + percode + "]";
	}
}