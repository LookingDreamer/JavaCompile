package com.zzb.cm.controller.vo;

public class PayInfoItem {
	private String taskid; 
	private String paymentmethod;
	private String checkcode;
	private String insurecono;
	private String tradeno;
	private String inscomcode;
	private String subInstanceId;
	private String deptcode;
	
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	/**
	 * @return the taskid
	 */
	public String getTaskid() {
		return taskid;
	}
	/**
	 * @param taskid the taskid to set
	 */
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	/**
	 * @return the paymentmethod
	 */
	public String getPaymentmethod() {
		return paymentmethod;
	}
	/**
	 * @param paymentmethod the paymentmethod to set
	 */
	public void setPaymentmethod(String paymentmethod) {
		this.paymentmethod = paymentmethod;
	}
	/**
	 * @return the checkcode
	 */
	public String getCheckcode() {
		return checkcode;
	}
	/**
	 * @param checkcode the checkcode to set
	 */
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	/**
	 * @return the insurecono
	 */
	public String getInsurecono() {
		return insurecono;
	}
	/**
	 * @param insurecono the insurecono to set
	 */
	public void setInsurecono(String insurecono) {
		this.insurecono = insurecono;
	}
	/**
	 * @return the tradeno
	 */
	public String getTradeno() {
		return tradeno;
	}
	/**
	 * @param tradeno the tradeno to set
	 */
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	/**
	 * @return the subInstanceId
	 */
	public String getSubInstanceId() {
		return subInstanceId;
	}
	/**
	 * @param subInstanceId the subInstanceId to set
	 */
	public void setSubInstanceId(String subInstanceId) {
		this.subInstanceId = subInstanceId;
	}
	
}
