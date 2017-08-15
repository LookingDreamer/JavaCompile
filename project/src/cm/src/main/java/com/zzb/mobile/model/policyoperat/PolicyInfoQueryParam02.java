package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 投保前信息查询操作接口使用
 */

public class PolicyInfoQueryParam02 {

	/**
	 * 保单号
	 */
	private String policyno;
	/**
	 * 查询标记位（cm、cf）
	 */
	private String queryflag;
	/**
	 * 保单类型 0交强  1商业
	 */
	private String policytype;
	
	public String getPolicytype() {
		return policytype;
	}
	public void setPolicytype(String policytype) {
		this.policytype = policytype;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getQueryflag() {
		return queryflag;
	}
	public void setQueryflag(String queryflag) {
		this.queryflag = queryflag;
	}
	
}
