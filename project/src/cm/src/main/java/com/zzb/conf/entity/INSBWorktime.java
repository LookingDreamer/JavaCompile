package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBWorktime extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 操作员
	 */
	private String operater;

	/**
	 * 机构id
	 */
	private String inscdeptid;

	/**
	 * 支付时间类型
	 */
	private String paytimetype;

	/**
	 * 非工作时间文字提醒
	 */
	private String noworkwords;

	/**
	 * 工作时间文字提醒
	 */
	private String workwords;
	
	/**
	 * 网点禁用状态
	 */
	private String networkstate;
	
	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getInscdeptid() {
		return inscdeptid;
	}

	public void setInscdeptid(String inscdeptid) {
		this.inscdeptid = inscdeptid;
	}

	public String getPaytimetype() {
		return paytimetype;
	}

	public void setPaytimetype(String paytimetype) {
		this.paytimetype = paytimetype;
	}

	public String getNoworkwords() {
		return noworkwords;
	}

	public void setNoworkwords(String noworkwords) {
		this.noworkwords = noworkwords;
	}

	public String getWorkwords() {
		return workwords;
	}

	public void setWorkwords(String workwords) {
		this.workwords = workwords;
	}

	public String getNetworkstate() {
		return networkstate;
	}

	public void setNetworkstate(String networkstate) {
		this.networkstate = networkstate;
	}
	
}