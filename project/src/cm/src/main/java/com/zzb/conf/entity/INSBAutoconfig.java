package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAutoconfig extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;


	/**
	 * 配置类型 01：报价配置 :02：核保配置:03：续保配置
	 */
	private String conftype;

	/**
	 * 1.EDI 2.精灵
	 */
	private String quotetype;

	/**
	 * 字典表codevalue
	 */
	private String codevalue;

	/**
	 * edi精灵规则id
	 */
	private String contentid;
	
	/**
	 * 机构id
	 */
	private String deptid;
	/**
	 * 供应商id
	 */
	private String providerid;
	
	

	public String getConftype() {
		return conftype;
	}

	public void setConftype(String conftype) {
		this.conftype = conftype;
	}

	public String getQuotetype() {
		return quotetype;
	}

	public void setQuotetype(String quotetype) {
		this.quotetype = quotetype;
	}

	public String getCodevalue() {
		return codevalue;
	}

	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	
}