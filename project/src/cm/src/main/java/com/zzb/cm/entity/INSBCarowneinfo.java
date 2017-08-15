package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCarowneinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 为内部任务id，后面会被替换为真实任务id
	 */
	private String taskid;

	/**
	 * 人员id
	 */
	private String personid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}

}