package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBTasksetrulebse extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 规则id
	 */
	private String rulebaseid;

	/**
	 * 任务组id
	 */
	private String tasksetid;

	public String getRulebaseid() {
		return rulebaseid;
	}

	public void setRulebaseid(String rulebaseid) {
		this.rulebaseid = rulebaseid;
	}

	public String getTasksetid() {
		return tasksetid;
	}

	public void setTasksetid(String tasksetid) {
		this.tasksetid = tasksetid;
	}

}