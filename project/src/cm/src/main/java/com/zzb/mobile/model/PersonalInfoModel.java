package com.zzb.mobile.model;

public class PersonalInfoModel {


	
	
	/**
	 * 工号
	 */
	private String jobnum;
	
	/**
	 * 手机账号
	 */
	private String phone;
	
	
	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String sex;


	/**
	 * 证件号码
	 */
	private String idno;
	/**
	 * 资格证号
	 */
	private String cgfns;
	/**
	 * 认证状态
	 */
	private int approvesstate;
	
	/**
	 * 平台
	 */
	private String deptName;
	
	private  int jobNumType;
	
	/**
	 *  网点
	 */
	private String branch;
	/**
	 * 给用户备注
	 */
	private String commentcontent;

	public String getCommentcontent() {
		return commentcontent;
	}

	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}

	public String getJobnum() {
		return jobnum;
	}

	public int getJobNumType() {
		return jobNumType;
	}

	public void setJobNumType(int jobNumType) {
		this.jobNumType = jobNumType;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getCgfns() {
		return cgfns;
	}

	public void setCgfns(String cgfns) {
		this.cgfns = cgfns;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getApprovesstate() {
		return approvesstate;
	}

	public void setApprovesstate(int approvesstate) {
		this.approvesstate = approvesstate;
	}
	
	
	
	
	
	
}
