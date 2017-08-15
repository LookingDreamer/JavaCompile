package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBTaskset extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 序号 
	 */
	 private String rownum;

	/**
	 * 任务组名称
	 */
	private String setname;

	/**
	 * 组织机构id
	 */
	private String deptid;
	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 任务组状态
	 */
	private Integer setstatus;

	/**
	 * 任务类型
	 */
	private String tasktype;

	/**
	 * 任务组状态表现值
	 */
	private String setstatusstr;

	/**
	 * 任务组描述
	 */
	private String setdescription;

	private String groupname;

	private String rulebasename;

	private Integer shedulingpolicy;

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getSetname() {
		return setname;
	}

	public void setSetname(String setname) {
		this.setname = setname;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public Integer getSetstatus() {
		return setstatus;
	}

	public void setSetstatus(Integer setstatus) {
		this.setstatus = setstatus;
	}

	public String getSetdescription() {
		return setdescription;
	}

	public void setSetdescription(String setdescription) {
		this.setdescription = setdescription;
	}

	public String getSetstatusstr() {
		return setstatusstr;
	}

	public void setSetstatusstr(String setstatusstr) {
		this.setstatusstr = setstatusstr;
	}

	public Integer getShedulingpolicy() {
		return shedulingpolicy;
	}

	public void setShedulingpolicy(Integer shedulingpolicy) {
		this.shedulingpolicy = shedulingpolicy;
	}

	public String getRulebasename() {
		return rulebasename;
	}

	public void setRulebasename(String rulebasename) {
		this.rulebasename = rulebasename;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	@Override
	public String toString() {
		return "INSBTaskset [setname=" + setname + ", deptid=" + deptid
				+ ", providerid=" + providerid + ", setstatus=" + setstatus
				+ ", tasktype=" + tasktype + ", setstatusstr=" + setstatusstr
				+ ", setdescription=" + setdescription + ", groupname="
				+ groupname + ", rulebasename=" + rulebasename
				+ ", shedulingpolicy=" + shedulingpolicy + ",rownum="+rownum+"]";
	}


}