package com.cninsure.system.entity;

import java.util.Date;

public class INSCMethodCode  {

	
	private int id;
	
	private String classname;
	
	/**
	 * 访问方法名
	 */
	private String method;

	/**
	 * 访问方法用法
	 */
	private String methodname;

	/**
	 * 访问时间
	 */
	private Date createtime;

	/**
	 * 登录用户
	 */
	private String operator;

	/**
	 * 注释
	 */
	private String noti;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	@Override
	public String toString() {
		return "INSCMethodCode [id=" + id + ", classname=" + classname
				+ ", method=" + method + ", methodname=" + methodname
				+ ", createtime=" + createtime + ", operator=" + operator
				+ ", noti=" + noti + "]";
	}
}