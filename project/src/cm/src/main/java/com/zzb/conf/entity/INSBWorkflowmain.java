package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBWorkflowmain extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * 操作人
	 */
	private String fromoperator;
	
	public void setFromoperator(String fromoperator) {
		this.fromoperator = fromoperator;
	}
	public String getFromoperator() {
		return fromoperator;
	}
	private String reclystate;
	
	public String getReclystate() {
		return reclystate;
	}
	public void setReclystate(String reclystate) {
		this.reclystate = reclystate;
	}
	/**
	 * 
	 */
	private String instanceid;

	/**
	 * 
	 */
	private String taskid;
	
	
	/**
	 * 当前节点操作状态
	 */
	private String operatorstate;

	/**
	 * 
	 */
	private String taskcode;
	
	
	private String taskname;
	
	/**
	 * 当前节点任务分配组
	 */
	private String groupid;

	/**
	 * 创建者
	 */
	private String createby;

	/**
	 * 节点状态 1：未完成 ，2：子节点，3：已完成
	 */
	private String taskstate;
	
	
	private String taskfeedback;

	private String providerId;// 供应商编码

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getTaskstate() {
		return taskstate;
	}

	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}
	
	

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	public void setOperatorstate(String operatorstate) {
		this.operatorstate = operatorstate;
	}
	public String getOperatorstate() {
		return operatorstate;
	}

	public String getTaskfeedback() {
		return taskfeedback;
	}
	public void setTaskfeedback(String taskfeedback) {
		this.taskfeedback = taskfeedback;
	}
	@Override
	public String toString() {
		return "INSBWorkflowmain [fromoperator=" + fromoperator
				+ ", instanceid=" + instanceid + ", taskid=" + taskid
				+ ", operatorstate=" + operatorstate + ", taskcode=" + taskcode
				+ ", taskname=" + taskname + ", groupid=" + groupid
				+ ", createby=" + createby + ", taskstate=" + taskstate
				+ ", taskfeedback=" + taskfeedback + ",providerId="+ providerId +"]";
	}
}