package com.zzb.cm.controller.vo;

public class LoopUnderWritingVO {

	private String mainInstanceId;
	
	private String subInstanceId;
	
	private String inscomcode;

	private String loopStatus;

	private String msg;
	
	public String getMainInstanceId() {
		return mainInstanceId;
	}
	public void setMainInstanceId(String mainInstanceId) {
		this.mainInstanceId = mainInstanceId;
	}
	public String getSubInstanceId() {
		return subInstanceId;
	}
	public void setSubInstanceId(String subInstanceId) {
		this.subInstanceId = subInstanceId;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getLoopStatus() {
		return loopStatus;
	}

	public void setLoopStatus(String loopStatus) {
		this.loopStatus = loopStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
