package com.zzb.conf.entity;

import java.util.Date;

public class INSBRuleBase {

	private Integer id;
	private Integer engineId;
	private String ruleName;
	private String ruleGroup;
	private String ruleActivation;
	private Integer ruleSalience;
	private Integer ruleLoop;
	private Integer ruleLock;
	private String rulePostil;
	private Date lastUpdated;

	/**
	 * 规则类别
	 * 
	 * 权重 调度
	 */
	private String ruleType;

	/**
	 * 群组id
	 */
	private String tasksetId;

	/**
	 * 群组名称
	 */
	private String ruleState;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEngineId() {
		return engineId;
	}

	public void setEngineId(Integer engineId) {
		this.engineId = engineId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(String ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

	public String getRuleActivation() {
		return ruleActivation;
	}

	public void setRuleActivation(String ruleActivation) {
		this.ruleActivation = ruleActivation;
	}

	public Integer getRuleSalience() {
		return ruleSalience;
	}

	public void setRuleSalience(Integer ruleSalience) {
		this.ruleSalience = ruleSalience;
	}

	public Integer getRuleLoop() {
		return ruleLoop;
	}

	public void setRuleLoop(Integer ruleLoop) {
		this.ruleLoop = ruleLoop;
	}

	public Integer getRuleLock() {
		return ruleLock;
	}

	public void setRuleLock(Integer ruleLock) {
		this.ruleLock = ruleLock;
	}

	public String getRulePostil() {
		return rulePostil;
	}

	public void setRulePostil(String rulePostil) {
		this.rulePostil = rulePostil;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getRuleState() {
		return ruleState;
	}

	public void setRuleState(String ruleState) {
		this.ruleState = ruleState;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getTasksetId() {
		return tasksetId;
	}

	public void setTasksetId(String tasksetId) {
		this.tasksetId = tasksetId;
	}

	@Override
	public String toString() {
		return "INSBRuleBase [id=" + id + ", engineId=" + engineId
				+ ", ruleName=" + ruleName + ", ruleGroup=" + ruleGroup
				+ ", ruleActivation=" + ruleActivation + ", ruleSalience="
				+ ruleSalience + ", ruleLoop=" + ruleLoop + ", ruleLock="
				+ ruleLock + ", rulePostil=" + rulePostil + ", lastUpdated="
				+ lastUpdated + ", ruleType=" + ruleType + ", tasksetId="
				+ tasksetId + ", ruleState=" + ruleState + "]";
	}

}