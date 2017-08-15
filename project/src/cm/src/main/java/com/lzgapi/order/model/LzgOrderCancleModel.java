package com.lzgapi.order.model;


public class LzgOrderCancleModel {

	/**
	 * 订单编号
	 */
	private String orderno;
	
	/**
	 * 0-取消、1-删除
	 */
	private String type;
	
	/**
	 * 懒掌柜入口标记位
	 */
	private String token;
	
	
	/**
	 * 一账通id
	 */
	private String managerid;
	
	
	/**
	 * 初始化订单id（寿险通适用）
	 */
	private String requirementid;


	public String getOrderno() {
		return orderno;
	}


	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getManagerid() {
		return managerid;
	}


	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}


	public String getRequirementid() {
		return requirementid;
	}


	public void setRequirementid(String requirementid) {
		this.requirementid = requirementid;
	}


	@Override
	public String toString() {
		return "LzgOrderCancleModel [orderno=" + orderno + ", type=" + type
				+ ", token=" + token + ", managerid=" + managerid
				+ ", requirementid=" + requirementid + "]";
	}
	
	
	
	
}