package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBOrderlistenpush extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 平台类型（1：懒掌柜）
	 */
	private String platformtype;

	/**
	 * 操作状态1：save 2 update 3 cancle 4complete
	 */
	private String operationtype;

	/**
	 * 任务号
	 */
	private String taskid;
	
	/**
	 * 任务状态
	 */
	private String taskcode;
	/**
	 * 响应状态：OK-成功 FAIL-失败
	 */
	private String status;

	/**
	 * 响应结果说明，如果状态为失败，这里给出失败原因
	 */
	private String message;

	/**
	 * 懒掌柜id
	 */
	private String lzgid;
	
	
	
	/**
	 * 订单子流程id
	 */
	private String subtaskid;
	
	/**
	 * cm订单状态信息
	 */
	private String taskname;

	public String getPlatformtype() {
		return platformtype;
	}

	public void setPlatformtype(String platformtype) {
		this.platformtype = platformtype;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLzgid() {
		return lzgid;
	}

	public void setLzgid(String lzgid) {
		this.lzgid = lzgid;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getSubtaskid() {
		return subtaskid;
	}

	public void setSubtaskid(String subtaskid) {
		this.subtaskid = subtaskid;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	@Override
	public String toString() {
		return "INSBOrderlistenpush [platformtype=" + platformtype
				+ ", operationtype=" + operationtype + ", taskid=" + taskid
				+ ", taskcode=" + taskcode + ", status=" + status
				+ ", message=" + message + ", lzgid=" + lzgid + ", subtaskid="
				+ subtaskid + ", taskname=" + taskname + "]";
	}
	
}