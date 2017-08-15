package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBTaskprivilege extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String pcode;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 等级 1：系统，2：模块，3：功能
	 */
	private Integer level;

	/**
	 * 排序
	 */
	private Integer orderflag;

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getorderflag() {
		return orderflag;
	}

	public void setorderflag(Integer orderflag) {
		this.orderflag = orderflag;
	}

	@Override
	public String toString() {
		return "INSBTaskprivilege [pcode=" + pcode + ", code=" + code
				+ ", name=" + name + ", level=" + level + ", orderflag=" + orderflag
				+ "]";
	}


	
	
}