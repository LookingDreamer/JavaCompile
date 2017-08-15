package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBWorkflowmaintrack extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String instanceid;

	/**
	 * 
	 */
	private String taskid;

	/**
	 * 
	 */
	private String taskcode;
	
	/**
	 * 当前节点任务分配组
	 */
	private String groupid;
	
	/**
	 * 当前节点操作状态
	 */
	private String operatorstate;

	/**
	 * 创建者
	 */
	private String createby;

	/**
	 * 节点状态 1：未完成 ，2：子节点，3：已完成
	 */
	private String  taskstate;

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

	public String getOperatorstate() {
		return operatorstate;
	}

	public void setOperatorstate(String operatorstate) {
		this.operatorstate = operatorstate;
	}

	@Override
	public String toString() {
		return "INSBWorkflowmaintrack [instanceid=" + instanceid + ", taskid="
				+ taskid + ", taskcode=" + taskcode + ", groupid=" + groupid
				+ ", operatorstate=" + operatorstate + ", createby=" + createby
				+ ", taskstate=" + taskstate + "]";
	}


}