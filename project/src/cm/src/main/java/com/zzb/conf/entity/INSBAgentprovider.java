package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentprovider extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 代理人id
	 */
	private String agentid;

	/**
	 * 供应商id
	 */
	private String providerid;

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

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "INSBAgentprovider [agentid=" + agentid + ", providerid="
				+ providerid + ", status=" + status + "]";
	}
	
	

}