package com.zzb.cm.Interface.entity.car.model;

import java.io.Serializable;
import java.util.List;

/**
 * 特殊险种
 * Created by austinChen on 2015/9/18.
 */

public class SpecialRisk implements Serializable {
	private static final long serialVersionUID = -4505539966510748804L;

	 private List<SuiteDef> suites;

	public List<SuiteDef> getSuites() {
		return suites;
	}

	public void setSuites(List<SuiteDef> suites) {
		this.suites = suites;
	}
	 
}
