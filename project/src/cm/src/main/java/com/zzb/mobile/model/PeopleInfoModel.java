package com.zzb.mobile.model;

public class PeopleInfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 *  是否与车主一致 true 一致
	 */
	private boolean samecarowner;
	/**
	 *  0被保人	1 投保人 	2权益索赔人
	 */
	private String flag;
	/**
	 * 人员信息
	 */
	private AppPerson personinfo;
    
	
	public AppPerson getPersoninfo() {
		return personinfo;
	}
	public void setPersoninfo(AppPerson personinfo) {
		this.personinfo = personinfo;
	}
	public boolean isSamecarowner() {
		return samecarowner;
	}
	public void setSamecarowner(boolean samecarowner) {
		this.samecarowner = samecarowner;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
