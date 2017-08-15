package com.lzgapi.yzt.model;

import java.util.List;

public class UptomanagerModel {

	private List<AgentLzgModel> otheruseridlist;
	
	private String platform;
	
	private String token;

	public List<AgentLzgModel> getOtheruseridlist() {
		return otheruseridlist;
	}

	public void setOtheruseridlist(List<AgentLzgModel> otheruseridlist) {
		this.otheruseridlist = otheruseridlist;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UptomanagerModel [otheruseridlist=" + otheruseridlist
				+ ", platform=" + platform + ", token=" + token + "]";
	}
}
