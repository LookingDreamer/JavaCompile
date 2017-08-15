package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBWorkflowsubtrack extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 子流程id
	 */
	private String instanceid;

	/**
	 * 主流程id
	 */
	private String maininstanceid;

	/**
	 * 主节点id
	 */
	private String starttaskid;

	/**
	 * 节点id
	 */
	private String taskid;
	
	/**
	 * 当前节点任务分配组
	 */
	private String groupid;

	/**
	 * 节点名称
	 */
	private String taskcode;

	/**
	 * 节点状态
	 */
	private String taskstate;
	
	/**
	 * 当前节点操作状态
	 */
	private String operatorstate;

	/**
	 * 流程创建者
	 */
	private String createby;
	
	private String wfsubid;

	public String getWfsubid() {
		return wfsubid;
	}

	public void setWfsubid(String wfsubid) {
		this.wfsubid = wfsubid;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getMaininstanceid() {
		return maininstanceid;
	}

	public void setMaininstanceid(String maininstanceid) {
		this.maininstanceid = maininstanceid;
	}

	public String getStarttaskid() {
		return starttaskid;
	}

	public void setStarttaskid(String starttaskid) {
		this.starttaskid = starttaskid;
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

	public String getTaskstate() {
		return taskstate;
	}

	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
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
		return "INSBWorkflowsubtrack [instanceid=" + instanceid + ", maininstanceid=" + maininstanceid
				+ ", starttaskid=" + starttaskid + ", taskid=" + taskid + ", groupid=" + groupid + ", taskcode="
				+ taskcode + ", taskstate=" + taskstate + ", operatorstate=" + operatorstate + ", createby=" + createby
				+ ", wfsubid=" + wfsubid + "]";
	}



}