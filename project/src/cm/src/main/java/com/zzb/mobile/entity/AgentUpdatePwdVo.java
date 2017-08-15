package com.zzb.mobile.entity;


public class AgentUpdatePwdVo {
	public String agentId;
	public String oldPwd;
	public String newPwd;
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	@Override
	public String toString() {
		return "AgentUpdatePwdVo [agentId=" + agentId + ", oldPwd=" + oldPwd
				+ ", newPwd=" + newPwd +"]";
	}
}
