package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCorecodemap extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String corecode;

	/**
	 * 
	 */
	private String corename;

	/**
	 * 
	 */
	private String cmcode;

	/**
	 * 
	 */
	private String cmname;

	/**
	 * 类型
	 */
	private String type;

	public String getCorecode() {
		return corecode;
	}

	public void setCorecode(String corecode) {
		this.corecode = corecode;
	}

	public String getCorename() {
		return corename;
	}

	public void setCorename(String corename) {
		this.corename = corename;
	}

	public String getCmcode() {
		return cmcode;
	}

	public void setCmcode(String cmcode) {
		this.cmcode = cmcode;
	}

	public String getCmname() {
		return cmname;
	}

	public void setCmname(String cmname) {
		this.cmname = cmname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}