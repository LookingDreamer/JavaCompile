package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSCDept extends BaseEntity implements Identifiable {

	private static final long serialVersionUID = 1L;

	/**
	 * 机构内部编码
	 */
	private String deptinnercode;
	
	/**
	 * 是否为试用机构 0是，1不是
	 */
	private String trydept;
	/**
	 * 机构编码
	 */
	private String comcode;

	/**
	 * 上级机构
	 */
	private String upcomcode;

	/**
	 * 机构树路径
	 */
	private String treedept;

	/**
	 * 机构名称
	 */
	private String comname;

	/**
	 * 机构简称
	 */
	private String shortname;

	/**
	 * 机构性质
	 */
	private String comkind;

	/**
	 * 机构业务类型
	 */
	private String combustype;

	/**
	 * 机构类型
	 */
	private String comtype;

	/**
	 * 机构等级
	 */
	private String comgrade;

	/**
	 * 类型 0-停业 1-营业
	 */
	private String type;

	/**
	 * 权限机构id
	 */
	private String deptid;

	/**
	 * 育成机构代码
	 */
	private String rearcomcode;

	/**
	 * 省code
	 */
	private String province;

	/**
	 * 市code
	 */
	private String city;

	/**
	 * 县code
	 */
	private String county;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 邮政编码
	 */
	private String zipcode;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 网页地址
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
	 * 机构负责人电话
	 */
	private String satrapphone;

	/**
	 * 子节点标记
	 */
	private String childflag;

	/**
	 * 排序
	 */
	private Integer orderflag;

	/**
	 * 节点层级
	 */
	private String treelevel;

	/**
	 * 状态
	 */
	private String status;
	
	
	private String parentcodes;
	
	/**
	 * 权限包名称以及id
	 * @return
	 */
	private String tryset;
	private String formalset;
	private String channelset;

	public String getTryset() {
		return tryset;
	}
	public void setTryset(String tryset) {
		this.tryset = tryset;
	}
	public String getFormalset() {
		return formalset;
	}
	public void setFormalset(String formalset) {
		this.formalset = formalset;
	}
	public String getChannelset() {
		return channelset;
	}
	public void setChannelset(String channelset) {
		this.channelset = channelset;
	}

	
	public String getDeptinnercode() {
		return deptinnercode;
	}

	public void setDeptinnercode(String deptinnercode) {
		this.deptinnercode = deptinnercode;
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

	public String getTreedept() {
		return treedept;
	}

	public void setTreedept(String treedept) {
		this.treedept = treedept;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
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

	public String getSatrapphone() {
		return satrapphone;
	}

	public void setSatrapphone(String satrapphone) {
		this.satrapphone = satrapphone;
	}

	public String getChildflag() {
		return childflag;
	}

	public void setChildflag(String childflag) {
		this.childflag = childflag;
	}

	public Integer getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(Integer orderflag) {
		this.orderflag = orderflag;
	}

	public String getTreelevel() {
		return treelevel;
	}

	public void setTreelevel(String treelevel) {
		this.treelevel = treelevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTrydept() {
		return trydept;
	}

	public void setTrydept(String trydept) {
		this.trydept = trydept;
	}

	public String getCombustype() {
		return combustype;
	}

	public void setCombustype(String combustype) {
		this.combustype = combustype;
	}

	public String getParentcodes() {
		return parentcodes;
	}

	public void setParentcodes(String parentcodes) {
		this.parentcodes = parentcodes;
	}
	@Override
	public String toString() {
		return "INSCDept [deptinnercode=" + deptinnercode + ", trydept=" + trydept + ", comcode=" + comcode
				+ ", upcomcode=" + upcomcode + ", treedept=" + treedept + ", comname=" + comname + ", shortname="
				+ shortname + ", comkind=" + comkind + ", comtype=" + comtype + ", comgrade=" + comgrade + ", type="
				+ type + ", deptid=" + deptid + ", rearcomcode=" + rearcomcode + ", province=" + province + ", city="
				+ city + ", county=" + county + ", address=" + address + ", zipcode=" + zipcode + ", phone=" + phone
				+ ", fax=" + fax + ", email=" + email + ", webaddress=" + webaddress + ", satrapname=" + satrapname
				+ ", satrapcode=" + satrapcode + ", satrapphone=" + satrapphone + ", childflag=" + childflag
				+ ", orderflag=" + orderflag + ", treelevel=" + treelevel + ", status=" + status + ", parentcodes="
				+ parentcodes + ", tryset=" + tryset + ", formalset=" + formalset + ", channelset=" + channelset + "]";
	}

}