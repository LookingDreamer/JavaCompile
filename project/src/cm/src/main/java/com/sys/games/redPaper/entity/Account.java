package com.sys.games.redPaper.entity;

public class Account {

   private String accountId;//工号ID
   private String accountCode;//工号编码
   private String accountName;//姓名
   private String mobilePhone;//电话号码
   private String orgCodeTwo;//机构编码
   private String orgNameTwo;//机构名称
   private String wexixinNum;//微信号
   private String cityId;//城市编码
   private String areaId;//地区编码
   private String amount;//金额
   
   
   public Account(String _accountCode,String _accountName,String _mobilePhone,String _orgCodeTwo,String _orgNameTwo,String _wexixinNum,String _cityId,String _areaId,String _amount) {
	  this.accountCode=_accountCode;
	  this.accountName=_accountName;
	  this.mobilePhone=_mobilePhone;
	  this.orgCodeTwo=_orgCodeTwo;
	  this.orgNameTwo=_orgNameTwo;
	  this.wexixinNum=_wexixinNum;
	  this.cityId=_cityId;
	  this.areaId=_areaId;
	  this.amount=_amount;
   }
   
   
   public String getAccountId() {
	return accountId;
   }
   public void setAccountId(String accountId) {
	this.accountId = accountId;
   }
   
   public String getAccountCode() {
	return accountCode;
   }
   public void setAccountCode(String accountCode) {
	this.accountCode = accountCode;
   }
   
   public String getAccountName() {
	return accountName;
   }
   public void setAccountName(String accountName) {
	this.accountName = accountName;
   }
   
   public String getMobilePhone() {
	return mobilePhone;
   }
   public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
   }
   
   public String getOrgCodeTwo() {
	return orgCodeTwo;
   }
   public void setOrgCodeTwo(String orgCodeTwo) {
	this.orgCodeTwo = orgCodeTwo;
   }
   
   public String getOrgNameTwo() {
	return orgNameTwo;
   }
   public void setOrgNameTwo(String orgNameTwo) {
	this.orgNameTwo = orgNameTwo;
   }
   
   public String getWexixinNum() {
	return wexixinNum;
   }
   public void setWexixinNum(String wexixinNum) {
 	this.wexixinNum = wexixinNum;
   }
   
   public String getCityId() {
	return cityId;
   }
   
   public void setCityId(String cityId) {
	this.cityId = cityId;
   }
   public String getAreaId() {
   return areaId;
   }
   
   public void setAreaId(String areaId) {
   this.areaId = areaId;
   }

   public String getAmount() {
	return amount;
   }
   public void setAmount(String amount) {
	this.amount = amount;
   }


@Override
public String toString() {
	return "Account [accountId=" + accountId + ", accountCode=" + accountCode
			+ ", accountName=" + accountName + ", mobilePhone=" + mobilePhone
			+ ", orgCodeTwo=" + orgCodeTwo + ", orgNameTwo=" + orgNameTwo
			+ ", wexixinNum=" + wexixinNum + ", cityId=" + cityId + ", areaId="
			+ areaId + ", amount=" + amount + "]";
}	
  
   
   
   
}
