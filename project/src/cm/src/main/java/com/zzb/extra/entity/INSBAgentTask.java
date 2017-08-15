package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentTask extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private String agentid;

	/**
	 * 任务id
	 */
	private String taskid;

    /**
     * 状态
     */
    private String status;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
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
}