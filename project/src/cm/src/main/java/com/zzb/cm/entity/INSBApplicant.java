package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBApplicant extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 人员表id
	 */
	private String personid;

	/**
	 * 商业险投保单号
	 */
	private String businessproposalformno;

	/**
	 * 商业险保单号
	 */
	private String businesspolicyno;
	
	/**
	 * 与车主关系
	 */
	private String relation;

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

	public String getBusinessproposalformno() {
		return businessproposalformno;
	}

	public void setBusinessproposalformno(String businessproposalformno) {
		this.businessproposalformno = businessproposalformno;
	}

	public String getBusinesspolicyno() {
		return businesspolicyno;
	}

	public void setBusinesspolicyno(String businesspolicyno) {
		this.businesspolicyno = businesspolicyno;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

}