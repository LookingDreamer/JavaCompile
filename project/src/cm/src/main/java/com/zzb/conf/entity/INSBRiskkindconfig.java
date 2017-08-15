package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskkindconfig extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 备注 类型 0 商业险 1 交强险车船税
	 */
	private String type;

	/**
	 * 险别名称
	 */
	private String riskkindname;

	/**
	 * 险别编码
	 */
	private String riskkindcode;

	/**
	 * 所需的前置险别编码
	 */
	private String prekindcode;

	/**
	 * 是否是不计免赔
	 */
	private String isdeductible;

	/**
	 * 险别所需数据项 存json对象
	 */
	private String datatemp;
	/**
	 * 是否选中 0 选中 1 没选中
	 */
	private String isselected;
	/**
	 * 选中的下拉列表key值
	 */
	private String selectkey;
	/**
	 * 是否选中不计免赔 0 选中 1不选中
	 */
	private String isflag;

	private String shortname;

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getIsflag() {
		return isflag;
	}

	public void setIsflag(String isflag) {
		this.isflag = isflag;
	}

	public String getIsselected() {
		return isselected;
	}

	public void setIsselected(String isselected) {
		this.isselected = isselected;
	}

	public String getSelectkey() {
		return selectkey;
	}

	public void setSelectkey(String selectkey) {
		this.selectkey = selectkey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRiskkindname() {
		return riskkindname;
	}

	public void setRiskkindname(String riskkindname) {
		this.riskkindname = riskkindname;
	}

	public String getRiskkindcode() {
		return riskkindcode;
	}

	public void setRiskkindcode(String riskkindcode) {
		this.riskkindcode = riskkindcode;
	}

	public String getPrekindcode() {
		return prekindcode;
	}

	public void setPrekindcode(String prekindcode) {
		this.prekindcode = prekindcode;
	}

	public String getIsdeductible() {
		return isdeductible;
	}

	public void setIsdeductible(String isdeductible) {
		this.isdeductible = isdeductible;
	}

	public String getDatatemp() {
		return datatemp;
	}

	public void setDatatemp(String datatemp) {
		this.datatemp = datatemp;
	}

}