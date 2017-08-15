package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class IDCardBean {
	private String name; //姓名
    private String cardnum; //身份证号
    private String sex; //性别
    private String nation; //民族
    private String birthday; //生日
    private String address; //地址
    private String authority; //签发单位
    private String expdate; //有效期
    private String telephone; //手机号
    
    private String expstartdate; //开始有效期限
	private String expenddate; //结束有效期限

	public String getExpstartdate() {
		return expstartdate;
	}
	public void setExpstartdate(String expstartdate) {
		this.expstartdate = expstartdate;
	}
	public String getExpenddate() {
		return expenddate;
	}
	public void setExpenddate(String expenddate) {
		this.expenddate = expenddate;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getExpdate() {
		return expdate;
	}
	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}
}
