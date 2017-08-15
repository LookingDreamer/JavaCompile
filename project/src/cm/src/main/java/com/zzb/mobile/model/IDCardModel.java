package com.zzb.mobile.model;

public class IDCardModel {
	private String name;//姓名
    private String cardnum;//身份证号
    private String sex;//性别
    private String nation;//民族
    private String birthday;//生日
    private String address;//地址
    private String authority;//签发单位
    private String expdate;//有效期
    private String telephone;//手机号
    
    private String taskid;//任务号
    private String inscomcode;//供应商
    private String agentid; //代理人
	private String expstartdate;//开始有效期限
	private String expenddate;//结束有效期限
    
    
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
}
