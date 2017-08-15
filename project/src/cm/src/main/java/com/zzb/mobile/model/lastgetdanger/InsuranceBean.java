package com.zzb.mobile.model.lastgetdanger;


public class InsuranceBean {
	String flag = "PT";//平台
	String areaId;//地区
	String taskSequence;//任务序列号
	private InParas inParas;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getTaskSequence() {
		return taskSequence;
	}
	public void setTaskSequence(String taskSequence) {
		this.taskSequence = taskSequence;
	}
	public InParas getInParas() {
		return inParas;
	}
	public void setInParas(InParas inParas) {
		this.inParas = inParas;
	}
	
	
}
