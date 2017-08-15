package com.zzb.conf.entity;

import java.util.List;



public class INSBAutoconfigshowQueryModel  {
	
	private String providerid;
	
	private String deptId;
	
	private String conftype;
	
	private List<String> quoteList;

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public List<String> getQuoteList() {
		return quoteList;
	}

	public void setQuoteList(List<String> quoteList) {
		this.quoteList = quoteList;
	}
	
	

	public String getConftype() {
		return conftype;
	}

	public void setConftype(String conftype) {
		this.conftype = conftype;
	}

	@Override
	public String toString() {
		return "INSBAutoconfigshowQueryModel [providerid=" + providerid
				+ ", deptId=" + deptId + ", conftype=" + conftype
				+ ", quoteList=" + quoteList + "]";
	}


	
}