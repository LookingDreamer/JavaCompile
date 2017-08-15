package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBConditionParams extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数名称
	 */
	private String paramname;

	/**
	 * 参数中文名称
	 */
	private String paramcnname;

	/**
	 * 参数标签
	 */
	private String paramtag;

	/**
	 * 数据类型
	 */
	private String datatype;

	/**
	 * 是否有默认值
	 */
	private String isdefault;

	/**
	 * 默认值
	 */
	private String defaultvalue;

	/**
	 * 查询脚本名称
	 */
	private String sqlname;

	/**
	 * 字段名称
	 */
	private String fieldname;

	/**
	 * 状态
	 */
	private String status;

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getParamcnname() {
		return paramcnname;
	}

	public void setParamcnname(String paramcnname) {
		this.paramcnname = paramcnname;
	}

	public String getParamtag() {
		return paramtag;
	}

	public void setParamtag(String paramtag) {
		this.paramtag = paramtag;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getSqlname() {
		return sqlname;
	}

	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}