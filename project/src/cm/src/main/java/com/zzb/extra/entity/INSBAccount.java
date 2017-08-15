package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAccount extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private String agentid;

	/**
	 * 状态
	 */
	private Integer status;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}