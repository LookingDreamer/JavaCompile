package com.lzgapi.yzt.model;

public class RegisterOrBecomeAgentModel {
	/**
	 * 一账通id
	 */
	private String managerid;

	/**
	 * 懒掌柜入口标记位
	 */
	private String token;

	/**
	 * 第三方平台用户id
	 */
	private String userid;

	private String name;
	private String phone;
	private String passWord;
	private String refNum;
	private String openid;

	/**
	 * 代理人工号
	 */
	/*
	 * private String agentnum;
	 */

	/**
	 * 主营业务
	 */
	private String mainbiz;

	/**
	 * 身份证正面照
	 */
	private String idcardpositive;

	/**
	 * 身份证反面照
	 */
	private String idcardopposite;

	/**
	 * 银行卡正面照
	 */
	private String bankcardpositive;

	/**
	 * 资格证照片页
	 */
	private String qualificationpositive;

	/**
	 * 资格证照片页
	 */
	private String qualificationpage;

	private String noti;

	/**
	 * 是否同意条款 1同意/0不同意
	 */
	private String agree;

	/*
	 * private String provinceCode; private String cityCode; private String
	 * countyCode;
	 */
	private String area;

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getRefNum() {
		return refNum;
	}

	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMainbiz() {
		return mainbiz;
	}

	public void setMainbiz(String mainbiz) {
		this.mainbiz = mainbiz;
	}

	public String getIdcardpositive() {
		return idcardpositive;
	}

	public void setIdcardpositive(String idcardpositive) {
		this.idcardpositive = idcardpositive;
	}

	public String getIdcardopposite() {
		return idcardopposite;
	}

	public void setIdcardopposite(String idcardopposite) {
		this.idcardopposite = idcardopposite;
	}

	public String getBankcardpositive() {
		return bankcardpositive;
	}

	public void setBankcardpositive(String bankcardpositive) {
		this.bankcardpositive = bankcardpositive;
	}

	public String getQualificationpositive() {
		return qualificationpositive;
	}

	public void setQualificationpositive(String qualificationpositive) {
		this.qualificationpositive = qualificationpositive;
	}

	public String getQualificationpage() {
		return qualificationpage;
	}

	public void setQualificationpage(String qualificationpage) {
		this.qualificationpage = qualificationpage;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}

	public String getAgree() {
		return agree;
	}

	public void setAgree(String agree) {
		this.agree = agree;
	}

	@Override
	public String toString() {
		return "RegisterOrBecomeAgentModel [managerid=" + managerid
				+ ", token=" + token + ", userid=" + userid + ", name=" + name
				+ ", phone=" + phone + ", passWord=" + passWord + ", refNum="
				+ refNum + ", openid=" + openid + ", mainbiz=" + mainbiz
				+ ", idcardpositive=" + idcardpositive + ", idcardopposite="
				+ idcardopposite + ", bankcardpositive=" + bankcardpositive
				+ ", qualificationpositive=" + qualificationpositive
				+ ", qualificationpage=" + qualificationpage + ", noti=" + noti
				+ ", agree=" + agree + ", area=" + area + "]";
	}

}
