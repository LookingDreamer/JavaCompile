package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBGroupprovide extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 业管群组id
	 */
	private String groupid;

	/**
	 * 供应商id
	 */
	private String provideid;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getProvideid() {
		return provideid;
	}

	public void setProvideid(String provideid) {
		this.provideid = provideid;
	}

	@Override
	public String toString() {
		return "INSBGroupprovide [groupid=" + groupid + ", provideid="
				+ provideid + "]";
	}
	
	

}