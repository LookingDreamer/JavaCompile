package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBGroupprivileg extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 群组id
	 */
	private String groupid;

	/**
	 * 叶子权限编码
	 */
	private String privilegcode;

	/**
	 * 权限是否生效
	 */
	private Integer privilegestate;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getPrivilegcode() {
		return privilegcode;
	}

	public void setPrivilegcode(String privilegcode) {
		this.privilegcode = privilegcode;
	}

	public Integer getPrivilegestate() {
		return privilegestate;
	}

	public void setPrivilegestate(Integer privilegestate) {
		this.privilegestate = privilegestate;
	}

	@Override
	public String toString() {
		return "INSBGroupprivileg [groupid=" + groupid + ", privilegcode="
				+ privilegcode + ", privilegestate=" + privilegestate + "]";
	}

	
	
}