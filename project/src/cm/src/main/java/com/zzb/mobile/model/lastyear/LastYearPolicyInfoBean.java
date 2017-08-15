package com.zzb.mobile.model.lastyear;

import java.io.Serializable;

public class LastYearPolicyInfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 返回状态
	 */
	private String status;
	/**
	 * 返回状态说明
	 */
	private String message;
	private LastYearPolicyBean lastYearPolicyBean;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LastYearPolicyBean getLastYearPolicyBean() {
		return lastYearPolicyBean;
	}
	public void setLastYearPolicyBean(LastYearPolicyBean lastYearPolicyBean) {
		this.lastYearPolicyBean = lastYearPolicyBean;
	}
	

}
