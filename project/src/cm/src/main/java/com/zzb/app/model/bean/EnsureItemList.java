package com.zzb.app.model.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class EnsureItemList {
	
	@XStreamImplicit(itemFieldName="row")
	private List<InsureType> ensureItemList;

	public List<InsureType> getEnsureItemList() {
		return ensureItemList;
	}

	public void setEnsureItemList(List<InsureType> ensureItemList) {
		this.ensureItemList = ensureItemList;
	}
	
	
}
