package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ownerInfo")
public class DriverOwer {
	/**
	 * 车主信息
	 */
	@XStreamAlias("personInfo")
	private PersonInfo personInfo;

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
}
