package com.cninsure.system.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSCFdcom extends BaseEntity implements Identifiable {

	private static final long serialVersionUID = 1L;

	/**
	 * 对内机构代码
	 */
	private String incomcode;
	/**
	 * 机构编码
	 */
	private String comcode;
	/**
	 * 上级机构代码
	 */
	private String upcomcode;
	/**
	 * 机构名称
	 */
	private String comname;
	/**
	 * 短名称
	 */
	private String shortname;
	/**
	 * 机构类别
	 */
	private String comkind;
	/**
	 * 机构类型
	 */
	private String comtype;
	/**
	 * 机构级别
	 */
	private String comgrade;
	/**
	 * 育成机构编码
	 */
	private String rearcomcode;
	/**
	 * 所在省
	 */
	private String province;
	/**
	 * 所在市
	 */
	private String city;
	/**
	 * 所在县
	 */
	private String county;
	/**
	 * 机构地址
	 */
	private String address;
	/**
	 * 机构邮编
	 */
	private String zipcode;
	/**
	 * 机构电话
	 */
	private String phone;
	/**
	 * 机构传真
	 */
	private String fax;
	/**
	 * EMail
	 */
	private String email;
	/**
	 * 网址
	 */
	private String webaddress;
	/**
	 * 机构负责人姓名
	 */
	private String satrapname;
	/**
	 * 机构负责人代码
	 */
	private String satrapcode;
	/**
	 * 是否子节点
	 */
	private String sonnodeflag;
	/**
	 * 机构层级数
	 */
	private Integer levelnum;
	/**
	 * 操作员代码
	 */
	private String operator;
	/**
	 * 创建时间
	 */
	private Date maketime;
	/**
	 * 修改时间
	 */
	private Date modifytime;
	
	/**
	 * 停业时间
	 */
	private String endFlag;

	public String getIncomcode() {
		return incomcode;
	}

	public void setIncomcode(String incomcode) {
		this.incomcode = incomcode;
	}

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getUpcomcode() {
		return upcomcode;
	}

	public void setUpcomcode(String upcomcode) {
		this.upcomcode = upcomcode;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getComkind() {
		return comkind;
	}

	public void setComkind(String comkind) {
		this.comkind = comkind;
	}

	public String getComtype() {
		return comtype;
	}

	public void setComtype(String comtype) {
		this.comtype = comtype;
	}

	public String getComgrade() {
		return comgrade;
	}

	public void setComgrade(String comgrade) {
		this.comgrade = comgrade;
	}

	public String getRearcomcode() {
		return rearcomcode;
	}

	public void setRearcomcode(String rearcomcode) {
		this.rearcomcode = rearcomcode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebaddress() {
		return webaddress;
	}

	public void setWebaddress(String webaddress) {
		this.webaddress = webaddress;
	}

	public String getSatrapname() {
		return satrapname;
	}

	public void setSatrapname(String satrapname) {
		this.satrapname = satrapname;
	}

	public String getSatrapcode() {
		return satrapcode;
	}

	public void setSatrapcode(String satrapcode) {
		this.satrapcode = satrapcode;
	}

	public String getSonnodeflag() {
		return sonnodeflag;
	}

	public void setSonnodeflag(String sonnodeflag) {
		this.sonnodeflag = sonnodeflag;
	}

	public Integer getLevelnum() {
		return levelnum;
	}

	public void setLevelnum(Integer levelnum) {
		this.levelnum = levelnum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getMaketime() {
		return maketime;
	}

	public void setMaketime(Date maketime) {
		this.maketime = maketime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	@Override
	public String toString() {
		return "INSCFdcom [incomcode=" + incomcode + ", comcode=" + comcode
				+ ", upcomcode=" + upcomcode + ", comname=" + comname
				+ ", shortname=" + shortname + ", comkind=" + comkind
				+ ", comtype=" + comtype + ", comgrade=" + comgrade
				+ ", rearcomcode=" + rearcomcode + ", province=" + province
				+ ", city=" + city + ", county=" + county + ", address="
				+ address + ", zipcode=" + zipcode + ", phone=" + phone
				+ ", fax=" + fax + ", email=" + email + ", webaddress="
				+ webaddress + ", satrapname=" + satrapname + ", satrapcode="
				+ satrapcode + ", sonnodeflag=" + sonnodeflag + ", levelnum="
				+ levelnum + ", operator=" + operator + ", maketime="
				+ maketime + ", modifytime=" + modifytime + ", endFlag=" + endFlag + "]";
	}

}
