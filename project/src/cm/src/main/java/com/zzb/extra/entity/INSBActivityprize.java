package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBActivityprize extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动ID
	 */
	private String activityid;

	/**
	 * 奖品ID
	 */
	private String prizeid;

	public String getActivityid() {
		return activityid;
	}

	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}

	public String getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(String prizeid) {
		this.prizeid = prizeid;
	}

}