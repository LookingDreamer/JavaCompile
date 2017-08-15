package com.zzb.app.model.bean;

import java.util.List;

/**
 * 险别要素供下拉选项
 * @author liuchao
 *
 */
public class AmountSelect {

	/**
	 * 要素类型
	 */
	private String TYPE;
	/**
	 * 要素值 
	 */
	private List<RisksData> VALUE;
	
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}
	public List<RisksData> getVALUE() {
		return VALUE;
	}
	public void setVALUE(List<RisksData> VALUE) {
		this.VALUE = VALUE;
	}
	
}
