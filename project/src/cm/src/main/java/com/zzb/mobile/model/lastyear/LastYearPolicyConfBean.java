package com.zzb.mobile.model.lastyear;

import java.io.Serializable;
import java.util.List;

public class LastYearPolicyConfBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 保险配置信息 险种名称，险种编码，保额()
	 */
	private List<LastYearRiskinfo> lastYearRiskinfos;

	public List<LastYearRiskinfo> getLastYearRiskinfos() {
		return lastYearRiskinfos;
	}

	public void setLastYearRiskinfos(List<LastYearRiskinfo> lastYearRiskinfos) {
		this.lastYearRiskinfos = lastYearRiskinfos;
	}

}
