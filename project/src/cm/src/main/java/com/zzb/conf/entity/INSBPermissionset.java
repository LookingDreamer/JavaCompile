package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBPermissionset extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 序号 
	 */
	 private String rownum;
	/**
	 * 权限包编码
	 */
	private String setcode;

	/**
	 * 权限包名称
	 */
	private String setname;

	/**
	 * 机构ID
	 */
	private String deptid;

	/**
	 * 机构树
	 */
	private String treedept;

	/**
	 * 用户种类
	 */
	private Integer agentkind;
	
	/**
	 * 是否为新功能包
	 */
	private Integer isnew;

	public Integer getIsnew() {
		return isnew;
	}

	public void setIsnew(Integer isnew) {
		this.isnew = isnew;
	}

	public String getAgentkindstr() {
		return agentkindstr;
	}

	public void setAgentkindstr(String agentkindstr) {
		this.agentkindstr = agentkindstr;
	}

	private String agentkindstr;

	/**
	 * 是否测试账号
	 */
	private Integer istest;

	/**
	 * 机构名称
	 */
	private String comname;

	private Integer status;
	
	
	private String statusstr;
	

	public String getStatusstr() {
		return statusstr;
	}

	public void setStatusstr(String statusstr) {
		this.statusstr = statusstr;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getSetcode() {
		return setcode;
	}

	public void setSetcode(String setcode) {
		this.setcode = setcode;
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

	public String getTreedept() {
		return treedept;
	}

	public void setTreedept(String treedept) {
		this.treedept = treedept;
	}

	public Integer getAgentkind() {
		return agentkind;
	}

	public void setAgentkind(Integer agentkind) {
		this.agentkind = agentkind;
	}

	public Integer getIstest() {
		return istest;
	}

	public void setIstest(Integer istest) {
		this.istest = istest;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	@Override
	public String toString() {
		return "INSBPermissionset [setcode=" + setcode + ", setname=" + setname
				+ ", deptid=" + deptid + ", treedept=" + treedept
				+ ", agentkind=" + agentkind + ", isnew=" + isnew
				+ ", agentkindstr=" + agentkindstr + ", istest=" + istest
				+ ", comname=" + comname + ", status=" + status
				+ ", statusstr=" + statusstr + ",rownum="+rownum+"]";
	}

	



}