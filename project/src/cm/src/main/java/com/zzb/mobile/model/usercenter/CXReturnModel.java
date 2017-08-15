package com.zzb.mobile.model.usercenter;

import java.util.List;

public class CXReturnModel {
	/**
	 * 返回状态信息fail success
	 */
	private String status;
	/**
	 * 返回状态描述信息
	 */
	private String message;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 *性别0男1女
	 */
    private String sex;
	/**
	 *手机号
	 */
    private String mobile;
	/**
	 *从手机号号
	 */
    private String mobile2;
	/**
	 *邮箱
	 */
    private String email;
	/**
	 *证件类型
	 */
    private String cardType;
	/**
	 *证件号
	 */
    private String cardNumber;
	/**
	 *年龄
	 */
    private Integer age;
	/**
	 *生日
	 */
    private String birthday;
	/**
	 *工号
	 */
    private String agentCode;
	/**
	 *财寿险标志01：寿险，02：财险
	 */
    private String bizType;
	/**
	 *是否团队长(财)0不是1是
	 */
    private String signUpFlag;
	/**
	 *执业证书号
	 */
    private String certifNo2;
	/**
	 *资格证书号
	 */
    private String certifNo;
	/**
	 *推荐人代码
	 */
    private String introAgency;
	/**
	 *代理人状态
	 */
    private String agentstate;
	/**
	 *身份证正面照url
	 */
	private String idcardpositiveurl;
	/**
	 *身份证反面照url
	 */
	private String idcardoppositeurl;
	/**
	 *密码
	 */
	private String password;
	/**
	 *用户银行卡信息
	 */
	private List<UserCenterBank> userCenterBanks;
	/**
	 *用户机构信息
	 */
	private List<UserCenterDept> userCenterDepts;
	/**
	 * 省份编码
	 */
	private String provincecode;
	/**
	 * 市编码
	 */
	private String citycode;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getSignUpFlag() {
		return signUpFlag;
	}
	public void setSignUpFlag(String signUpFlag) {
		this.signUpFlag = signUpFlag;
	}
	public String getCertifNo2() {
		return certifNo2;
	}
	public void setCertifNo2(String certifNo2) {
		this.certifNo2 = certifNo2;
	}
	public String getCertifNo() {
		return certifNo;
	}
	public void setCertifNo(String certifNo) {
		this.certifNo = certifNo;
	}
	public String getIntroAgency() {
		return introAgency;
	}
	public void setIntroAgency(String introAgency) {
		this.introAgency = introAgency;
	}
	public String getAgentstate() {
		return agentstate;
	}
	public void setAgentstate(String agentstate) {
		this.agentstate = agentstate;
	}
	public String getIdcardpositiveurl() {
		return idcardpositiveurl;
	}
	public void setIdcardpositiveurl(String idcardpositiveurl) {
		this.idcardpositiveurl = idcardpositiveurl;
	}
	public String getIdcardoppositeurl() {
		return idcardoppositeurl;
	}
	public void setIdcardoppositeurl(String idcardoppositeurl) {
		this.idcardoppositeurl = idcardoppositeurl;
	}
	public List<UserCenterBank> getUserCenterBanks() {
		return userCenterBanks;
	}
	public void setUserCenterBanks(List<UserCenterBank> userCenterBanks) {
		this.userCenterBanks = userCenterBanks;
	}
	public List<UserCenterDept> getUserCenterDepts() {
		return userCenterDepts;
	}
	public void setUserCenterDepts(List<UserCenterDept> userCenterDepts) {
		this.userCenterDepts = userCenterDepts;
	}

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
}
