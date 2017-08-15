package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("insureConfig")
public class InsureConfig {

	/**
	 * 险种列表
	 */
	@XStreamAlias("ensureItemList")
	private EnsureItemList ensureItemList;
	/**
	 * 备注列表
	 */
	@XStreamAlias("remarks")
	private Remarks remarks;
	
	public EnsureItemList getEnsureItemList() {
		return ensureItemList;
	}
	public void setEnsureItemList(EnsureItemList ensureItemList) {
		this.ensureItemList = ensureItemList;
	}
	public Remarks getRemarks() {
		return remarks;
	}
	public void setRemarks(Remarks remarks) {
		this.remarks = remarks;
	}
	
}
