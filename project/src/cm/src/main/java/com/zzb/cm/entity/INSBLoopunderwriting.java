package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBLoopunderwriting extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主任务号
	 */
	private String taskid;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 轮询任务创建时间
	 */
	private Date taskcreatetime;

	/**
	 * 轮询状态
	 */
	private String loopstatus;

	/**
	 * 任务类型
	 */
	private String tasktype;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public Date getTaskcreatetime() {
		return taskcreatetime;
	}

	public void setTaskcreatetime(Date taskcreatetime) {
		this.taskcreatetime = taskcreatetime;
	}

	public String getLoopstatus() {
		return loopstatus;
	}

	public void setLoopstatus(String loopstatus) {
		this.loopstatus = loopstatus;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

}