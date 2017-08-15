package com.zzb.mobile.model;

/**
 * @author qiu
 * 交强险
 */
public class TrafficInsureddateBean {
	private String type;  // 0 商业险 1 交强险
    private String riskstartdate; //开始时间
    private String riskenddate ; //结束时间
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRiskstartdate() {
		return riskstartdate;
	}
	public void setRiskstartdate(String riskstartdate) {
		this.riskstartdate = riskstartdate;
	}
	public String getRiskenddate() {
		return riskenddate;
	}
	public void setRiskenddate(String riskenddate) {
		this.riskenddate = riskenddate;
	}
	
}
