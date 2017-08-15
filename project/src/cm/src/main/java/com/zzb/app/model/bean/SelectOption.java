package com.zzb.app.model.bean;

/**
 * 险别要素model
 * @author hejie
 *
 */
public class SelectOption {

	/**
	 * 要素类型
	 */
	private String TYPE;
	/**
	 * 要素值 
	 */
	private RisksData VALUE;
	
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}
	public RisksData getVALUE() {
		return VALUE;
	}
	public void setVALUE(RisksData VALUE) {
		this.VALUE = VALUE;
	}
	
}
