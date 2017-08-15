package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSpecifydriver extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 为内部任务id，后面会被替换为真实任务id
	 */
	private String taskid;

	/**
	 * 车辆信息表id
	 */
	private String carinfoid;

	/**
	 * 人员id
	 */
	private String personid;
	
	/**
	 * 驾驶证描述
	 */
	private String licenseDesc;
	
	/**
	 * 证件状态
	 */
	private String licenseState;
	
	/**
	 * 是否主驾驶员
	 */
	private String isPrimary;
	
	/**
	 * 与车主关系
	 */
	private String relation;
	
	/**
	 * 驾驶证类型
	 */
	private String licenseType;
	
	/**
	 * 驾驶习惯
	 */
	private String habits;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCarinfoid() {
		return carinfoid;
	}

	public void setCarinfoid(String carinfoid) {
		this.carinfoid = carinfoid;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}

	public String getLicenseDesc() {
		return licenseDesc;
	}

	public void setLicenseDesc(String licenseDesc) {
		this.licenseDesc = licenseDesc;
	}

	public String getLicenseState() {
		return licenseState;
	}

	public void setLicenseState(String licenseState) {
		this.licenseState = licenseState;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getHabits() {
		return habits;
	}

	public void setHabits(String habits) {
		this.habits = habits;
	}

}