package com.zzb.app.model;

public class AppRegistModel {

	/**
	 * 账号
	 */
	private String account;
	/**
	 * 工号
	 */
	private String worknumber;
	/**
	 * 注册手机号
	 */
	private String phone;
	/**
	 * 注册密码
	 */
	private String passWord;
	/**
	 * 重复密码
	 */
	private String repassWord;
	/**
	 * 验证码
	 */
	private String securityCode;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 推荐人工号
	 */
	private String readNum;
	/**
	 * 所在地区  省级code（暂对应代理人所属城市字段）
	 */
	private String provinceCode;
	/**
	 * 所在地区  市级code（暂对应代理人所属城市字段）
	 */
	private String cityCode;
	/**
	 * 所在地区  区级code（暂对应代理人所属城市字段）
	 */
	private String countyCode;
	/**
	 * 备注
	 */
	private String remark;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getWorknumber() {
		return worknumber;
	}
	public void setWorknumber(String worknumber) {
		this.worknumber = worknumber;
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
	public String getRepassWord() {
		return repassWord;
	}
	public void setRepassWord(String repassWord) {
		this.repassWord = repassWord;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReadNum() {
		return readNum;
	}
	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
