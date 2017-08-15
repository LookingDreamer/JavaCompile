package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBTasksetscope extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务组id
	 */
	private String taksetid;

	/**
	 * 机构id
	 */
	private String deptid;

	public String getTaksetid() {
		return taksetid;
	}

	public void setTaksetid(String taksetid) {
		this.taksetid = taksetid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

}