package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRegion extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 地区代码
	 */
	private String comcode;

	/**
	 * 地区名称
	 */
	private String comcodename;

	/**
	 * 上级id
	 */
	private String parentid;

	private String register;

	private String deptid;

	private String shortname;

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getComcodename() {
		return comcodename;
	}

	public void setComcodename(String comcodename) {
		this.comcodename = comcodename;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
}