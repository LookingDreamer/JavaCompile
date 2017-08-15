package com.zzb.mobile.model;

import java.io.Serializable;
import java.util.List;

import com.zzb.app.model.bean.RisksData;

public class InsuredConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public List<RisksData> getVALUE() {
		return VALUE;
	}
	public void setVALUE(List<RisksData> vALUE) {
		VALUE = vALUE;
	}
	
	
}
