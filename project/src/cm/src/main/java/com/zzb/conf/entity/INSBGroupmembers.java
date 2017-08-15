package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBGroupmembers extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 群组id
	 */
	private String groupid;

	/**
	 * 成员id
	 */
	private String userid;

	/**
	 * 群组权限
	 */
	private Integer groupprivilege;

	/**
	 * 群组成员权限表现值
	 */
	private String groupprivilegestr;

	private String usercode;
	private String name;
	private String userorganization;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public Integer getGroupprivilege() {
		return groupprivilege;
	}

	public void setGroupprivilege(Integer groupprivilege) {
		this.groupprivilege = groupprivilege;
	}

	public String getGroupprivilegestr() {
		return groupprivilegestr;
	}

	public void setGroupprivilegestr(String groupprivilegestr) {
		this.groupprivilegestr = groupprivilegestr;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}


	public String getUserorganization() {
		return userorganization;
	}

	public void setUserorganization(String userorganization) {
		this.userorganization = userorganization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "INSBGroupmembers [groupid=" + groupid + ", userid=" + userid
				+ ", groupprivilege=" + groupprivilege + ", groupprivilegestr="
				+ groupprivilegestr + ", usercode=" + usercode + ", name="
				+ name + ", userorganization=" + userorganization + "]";
	}



}