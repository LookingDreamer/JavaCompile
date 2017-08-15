package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBMarketActivityPrize extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动id
	 */
	private String activityid;

	/**
	 * 奖品id refresh
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