package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.cm.entity.INSBPerson;

public class INSBRelationPersonVO extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	//关系人信息
	private INSBPerson insured;
	private INSBPerson applicant;
	private INSBPerson linkPerson;
	private INSBPerson personForRight;
	private INSBPerson carOwnerInfo;
	
	//是否与被保人一致标记
	private Boolean applicantflag;
	private Boolean linkPersonflag;
	private Boolean personForRightflag;
	
	//其他信息
	private String taskid;
	private String inscomcode;
	private String insuredid;
	private String applicantid;
	private String linkPersonid;
	private String personForRightid;
	private String carOwnerid;
	public INSBPerson getInsured() {
		return insured;
	}
	public void setInsured(INSBPerson insured) {
		this.insured = insured;
	}
	public INSBPerson getApplicant() {
		return applicant;
	}
	public void setApplicant(INSBPerson applicant) {
		this.applicant = applicant;
	}
	public INSBPerson getLinkPerson() {
		return linkPerson;
	}
	public void setLinkPerson(INSBPerson linkPerson) {
		this.linkPerson = linkPerson;
	}
	public INSBPerson getPersonForRight() {
		return personForRight;
	}
	public void setPersonForRight(INSBPerson personForRight) {
		this.personForRight = personForRight;
	}
	public Boolean getApplicantflag() {
		return applicantflag;
	}
	public void setApplicantflag(Boolean applicantflag) {
		this.applicantflag = applicantflag;
	}
	public Boolean getLinkPersonflag() {
		return linkPersonflag;
	}
	public void setLinkPersonflag(Boolean linkPersonflag) {
		this.linkPersonflag = linkPersonflag;
	}
	public Boolean getPersonForRightflag() {
		return personForRightflag;
	}
	public void setPersonForRightflag(Boolean personForRightflag) {
		this.personForRightflag = personForRightflag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getInsuredid() {
		return insuredid;
	}
	public void setInsuredid(String insuredid) {
		this.insuredid = insuredid;
	}
	public String getApplicantid() {
		return applicantid;
	}
	public void setApplicantid(String applicantid) {
		this.applicantid = applicantid;
	}
	public String getLinkPersonid() {
		return linkPersonid;
	}
	public void setLinkPersonid(String linkPersonid) {
		this.linkPersonid = linkPersonid;
	}
	public String getPersonForRightid() {
		return personForRightid;
	}
	public void setPersonForRightid(String personForRightid) {
		this.personForRightid = personForRightid;
	}
	public INSBPerson getCarOwnerInfo() {
		return carOwnerInfo;
	}
	public void setCarOwnerInfo(INSBPerson carOwnerInfo) {
		this.carOwnerInfo = carOwnerInfo;
	}
	public String getCarOwnerid() {
		return carOwnerid;
	}
	public void setCarOwnerid(String carOwnerid) {
		this.carOwnerid = carOwnerid;
	}
	public INSBRelationPersonVO() {
		super();
	}
	
}
