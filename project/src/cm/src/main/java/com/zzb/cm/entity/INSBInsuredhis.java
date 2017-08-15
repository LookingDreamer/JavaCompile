package com.zzb.cm.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBInsuredhis extends BaseEntity implements Identifiable {
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

	/**
	 * 保险公司代码
	 */
	private String inscomcode;
	
	/**
	 * 前端输入的验证码
	 */
	private String pincode;
	/**
	 * 申请验证码状态
	 */
	private String applystatus;
	/**
	 * 获取验证码时间
	 */
	private Date pindatetime;
	/**
	 * 提交验证码状态
	 */
	private String commitstatus;
	/**
	 * 申请/提交 验证码返回的错误信息
	 */
	private String pinerrmsg;
	

	public String getPinerrmsg() {
		return pinerrmsg;
	}

	public void setPinerrmsg(String pinerrmsg) {
		this.pinerrmsg = pinerrmsg;
	}

	public String getCommitstatus() {
		return commitstatus;
	}

	public void setCommitstatus(String commitstatus) {
		this.commitstatus = commitstatus;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(String applystatus) {
		this.applystatus = applystatus;
	}

	public Date getPindatetime() {
		return pindatetime;
	}

	public void setPindatetime(Date pindatetime) {
		this.pindatetime = pindatetime;
	}

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

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

}