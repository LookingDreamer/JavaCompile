package com.zzb.extra.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentPrize extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private String agentid;

	/**
	 * 活动奖品id
	 */
	private String activityprizeid;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 任务id
	 */
	private String providercode;

	/**
	 * 数额
	 */
	private Double counts;

	/**
	 * 状态 refresh
	 */
	private String status;
	/**
	 * 得到时间
	 */
	private Date gettime;
	/**
	 * 使用时间
	 */
	private Date usetime;
	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getActivityprizeid() {
		return activityprizeid;
	}

	public void setActivityprizeid(String activityprizeid) {
		this.activityprizeid = activityprizeid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getProvidercode() {
		return providercode;
	}

	public void setProvidercode(String providercode) {
		this.providercode = providercode;
	}

	public Double getCounts() {
		return counts;
	}

	public void setCounts(Double counts) {
		this.counts = counts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}//refresh
	
	public Date getGettime() {
		return gettime;
	}
	
	public void setGettime(Date gettime) {
		this.gettime = gettime;
	}
	
	public Date getUsetime() {
		return usetime;
	}
	
	public void setUsetime(Date usetime) {
		this.usetime = usetime;
	}
	
}