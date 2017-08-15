package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBGrouptaskset extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String tasksetid;

	/**
	 * 
	 */
	private String groupid;

	/**
	 * 群组名称 方便查询
	 */
	private String groupname;

	private String groupcode;

	/**
	 * 任务组编码 ldap
	 */
	private String tasksetcode;

	/**
	 * 任务组名称
	 */
	private String setname;

	/**
	 * 任务组状态
	 */
	private Integer setstatus;

	/**
	 * 任务组状态表现值
	 */
	private String setstatusstr;

	/**
	 * 任务组描述
	 */
	private String setdescription;

	public String getTasksetid() {
		return tasksetid;
	}

	public void setTasksetid(String tasksetid) {
		this.tasksetid = tasksetid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getTasksetcode() {
		return tasksetcode;
	}

	public void setTasksetcode(String tasksetcode) {
		this.tasksetcode = tasksetcode;
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

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public String getSetstatusstr() {
		return setstatusstr;
	}

	public void setSetstatusstr(String setstatusstr) {
		this.setstatusstr = setstatusstr;
	}

	@Override
	public String toString() {
		return "INSBGrouptaskset [tasksetid=" + tasksetid + ", groupid="
				+ groupid + ", groupname=" + groupname + ", groupcode="
				+ groupcode + ", tasksetcode=" + tasksetcode + ", setname="
				+ setname + ", setstatus=" + setstatus + ", setstatusstr="
				+ setstatusstr + ", setdescription=" + setdescription + "]";
	}

}