package com.zzb.mobile.model;


/**
 * 商业险期间/交强期间
 * 
 * @author qiu
 *
 */
public class AppBusiness {
	 /**
	 * 开始日期
	 */
	private String riskstartdate;
     /**
     * 结束日期
     */
    private String riskenddate;
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
