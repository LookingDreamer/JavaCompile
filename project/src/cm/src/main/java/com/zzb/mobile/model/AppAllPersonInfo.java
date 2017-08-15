package com.zzb.mobile.model;


/**
 * 被保人+投保人+权益索赔人 信息
 * 
 * @author hzk
 *
 */
public class AppAllPersonInfo {
	/**
	 * 被保人
	 */
	private AppPerson insuredPerson;
	/**
	 * 投保人
	 */
	private AppPerson applicantPerson;
	/**
	 * 权益索赔人
	 */
	private AppPerson legalrightclaimPerson;
	/**
	 * 被保人是否与车主相同标记
	 */
	private boolean isSameCarownerInsured;
	/**
	 * 投保人是否与车主相同标记
	 */
	private boolean isSameCarownerapplicant;
	/**
	 * 权益索赔人是否与车主相同标记
	 */
	private boolean isSameCarownerRightclaim;
	public AppPerson getInsuredPerson() {
		return insuredPerson;
	}
	public void setInsuredPerson(AppPerson insuredPerson) {
		this.insuredPerson = insuredPerson;
	}
	public AppPerson getApplicantPerson() {
		return applicantPerson;
	}
	public void setApplicantPerson(AppPerson applicantPerson) {
		this.applicantPerson = applicantPerson;
	}
	public AppPerson getLegalrightclaimPerson() {
		return legalrightclaimPerson;
	}
	public void setLegalrightclaimPerson(AppPerson legalrightclaimPerson) {
		this.legalrightclaimPerson = legalrightclaimPerson;
	}
	public boolean isSameCarownerInsured() {
		return isSameCarownerInsured;
	}
	public void setSameCarownerInsured(boolean isSameCarownerInsured) {
		this.isSameCarownerInsured = isSameCarownerInsured;
	}
	public boolean isSameCarownerapplicant() {
		return isSameCarownerapplicant;
	}
	public void setSameCarownerapplicant(boolean isSameCarownerapplicant) {
		this.isSameCarownerapplicant = isSameCarownerapplicant;
	}
	public boolean isSameCarownerRightclaim() {
		return isSameCarownerRightclaim;
	}
	public void setSameCarownerRightclaim(boolean isSameCarownerRightclaim) {
		this.isSameCarownerRightclaim = isSameCarownerRightclaim;
	}
	
	
}
