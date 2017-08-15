package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;
 
public class INSBPermissionallot extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 权限包id
	 */
	private String setid;

	/**
	 * 权限ID
	 */
	private String permissionid;

	/**
	 * 权限名称
	 */
	private String permissionname;

	/**
	 * 前端状态
	 */
	private Integer frontstate;
	/**
	 * 表现值
	 */
	private String frontstatestr;

	/**
	 * 功能状态
	 */
	private Integer functionstate;
	/**
	 * 表现值 
	 */
	private String functionstatestr;
	
	/**
	 * 试用权限数量
	 */
	private Integer num;
	

	/**
	 * 有效时间起
	 */
	private Date validtimestart;
	
	private String validtimestartstr;

	/**
	 * 有效时间止
	 */
	private Date validtimeend;
	
	private String validtimeendstr;

	/**
	 * 关闭方式
	 */
	private Integer abort;
	/**
	 * 表现值
	 */
	private String abortstr;

	/**
	 * 预警次数
	 */
	private Integer warningtimes;

	public Integer getWarningtimes() {
		return warningtimes;
	}

	public void setWarningtimes(Integer warningtimes) {
		this.warningtimes = warningtimes;
	}

	public String getValidtimestartstr() {
		return validtimestartstr;
	}

	public void setValidtimestartstr(String validtimestartstr) {
		this.validtimestartstr = validtimestartstr;
	}


	
	public String getValidtimeendstr() {
		return validtimeendstr;
	}

	public void setValidtimeendstr(String validtimeendstr) {
		this.validtimeendstr = validtimeendstr;
	}

	public String getSetid() {
		return setid;
	}

	public void setSetid(String setid) {
		this.setid = setid;
	}

	public String getPermissionid() {
		return permissionid;
	}

	public void setPermissionid(String permissionid) {
		this.permissionid = permissionid;
	}

	public String getPermissionname() {
		return permissionname;
	}

	public void setPermissionname(String permissionname) {
		this.permissionname = permissionname;
	}

	public Integer getFrontstate() {
		return frontstate;
	}

	public void setFrontstate(Integer frontstate) {
		this.frontstate = frontstate;
	}

	public Integer getFunctionstate() {
		return functionstate;
	}

	public void setFunctionstate(Integer functionstate) {
		this.functionstate = functionstate;
	}

	public Date getValidtimestart() {
		return validtimestart;
	}

	public void setValidtimestart(Date validtimestart) {
		this.validtimestart = validtimestart;
	}

	public Date getValidtimeend() {
		return validtimeend;
	}

	public void setValidtimeend(Date validtimeend) {
		this.validtimeend = validtimeend;
	}

	public Integer getAbort() {
		return abort;
	}

	public void setAbort(Integer abort) {
		this.abort = abort;
	}

	
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	

	public String getFrontstatestr() {
		return frontstatestr;
	}

	public void setFrontstatestr(String frontstatestr) {
		this.frontstatestr = frontstatestr;
	}

	public String getFunctionstatestr() {
		return functionstatestr;
	}

	public void setFunctionstatestr(String functionstatestr) {
		this.functionstatestr = functionstatestr;
	}

	public String getAbortstr() {
		return abortstr;
	}

	public void setAbortstr(String abortstr) {
		this.abortstr = abortstr;
	}

	@Override
	public String toString() {
		return "INSBPermissionallot [setid=" + setid + ", permissionid="
				+ permissionid + ", permissionname=" + permissionname
				+ ", frontstate=" + frontstate + ", frontstatestr="
				+ frontstatestr + ", functionstate="+ functionstate
				+ ", functionstatestr=" + functionstatestr
				+  ", num=" + num + ", validtimestart="
				+ validtimestart + ", validtimestartstr=" + validtimestartstr
				+ ", validtimeend=" + validtimeend + ", validtimeendstr="
				+ validtimeendstr + ", abort=" + abort + ", abortstr=" + abortstr + "]";
	}


	


	
	
	
}