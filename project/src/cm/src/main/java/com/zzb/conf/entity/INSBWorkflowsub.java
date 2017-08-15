package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;


public class INSBWorkflowsub extends BaseEntity implements Identifiable {
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
	 * 当前流程id
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
	
	
	private String taskname;
	
	/**
	 * 当前节点操作状态
	 */
	private String operatorstate;

	/**
	 * 节点状态
	 */
	private  String taskstate;
	
	
	private String createby;
	
	private String taskfeedback;

	private String wfsubtrackid;

	/**
	 * 首次核保时间
	 */
	private Date firstunderwritingtime;

	/**
	 * 最后一次核保完成时间
	 */
	private Date lastunderwritingovertime;

	private String providerId;// 供应商编码

	/**
	 * 核保成功方式，用taskcode对应的代码，18-人工核保，40-EDI核保，41-精灵核保，其他-未核保或核保不成功
	 */
	private String underwritingsuccesstype;

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


	public String getTaskname() {
		return taskname;
	}


	public void setTaskname(String taskname) {
		this.taskname = taskname;
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


	public String getTaskfeedback() {
		return taskfeedback;
	}
	public void setTaskfeedback(String taskfeedback) {
		this.taskfeedback = taskfeedback;
	}

	public String getWfsubtrackid() {
		return wfsubtrackid;
	}

	public void setWfsubtrackid(String wfsubtrackid) {
		this.wfsubtrackid = wfsubtrackid;
	}

	public Date getFirstunderwritingtime() {
		return firstunderwritingtime;
	}

	public void setFirstunderwritingtime(Date firstunderwritingtime) {
		this.firstunderwritingtime = firstunderwritingtime;
	}

	public Date getLastunderwritingovertime() {
		return lastunderwritingovertime;
	}

	public void setLastunderwritingovertime(Date lastunderwritingovertime) {
		this.lastunderwritingovertime = lastunderwritingovertime;
	}

	private String startTime;
	private String endTime;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUnderwritingsuccesstype() {
		return underwritingsuccesstype;
	}

	public void setUnderwritingsuccesstype(String underwritingsuccesstype) {
		this.underwritingsuccesstype = underwritingsuccesstype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instanceid == null) ? 0 : instanceid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		INSBWorkflowsub other = (INSBWorkflowsub) obj;
		if (instanceid == null) {
			if (other.instanceid != null)
				return false;
		} else if (!instanceid.equals(other.instanceid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "INSBWorkflowsub{" +
				"fromoperator='" + fromoperator + '\'' +
				", reclystate='" + reclystate + '\'' +
				", instanceid='" + instanceid + '\'' +
				", maininstanceid='" + maininstanceid + '\'' +
				", starttaskid='" + starttaskid + '\'' +
				", taskid='" + taskid + '\'' +
				", groupid='" + groupid + '\'' +
				", taskcode='" + taskcode + '\'' +
				", taskname='" + taskname + '\'' +
				", operatorstate='" + operatorstate + '\'' +
				", taskstate='" + taskstate + '\'' +
				", createby='" + createby + '\'' +
				", taskfeedback='" + taskfeedback + '\'' +
				", wfsubtrackid='" + wfsubtrackid + '\'' +
				", firstunderwritingtime=" + firstunderwritingtime +
				", lastunderwritingovertime=" + lastunderwritingovertime +
				", providerId='" + providerId + '\'' +
				", underwritingsuccesstype='" + underwritingsuccesstype + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				'}';
	}
}