package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSCTask extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务类别ID
	 */
	private String tasktypeid;

	/**
	 * 任务名称
	 */
	private String taskname;

	/**
	 * 完整的类名
	 */
	private String fullclassname;

	public String getTasktypeid() {
		return tasktypeid;
	}

	public void setTasktypeid(String tasktypeid) {
		this.tasktypeid = tasktypeid;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getFullclassname() {
		return fullclassname;
	}

	public void setFullclassname(String fullclassname) {
		this.fullclassname = fullclassname;
	}

}