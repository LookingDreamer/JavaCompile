package com.zzb.app.model.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Remarks {

	@XStreamImplicit(itemFieldName="remark")
	private List<InsureRemark> remarks;

	public List<InsureRemark> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<InsureRemark> remarks) {
		this.remarks = remarks;
	}
	
}
