package com.zzb.app.model.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("supplierIdList")
public class SupplierIdList {

	@XStreamImplicit(itemFieldName="row")
	private List<String> row;

	public List<String> getRow() {
		return row;
	}

	public void setRow(List<String> row) {
		this.row = row;
	}
}
