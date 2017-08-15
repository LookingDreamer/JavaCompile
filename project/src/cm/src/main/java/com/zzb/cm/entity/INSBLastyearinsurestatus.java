package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBLastyearinsurestatus extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 平台查询开始时间
	 */
	private Date cifstarttime;

	/**
	 * 平台查询返回时间
	 */
	private String cifflag;

	/**
	 * 平台查询 0 未超时 1 超时
	 */
	private Date cifendtime;

	/**
	 * 规则平台查询开始时间
	 */
	private Date gzptstarttime;

	/**
	 * 规则平台查询返回时间
	 */
	private Date gzptendtime;

	/**
	 * 规则平台查询 0 未超时 1 超时
	 */
	private String gzptflag;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Date getCifstarttime() {
		return cifstarttime;
	}

	public void setCifstarttime(Date cifstarttime) {
		this.cifstarttime = cifstarttime;
	}

	public String getCifflag() {
		return cifflag;
	}

	public void setCifflag(String cifflag) {
		this.cifflag = cifflag;
	}

	public Date getCifendtime() {
		return cifendtime;
	}

	public void setCifendtime(Date cifendtime) {
		this.cifendtime = cifendtime;
	}

	public Date getGzptstarttime() {
		return gzptstarttime;
	}

	public void setGzptstarttime(Date gzptstarttime) {
		this.gzptstarttime = gzptstarttime;
	}

	public Date getGzptendtime() {
		return gzptendtime;
	}

	public void setGzptendtime(Date gzptendtime) {
		this.gzptendtime = gzptendtime;
	}

	public String getGzptflag() {
		return gzptflag;
	}

	public void setGzptflag(String gzptflag) {
		this.gzptflag = gzptflag;
	}

}