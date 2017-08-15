package com.zzb.mobile.model;

public class CommonModel {

	/**失败**/
	public static final String STATUS_FAIL = "fail";
	/**成功**/
	public static final String STATUS_SUCCESS = "success";
	/**数据检查异常**/
	public static final String STATUS_CHECK="check";
	/** 支付失败状态码*/
	public static final String PAID_FAILED = "03";
	/** 已支付状态码*/
	public static final String PAID_CODE = "02";
	/** 支付中状态码*/
	public static final String PAYING_CODE = "01";
	/** 未支付状态码*/
	public static final String UNPAID_CODE = "00";
	/**
	 * success or fail or check
	 */
	private String status;
	/**
	 * 返回状态信息
	 */
	private String message;
	/**
	 * 返回的结果
	 */
	private Object body;

	public CommonModel(){}

	public CommonModel(String status, String message, String body) {
		this.status = status;
		this.message = message;
		this.body = body;
	}
	
	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
