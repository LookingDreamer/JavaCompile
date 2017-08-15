package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskplanrelation extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 方案名称
	 */
	private String planname;

	/**
	 * 方案key
	 */
	private String plankey;

	/**
	 * 险别保险配置id
	 */
	private String riskkindid;
	/**
	 * 是否显示 0 显示
	 */
	private String isshow;
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

	public String getIsflag() {
		return isflag;
	}

	public void setIsflag(String isflag) {
		this.isflag = isflag;
	}

	public String getPlanname() {
		return planname;
	}

	public void setPlanname(String planname) {
		this.planname = planname;
	}

	public String getPlankey() {
		return plankey;
	}

	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}

	public String getRiskkindid() {
		return riskkindid;
	}

	public void setRiskkindid(String riskkindid) {
		this.riskkindid = riskkindid;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

}