package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSHPerson extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 英文名
	 */
	private String ename;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 生日
	 */
	private Date birthday;

	/**
	 * 血型
	 */
	private Integer bloodtype;

	/**
	 * 
	 */
	private Integer age;

	/**
	 * 证件类型
	 */
	private Integer idcardtype;

	/**
	 * 证件号码
	 */
	private String idcardno;

	/**
	 * 手机号
	 */
	private String cellphone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 省
	 */
	private Integer province;

	/**
	 * 城
	 */
	private Integer city;

	/**
	 * 区县
	 */
	private Integer area;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 
	 */
	private String eaddress;

	/**
	 * 邮编
	 */
	private String zip;

	/**
	 * 驾驶证类型
	 */
	private String licensetype;

	/**
	 * 驾驶证号码
	 */
	private String licenseno;

	/**
	 * 驾驶证发照日期
	 */
	private Date licensedate;

	/**
	 * 
	 */
	private String maritalstatus;

	/**
	 * 
	 */
	private String job;

	/**
	 * 
	 */
	private String educateinfo;

	/**
	 * 精灵或edi，robot-精灵，edi-EDI
	 */
	private String fairyoredi;

	/**
	 * 流程节点，A-报价，B-核保回写，D-承保回写
	 */
	private String nodecode;

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

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(Integer bloodtype) {
		this.bloodtype = bloodtype;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getIdcardtype() {
		return idcardtype;
	}

	public void setIdcardtype(Integer idcardtype) {
		this.idcardtype = idcardtype;
	}

	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEaddress() {
		return eaddress;
	}

	public void setEaddress(String eaddress) {
		this.eaddress = eaddress;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLicensetype() {
		return licensetype;
	}

	public void setLicensetype(String licensetype) {
		this.licensetype = licensetype;
	}

	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public Date getLicensedate() {
		return licensedate;
	}

	public void setLicensedate(Date licensedate) {
		this.licensedate = licensedate;
	}

	public String getMaritalstatus() {
		return maritalstatus;
	}

	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEducateinfo() {
		return educateinfo;
	}

	public void setEducateinfo(String educateinfo) {
		this.educateinfo = educateinfo;
	}

	public String getFairyoredi() {
		return fairyoredi;
	}

	public void setFairyoredi(String fairyoredi) {
		this.fairyoredi = fairyoredi;
	}

	public String getNodecode() {
		return nodecode;
	}

	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}

}