package com.zzb.extra.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBUserprize extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * UID
	 */
	private String uid;

	/**
	 * 活动ID
	 */
	private String activityid;

	/**
	 * 获取时间
	 */
	private  String gettime;

	/**
	 * 使用时间
	 */
	private String usetime;

	/**
	 * 状态
	 */
	private char  status;

	/**
	 * 数额
	 */
	private Integer counts;
	
	/**
	 * 奖品ID
	 */
	private String prizeid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getActivityid() {
		return activityid;
	}

	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}

	public String getGettime() {
		return gettime;
	}

	public void setGettime(String gettime) {
		this.gettime = gettime;
	}

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(String prizeid) {
		this.prizeid = prizeid;
	}

}