package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBBusinessmanagegroup extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * pid
	 */
	private String pid;
	
	
	/**
	 * 群组名称
	 */
	private String groupname;

	/**
	 * 群组类型
	 */
	private String groupkind;
	
	
	private String groupkindstr;

	/**
	 * 群组成员数量
	 */
	private Integer groupnum;

	/**
	 * 群组所属机构id
	 */
	private String organizationid;

	/**
	 * 工作池系统权限
	 */
	private String roleid;
	
	/**
	 * ldap 业务主键
	 */
	private String groupcode;

	/**
	 * 权限是否生效
	 */
	private Integer privilegestate;
	
	private String privilegestatestr;
	
	/**
	 * 任务类型
	 */
	private String tasktype;
	/**
	 * 工作量
	 */
	private Integer workload;
	
	public Integer getWorkload() {
		return workload;
	}

	public void setWorkload(Integer workload) {
		this.workload = workload;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupkind() {
		return groupkind;
	}

	public void setGroupkind(String groupkind) {
		this.groupkind = groupkind;
	}

	public Integer getGroupnum() {
		return groupnum;
	}

	public void setGroupnum(Integer groupnum) {
		this.groupnum = groupnum;
	}

	public String getOrganizationid() {
		return organizationid;
	}

	public void setOrganizationid(String organizationid) {
		this.organizationid = organizationid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public Integer getPrivilegestate() {
		return privilegestate;
	}

	public void setPrivilegestate(Integer privilegestate) {
		this.privilegestate = privilegestate;
	}

	public String getGroupkindstr() {
		return groupkindstr;
	}

	public void setGroupkindstr(String groupkindstr) {
		this.groupkindstr = groupkindstr;
	}
	
	

	public String getPrivilegestatestr() {
		return privilegestatestr;
	}

	public void setPrivilegestatestr(String privilegestatestr) {
		this.privilegestatestr = privilegestatestr;
	}
	

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}
	
	

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	@Override
	public String toString() {
		return "INSBBusinessmanagegroup [pid=" + pid + ", groupname="
				+ groupname + ", groupkind=" + groupkind + ", groupkindstr="
				+ groupkindstr + ", groupnum=" + groupnum + ", organizationid="
				+ organizationid + ", roleid=" + roleid + ", groupcode="
				+ groupcode + ", privilegestate=" + privilegestate
				+ ", privilegestatestr=" + privilegestatestr + ", tasktype="
				+ tasktype + "]";
	}
}