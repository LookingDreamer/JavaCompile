package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAutoconfigshow extends BaseEntity implements Identifiable {
	public static final transient String ELF_QUOTE_TYPE = "02";
	private static final long serialVersionUID = 1L;

	/**
	 * 配置类型 01：报价配置 :02：核保配置:03：续保配置.04:承保配置.05:平台
	 */
	private String conftype;

	/**
	 * 报价类型 01:EDI  02:精灵 
	 */
	private String quotetype;

	/**
	 * 字典表codevalue :processAuto
	 */
	private String codevalue;

	/**
	 * edi精灵规则id
	 */
	private String contentid;

	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 机构id
	 */
	private String deptid;

	public INSBAutoconfigshow() {
	}

	public INSBAutoconfigshow(String conftype, String quotetype, String codevalue, String contentid, String providerid, String deptid) {
		this.conftype = conftype;
		this.quotetype = quotetype;
		this.codevalue = codevalue;
		this.contentid = contentid;
		this.providerid = providerid;
		this.deptid = deptid;
	}

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

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Override
	public String toString() {
		return "INSBAutoconfigshow [conftype=" + conftype + ", quotetype="
				+ quotetype + ", codevalue=" + codevalue + ", contentid="
				+ contentid + ", providerid=" + providerid + ", deptid="
				+ deptid + "]";
	}
}