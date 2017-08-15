package com.zzb.mobile.model.usercenter;

/**
 * 用户银行卡信息
 */
public class UserCenterBank {
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 证件类型
	 */
	private String cardType;
	/**
	 * 证件号
	 */
	private String cardNo;
	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 银行卡号
	 */
	private String bankcarkNo;
	/**
	 * 手机号
	 */
	private String phoneNo;
	/**
	 * 银行编码
	 */
	private String bankCode;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankcarkNo() {
		return bankcarkNo;
	}

	public void setBankcarkNo(String bankcarkNo) {
		this.bankcarkNo = bankcarkNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}
