package com.zzb.mobile.model.lastinsured;

import java.util.List;

public class LastYearPolicyConfBean {
	
	//保险配置信息
	List<LastYearRiskinfo> lastYearRiskinfos;//险种名称，险种编码，保额()


	public List<LastYearRiskinfo> getLastYearRiskinfos() {
		return lastYearRiskinfos;
	}
	public void setLastYearRiskinfos(List<LastYearRiskinfo> lastYearRiskinfos) {
		this.lastYearRiskinfos = lastYearRiskinfos;
	}
	
}
