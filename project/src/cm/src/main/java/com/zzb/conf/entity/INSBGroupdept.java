package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBGroupdept extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 业管群组id
	 */
	private String groupid;

	/**
	 * 机构id
	 */
	private String deptid;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Override
	public String toString() {
		return "INSBGroupdept [groupid=" + groupid + ", deptid=" + deptid + "]";
	}

	
	
}