package com.zzb.app.model.bean;

import java.io.Serializable;

public class RisksData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 险别保额key值
	 */
	private String KEY;
	/**
	 * 险别保额
	 */
	private String VALUE;
	/**
	 * 单位   万/座
	 */
	private String UNIT;
	
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String UNIT) {
		this.UNIT = UNIT;
	}
	public String getKEY() {
		return KEY;
	}
	public void setKEY(String KEY) {
		this.KEY = KEY;
	}
	public String getVALUE() {
		return VALUE;
	}
	public void setVALUE(String VALUE) {
		this.VALUE = VALUE;
	}
	
	
}
