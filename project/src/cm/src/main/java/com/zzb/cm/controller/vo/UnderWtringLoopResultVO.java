package com.zzb.cm.controller.vo;

public class UnderWtringLoopResultVO {

	private String maininstanceId;
	
	private String inscomcode;
	
	private String userCode;
	
	private String backFlag;
	
	private String forediflag;
	
	public String getMaininstanceId() {
		return maininstanceId;
	}
	public void setMaininstanceId(String maininstanceId) {
		this.maininstanceId = maininstanceId;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getBackFlag() {
		return backFlag;
	}
	public void setBackFlag(String backFlag) {
		this.backFlag = backFlag;
	}
	
	public String getForediflag() {
		return forediflag;
	}
	public void setForediflag(String forediflag) {
		this.forediflag = forediflag;
	}
	public UnderWtringLoopResultVO() {
		super();
	}
	public UnderWtringLoopResultVO(String maininstanceId, String inscomcode,
			String userCode, String backFlag, String forediflag) {
		super();
		this.maininstanceId = maininstanceId;
		this.inscomcode = inscomcode;
		this.userCode = userCode;
		this.backFlag = backFlag;
		this.forediflag = forediflag;
	}
	@Override
	public String toString() {
		return "UnderWtringLoopResultVO [maininstanceId=" + maininstanceId
				+ ", inscomcode=" + inscomcode + ", userCode=" + userCode
				+ ", backFlag=" + backFlag + ", forediflag=" + forediflag + "]";
	}
	
}
